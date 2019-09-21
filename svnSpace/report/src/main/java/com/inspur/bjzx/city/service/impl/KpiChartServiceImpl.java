package com.inspur.bjzx.city.service.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inspur.bjzx.city.service.KpiChartService;
import com.inspur.bjzx.city.util.DateStringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class KpiChartServiceImpl implements KpiChartService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public JSONArray getKpiInfo(String time, String condition, String type) {

        List<ImmutableMap<String, String>> chartInfos = getChartInfoMaps(condition, type);

        JSONArray chartArray = new JSONArray();
        for (ImmutableMap<String, String> chartInfo : chartInfos) {
            chartArray.add(makeChartJson(time, chartInfo));
        }
        return chartArray;
    }

    private JSONObject makeChartJson(String time, ImmutableMap<String, String> chartInfo) {
        JSONObject chartObject = new JSONObject();
        chartObject.put("id", Integer.parseInt(chartInfo.get("id")));
        chartObject.put("clock", Boolean.parseBoolean(chartInfo.get("clock")));
        chartObject.put("legend", Boolean.parseBoolean(chartInfo.get("legend")));
        chartObject.put("timegrad", chartInfo.get("time_grad"));
        chartObject.put("charttype", chartInfo.get("chart_type"));
        chartObject.put("title", chartInfo.get("chart_title"));
        chartObject.put("subtitle", chartInfo.get("sub_title"));
        chartObject.put("xTitle", "");
        chartObject.put("yTitle", chartInfo.get("yTitle"));
        chartObject.put("min", "0");
        chartObject.put("max", chartInfo.get("yMax"));
        Integer time_scale = Integer.parseInt(chartInfo.get("time_scale"));

        String timetmp = getKpiMaxDate(chartInfo.get("time_grad"), time, chartInfo.get("table_name"));
        if (timetmp != null) {
            chartObject.put("time", timetmp);

            String[] chart_time = DateStringUtil.getTimeRange(chartInfo.get("time_grad"), timetmp, time_scale - 1).split(";");

            String chartTimeFormat = getChartTimeFormat(chartInfo.get("time_grad"));

            Map<String, List<String>> chartInfoMap = getChartDataListMap(chartInfo, time_scale, chart_time, chartTimeFormat);

            //处理特殊指标
            if (judgeNoNull(chartInfo.get("s_kpi_field"))) {
                String sKpi = getSKpiInfo(chartInfo, chart_time);
                chartObject.put("sKpi", sKpi);
            }

            if (chartInfoMap.size() > 0) {
                //判断图形的类型
                boolean flag = true;
                if (chartInfo.get("chart_type").equals("pie")) {
                    flag = false;
                }
                if (chartInfoMap.get("x").size() > 0) {
                    chartObject.put("xLabels", Joiner.on(",").join(chartInfoMap.get("x")));
                }
                if (judgeNoNull(chartInfo.get("x_kpi_name"))) {
                    String[] chartInfoArray = chartInfo.get("x_kpi_name").split(",");
                    chartObject.put("xLabels", Joiner.on(",").join(chartInfoArray));
                }
                JSONArray y_kpi_json_array = new JSONArray();
                if (chartInfo.get("y_kpi_field").contains(",")) {
                    dealMutiKpisValuesToJson(chartInfo, chartInfoMap, flag, y_kpi_json_array);
                } else {
                    dealOneKpiValuesToJson(chartInfo, chartInfoMap, flag, y_kpi_json_array);
                }
                chartObject.put("series", y_kpi_json_array);
            } else {
                System.out.println("-----【" + chartObject.get("title") + "】表名：【" + chartInfo.get("table_name") + "】时间：【" + chart_time[0] + "--" + chart_time[1] + "】无数据");
            }
        }
        return chartObject;
    }

    private Map<String, List<String>> getChartDataListMap(ImmutableMap<String, String> chartInfo, Integer time_scale, String[] chart_time, String chartTimeFormat) {
        String chart_sql;
        chart_sql = makeChartSql(chartInfo, time_scale, chartTimeFormat);
        System.out.println("*** sql:" + chart_sql + "\n***** time:" + chart_time[0] + "---" + chart_time[1]);

        return (Map<String, List<String>>) jdbcTemplate.query(chart_sql, new Object[]{chart_time[0], chart_time[1]}, new ResultSetExtractor() {
            public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                List<String> list_x = new ArrayList<String>();
                /*List<String> list_y = new ArrayList<String>();*/
                List list_y = new ArrayList();
                Map<String, List<String>> map = Maps.newLinkedHashMap();
                //判断图形的类型
                boolean flag = true;
                String charttype = (String) chartInfo.get("chart_type");
                if (charttype.equals("pie")) {
                    flag = false;
                }
                while (resultSet.next()) {
                    String x_kpi_field = chartInfo.get("x_kpi_field");
                    if (null != x_kpi_field && !"".equals(x_kpi_field)) {
                        list_x.add(resultSet.getString(x_kpi_field) == null ? "" : resultSet.getString(x_kpi_field));
                    }
                    map.put("x", Lists.newArrayList(list_x));
                    if (chartInfo.get("y_kpi_field").contains(",")) {
                        for (String y_kpi_chart : chartInfo.get("y_kpi_field").split(",")) {
                            if (flag) {
                                List<String> y_kpi_chart_list = map.get(y_kpi_chart);
                                if (y_kpi_chart_list == null) {
                                    y_kpi_chart_list = Lists.newArrayList();
                                }
                                y_kpi_chart_list.add(String.valueOf(Double.parseDouble(resultSet.getString(y_kpi_chart) == null ? "0" : resultSet.getString(y_kpi_chart))));
                                map.put(y_kpi_chart, Lists.newArrayList(y_kpi_chart_list));
                            } else {
                                List<String> y_kpi_chart_list = map.get(y_kpi_chart);
                                if (y_kpi_chart_list == null) {
                                    y_kpi_chart_list = Lists.newArrayList();
                                }
                                y_kpi_chart_list.add(resultSet.getString(y_kpi_chart) == null ? "" : resultSet.getString(y_kpi_chart));
                                map.put(y_kpi_chart, Lists.newArrayList(y_kpi_chart_list));
                            }
                        }
                    } else {
                        if (flag) {
                            list_y.add(Double.parseDouble(resultSet.getString(chartInfo.get("y_kpi_field")) == null ? "0" : resultSet.getString(chartInfo.get("y_kpi_field"))));
                            map.put(chartInfo.get("y_kpi_field"), Lists.newArrayList(list_y));
                        } else {
                            list_y.add(resultSet.getString(chartInfo.get("y_kpi_field")) == null ? "" : resultSet.getString(chartInfo.get("y_kpi_field")));
                            map.put(chartInfo.get("y_kpi_field"), Lists.newArrayList(list_y));
                        }

                    }
                }
                return map;
            }
        });
    }

    private String getSKpiInfo(ImmutableMap<String, String> chartInfo, String[] chart_time) {

        StringBuffer chart_sql = new StringBuffer();

        chart_sql.append("SELECT ");

        String[] s_kpi_fields = chartInfo.get("s_kpi_field").split(",");
        for (int i = 0; i < s_kpi_fields.length; i++) {
            chart_sql.append("TO_CHAR(").append(s_kpi_fields[i]).append(", 'fm9999990.00') AS ").append(s_kpi_fields[i]);

            if (i != s_kpi_fields.length - 1) {
                chart_sql.append(",");
            }
        }


        chart_sql.append(" FROM ").append(chartInfo.get("table_name"));
        chart_sql.append(" WHERE ").append(chartInfo.get("time_field")).append(" BETWEEN TO_DATE(?, 'yyyy-MM-dd hh24:mi:ss') AND TO_DATE(?, 'yyyy-MM-dd hh24:mi:ss') ");


        System.out.println("*** sql:" + chart_sql + "\n***** time:" + chart_time[0] + "---" + chart_time[1]);

        return (String) jdbcTemplate.query(chart_sql.toString(), new Object[]{chart_time[0], chart_time[1]}, new ResultSetExtractor() {
            public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {

                StringBuffer sKpiStr = new StringBuffer();
                while (resultSet.next()) {

                    for (int i = 0; i < s_kpi_fields.length; i++) {
                        sKpiStr.append(s_kpi_fields[i]).append(":").append(resultSet.getString(s_kpi_fields[i]));
                        if (i != s_kpi_fields.length - 1) {
                            sKpiStr.append(",");
                        }
                    }
                }
                return sKpiStr.toString();
            }
        });

    }

    private List<ImmutableMap<String, String>> getChartInfoMaps(String condition, String type) {
        String sql = "";
        if ("MATAKPI_ID".equals(type)) {
            sql = "SELECT ID,CHART_TYPE,CHART_TITLE,X_KPI_FIELD,Y_KPI_FIELD,TABLE_NAME,TIME_FIELD,TIME_GRAD,TIME_SCALE,ORDER_FIELD,ORDER_TYPE,FILTER,X_KPI_NAME,Y_KPI_NAME,CLOCK,LEGEND,SUB_TITLE,YTITLE,S_KPI_FIELD,YMAX FROM PALM_CHART_CONF WHERE MATAKPI_ID = ?";
        } else if ("ID".equals(type)) {
            sql = "SELECT ID,CHART_TYPE,CHART_TITLE,X_KPI_FIELD,Y_KPI_FIELD,TABLE_NAME,TIME_FIELD,TIME_GRAD,TIME_SCALE,ORDER_FIELD,ORDER_TYPE,FILTER,X_KPI_NAME,Y_KPI_NAME,CLOCK,LEGEND,SUB_TITLE,YTITLE,S_KPI_FIELD,YMAX FROM PALM_CHART_CONF WHERE ID = ?";
        }
        return jdbcTemplate.query(sql, new Object[]{condition}, (resultSet, i) -> ImmutableMap.<String, String>builder()
                .put("chart_type", getDbValue(resultSet, "CHART_TYPE"))
                .put("chart_title", getDbValue(resultSet, "CHART_TITLE"))
                .put("x_kpi_field", getDbValue(resultSet, "X_KPI_FIELD"))
                .put("y_kpi_field", getDbValue(resultSet, "Y_KPI_FIELD"))
                .put("x_kpi_name", getDbValue(resultSet, "X_KPI_NAME"))
                .put("y_kpi_name", getDbValue(resultSet, "Y_KPI_NAME"))
                .put("table_name", getDbValue(resultSet, "TABLE_NAME"))
                .put("time_field", getDbValue(resultSet, "TIME_FIELD"))
                .put("time_grad", getDbValue(resultSet, "TIME_GRAD"))
                .put("time_scale", getDbValue(resultSet, "TIME_SCALE"))
                .put("order_field", getDbValue(resultSet, "ORDER_FIELD"))
                .put("order_type", getDbValue(resultSet, "ORDER_TYPE"))
                .put("filter", getDbValue(resultSet, "FILTER"))
                .put("clock", getDbValue(resultSet, "CLOCK"))
                .put("legend", getDbValue(resultSet, "LEGEND"))
                .put("sub_title", getDbValue(resultSet, "SUB_TITLE"))
                .put("yTitle", getDbValue(resultSet, "YTITLE"))
                .put("id", getDbValue(resultSet, "ID"))
                .put("s_kpi_field", getDbValue(resultSet, "S_KPI_FIELD"))
                .put("yMax", getDbValue(resultSet, "YMAX"))
                .build());
    }

    private void dealOneKpiValuesToJson(ImmutableMap<String, String> chartInfo, Map<String, List<String>> chartInfoMap, boolean flag, JSONArray y_kpi_json_array) {
        JSONObject y_kpi_json_object = new JSONObject();
        y_kpi_json_object.put("name", chartInfo.get("y_kpi_name"));
        List list = chartInfoMap.get(chartInfo.get("y_kpi_field"));
        if (flag) {
            Double[] y_kpi_value = (Double[]) list.toArray(new Double[list.size()]);
            y_kpi_json_object.put("data", y_kpi_value);
            y_kpi_json_array.add(y_kpi_json_object);
        } else {
            String[] y_kpi_value = (String[]) list.toArray(new String[list.size()]);
            y_kpi_json_object.put("data", y_kpi_value);
            y_kpi_json_array.add(y_kpi_json_object);
        }
    }

    private void dealMutiKpisValuesToJson(ImmutableMap<String, String> chartInfo, Map<String, List<String>> chartInfoMap, boolean flag, JSONArray y_kpi_json_array) {
        Object[] y_kpi_fields = chartInfo.get("y_kpi_field").split(",");


        if (judgeNoNull(chartInfo.get("y_kpi_name"))) {
            Object[] y_kpi_names = chartInfo.get("y_kpi_name").split(",");
            if(y_kpi_names.length == 1 ){
                //单序列
                singleSeries(chartInfo, chartInfoMap, y_kpi_json_array, y_kpi_fields);
            }else {
                //多序列
                mutiSeries(chartInfo, chartInfoMap, flag, y_kpi_json_array, y_kpi_fields);
            }
        } else {
            //单序列
            singleSeries(chartInfo, chartInfoMap, y_kpi_json_array, y_kpi_fields);
        }
    }

    private void singleSeries(ImmutableMap<String, String> chartInfo, Map<String, List<String>> chartInfoMap, JSONArray y_kpi_json_array, Object[] y_kpi_fields) {
        //单序列
        JSONObject y_kpi_json_object = new JSONObject();
        List<Double> y_values = new ArrayList<Double>();
        if(judgeNoNull(chartInfo.get("y_kpi_name"))){
            y_kpi_json_object.put("name", chartInfo.get("y_kpi_name"));
        }else{
            y_kpi_json_object.put("name", "name");
        }

        for (int i = 0; i < y_kpi_fields.length; i++) {
            String y_kpi_chart = chartInfo.get("y_kpi_field").split(",")[i];
            List list = chartInfoMap.get(y_kpi_chart);
            if (list != null && list.size() > 0) {
                y_values.add(Double.parseDouble(list.get(0).toString()));

            } else {
                y_values.add(Double.parseDouble("0"));
            }
        }
        y_kpi_json_object.put("data", y_values);
        y_kpi_json_array.add(y_kpi_json_object);
    }

    private void mutiSeries(ImmutableMap<String, String> chartInfo, Map<String, List<String>> chartInfoMap, boolean flag, JSONArray y_kpi_json_array, Object[] y_kpi_fields) {
        Object[] y_kpi_names = chartInfo.get("y_kpi_name").split(",");
        for (int i = 0; i < y_kpi_fields.length; i++) {
            String y_kpi_chart = chartInfo.get("y_kpi_field").split(",")[i];
            JSONObject y_kpi_json_object = new JSONObject();
            y_kpi_json_object.put("name", y_kpi_names[i]);
            List list = chartInfoMap.get(y_kpi_chart);
            if (flag) {
                if (list != null && list.size() > 0) {
                    Double[] y_kpi_value = new Double[list.size()];
                    for (int j = 0; j < list.size(); j++) {
                        y_kpi_value[j] = Double.parseDouble(list.get(j).toString());
                    }
                    y_kpi_json_object.put("data", y_kpi_value);
                } else {
                    y_kpi_json_object.put("data", "");
                }
                y_kpi_json_array.add(y_kpi_json_object);
            } else {
                String[] y_kpi_value = (String[]) list.toArray(new String[list.size()]);
                y_kpi_json_object.put("data", y_kpi_value);
                y_kpi_json_array.add(y_kpi_json_object);
            }
        }
    }

    private String makeChartSql(ImmutableMap<String, String> chartInfo, int time_scale, String chartTimeFormat) {
        StringBuffer chart_sql = new StringBuffer();
        if (time_scale == 1) {

            chart_sql.append("SELECT ");
            if (judgeNoNull(chartInfo.get("x_kpi_field"))) {
                chart_sql.append(chartInfo.get("x_kpi_field")).append(", ");
            }
            String[] y_kpi_fields = chartInfo.get("y_kpi_field").split(",");
            for (int i = 0; i < y_kpi_fields.length; i++) {
                chart_sql.append("TO_CHAR(").append(y_kpi_fields[i]).append(", 'fm9999990.00') AS ").append(y_kpi_fields[i]);

                if (i != y_kpi_fields.length - 1) {
                    chart_sql.append(",");
                }
            }
        } else {
            if ("STATISTICAL_TIME".equals((chartInfo.get("x_kpi_field")).trim().toUpperCase())) {
                chart_sql.append("SELECT TO_CHAR(" + chartInfo.get("x_kpi_field") + ", '" + chartTimeFormat + "') AS " + chartInfo.get("x_kpi_field") + ", " + chartInfo.get("y_kpi_field"));
            } else {
                if("".equals(chartInfo.get("x_kpi_field"))){
                    chart_sql.append("SELECT " + chartInfo.get("y_kpi_field"));
                }else if("".equals(chartInfo.get("y_kpi_field"))) {
                    chart_sql.append("SELECT " + chartInfo.get("x_kpi_field") + " AS " + chartInfo.get("x_kpi_field"));
                }else{
                    chart_sql.append("SELECT " + chartInfo.get("x_kpi_field") + " AS " + chartInfo.get("x_kpi_field") + ", " + chartInfo.get("y_kpi_field"));
                }


            }
        }

        chart_sql.append(" FROM ").append(chartInfo.get("table_name"));
        chart_sql.append(" WHERE ").append(chartInfo.get("time_field")).append(" BETWEEN TO_DATE(?, 'yyyy-MM-dd hh24:mi:ss') AND TO_DATE(?, 'yyyy-MM-dd hh24:mi:ss') ");

        if (judgeNoNull(chartInfo.get("filter"))) {
            chart_sql.append(" AND ").append(chartInfo.get("filter"));
        }

        if (judgeNoNull(chartInfo.get("order_field"))) {
            String orderType = chartInfo.get("order_type") == null ? "ASC" : chartInfo.get("order_type");
            chart_sql.append(" ORDER BY ").append(chartInfo.get("order_field")).append(" ").append(orderType);
        }

        return chart_sql.toString();
    }

    private String getChartTimeFormat(String timeGard) {
        if ("YEAR".equals(timeGard)) {
            return "yyyy";
        } else if ("MONTH".equals(timeGard)) {
            return "yyyy-MM";
        } else {
            return "yyyy-MM-dd";
        }
    }

    private String getKpiMaxDate(String timeGrad, String time, String kpi_table) {
        String maxTimeSql = "SELECT  max(STATISTICAL_TIME) AS MAX_TIME FROM " + kpi_table + " WHERE  STATISTICAL_TIME <= TO_DATE(?, 'yyyy-MM-dd hh24:mi:ss') ";
        System.out.print("kpiValueSql:" + maxTimeSql);

        String[] timeRange = DateStringUtil.getTimeRange(timeGrad, time, 0).split(";");
        Object obj = jdbcTemplate.query(maxTimeSql, new Object[]{timeRange[1]}, (resultSet, i) -> ImmutableMap.<String, Object>builder()
                .put("MAX_TIME", (resultSet.getObject("MAX_TIME") == null) ? "" : resultSet.getObject("MAX_TIME"))
                .build()).get(0).get("MAX_TIME");

        if (obj instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(obj);
        }

        return null;
    }

    private boolean judgeNoNull(Object obj) {
        return null != obj && !"".equals(((String) obj).trim());

    }

    private String getDbValue(ResultSet resultSet, String chart_type) throws SQLException {
        return StringUtils.isEmpty(resultSet.getObject(chart_type)) ? "" : resultSet.getString(chart_type);
    }
}
