package com.inspur.bjzx.city.service.impl;

import bsh.EvalError;
import bsh.Interpreter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.inspur.bjzx.city.service.KpiService;
import com.inspur.bjzx.city.util.DateStringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class KpiServiceImpl implements KpiService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("${default_color}")
    String defaultColor;

    @Override
    public List<ImmutableMap<String, String>> getKpiGrpInfo(String userId) {
        String sql = "SELECT ID,GRP_NAME FROM PALM_KPIGRP_CONF ORDER BY GRP_ORDER";
        return jdbcTemplate.query(sql, (resultSet, i) -> ImmutableMap.<String, String>builder()
                .put("id", getDbValue(resultSet, "ID"))
                .put("name", getDbValue(resultSet, "GRP_NAME"))
                .build());
    }

    @Override
    public JSONArray getKpiLists(String userId, String kpiGrpId, String time, String regionId) {
        List<ImmutableMap<String, String>> kpiModels;
        if (StringUtils.isEmpty(kpiGrpId)) {
            kpiModels = getUserKpiModels(userId);
        } else {
            kpiModels = getGrpKpiModels(userId, kpiGrpId);
        }
        return getKpiValuesJsonArray(kpiGrpId, regionId, kpiModels, time);
    }

    private List<ImmutableMap<String, String>> getUserKpiModels(String userId) {
        List<ImmutableMap<String, String>> kpiModels;
        String kpiSql = "SELECT\n" +
                "    PALM_KPIGRP_CONF.ID GRP_ID,\n" +
                "    PALM_KPIGRP_CONF.GRP_NAME,\n" +
                "    PALM_MATAKPI_CONF.ID,\n" +
                "    PALM_MATAKPI_CONF.NAME,\n" +
                "    PALM_MATAKPI_CONF.KPI_TABLE,\n" +
                "    PALM_MATAKPI_CONF.KPI_ALGORITHM,\n" +
                "    PALM_MATAKPI_CONF.DEFAULT_TIME_GRAD,\n" +
                "    PALM_MATAKPI_CONF.KPI_FIELD,\n" +
                "    PALM_MATAKPI_CONF.KPI_NAME,\n" +
                "    PALM_MATAKPI_CONF.KPI_UNIT,\n" +
                "    PALM_MATAKPI_CONF.KPI_LATTICE,\n" +
                "    PALM_MATAKPI_CONF.KPI_THRESHOLD,\n" +
                "    PALM_MATAKPI_CONF.KPI_VALUE_TYPE\n" +
                "FROM\n" +
                "    PALM_MATAKPI_CONF\n" +
                "INNER JOIN\n" +
                "    PALM_KPIGRP_CONF\n" +
                "ON\n" +
                "    (\n" +
                "        PALM_MATAKPI_CONF.GRP_ID = PALM_KPIGRP_CONF.ID)\n" +
                "INNER JOIN\n" +
                "    PALM_ATTENTION_KPI\n" +
                "ON\n" +
                "    (\n" +
                "        PALM_MATAKPI_CONF.ID = PALM_ATTENTION_KPI.MATAKPI_ID)\n" +
                "WHERE\n" +
                "    PALM_ATTENTION_KPI.USER_ID = ?\n" +
                "ORDER BY\n" +
                "    PALM_KPIGRP_CONF.GRP_ORDER ASC,\n" +
                "    PALM_MATAKPI_CONF.DEFAULT_ORDER ASC";
        kpiModels = jdbcTemplate.query(kpiSql, new Object[]{userId}, (resultSet, i) -> getKpiGrpInfoPut(resultSet)
                .build());
        return kpiModels;
    }

    private List<ImmutableMap<String, String>> getGrpKpiModels(String userId, String kpiGrpId) {
        List<ImmutableMap<String, String>> kpiModels;
        String kpiSql = "SELECT\n" +
                "    PALM_MATAKPI_CONF.ID,\n" +
                "    PALM_MATAKPI_CONF.NAME,\n" +
                "    PALM_MATAKPI_CONF.KPI_TABLE,\n" +
                "    PALM_MATAKPI_CONF.DEFAULT_ORDER,\n" +
                "    PALM_MATAKPI_CONF.DEFAULT_TIME_GRAD,\n" +
                "    PALM_MATAKPI_CONF.KPI_ALGORITHM,\n" +
                "    PALM_MATAKPI_CONF.KPI_FIELD,\n" +
                "    PALM_MATAKPI_CONF.KPI_NAME,\n" +
                "    PALM_MATAKPI_CONF.KPI_UNIT,\n" +
                "    PALM_MATAKPI_CONF.KPI_LATTICE,\n" +
                "    PALM_MATAKPI_CONF.KPI_THRESHOLD,\n" +
                "    PALM_MATAKPI_CONF.KPI_VALUE_TYPE,\n" +
                "    T1.SEL\n" +
                "FROM\n" +
                "    PALM_MATAKPI_CONF\n" +
                "FULL OUTER JOIN\n" +
                "    (SELECT MATAKPI_ID,'1' AS SEL FROM PALM_ATTENTION_KPI WHERE USER_ID = ?) T1\n" +
                "ON\n" +
                "    (\n" +
                "        PALM_MATAKPI_CONF.ID = T1.MATAKPI_ID) " +
                "WHERE\n" +
                "    PALM_MATAKPI_CONF.GRP_ID = ? \n" +
                "ORDER BY\n" +
                "    PALM_MATAKPI_CONF.DEFAULT_ORDER ASC";
        kpiModels = jdbcTemplate.query(kpiSql, new Object[]{userId, kpiGrpId}, (resultSet, i) -> getKpiInfoPut(resultSet)
                .put("sel", StringUtils.isEmpty(resultSet.getObject("SEL")) ? "0" : resultSet.getString("SEL"))
                .build());
        return kpiModels;
    }

    private ImmutableMap.Builder<String, String> getKpiGrpInfoPut(ResultSet resultSet) throws SQLException {
        return ImmutableMap.<String, String>builder()
                .put("grp_id", getDbValue(resultSet, "GRP_ID"))
                .put("grp_name", getDbValue(resultSet, "GRP_NAME"))
                .put("id", getDbValue(resultSet, "ID"))
                .put("name", getDbValue(resultSet, "NAME"))
                .put("kpi_table", getDbValue(resultSet, "KPI_TABLE"))
                .put("default_time_grad", getDbValue(resultSet, "DEFAULT_TIME_GRAD"))
                .put("kpi_algorithm", getDbValue(resultSet, "KPI_ALGORITHM"))
                .put("attr_name", getDbValue(resultSet, "KPI_FIELD"))
                .put("field_name", getDbValue(resultSet, "KPI_NAME"))
                .put("value_unit", getDbValue(resultSet, "KPI_UNIT"))
                .put("for_lattice", getDbValue(resultSet, "KPI_LATTICE"))
                .put("threshold", getDbValue(resultSet, "KPI_THRESHOLD"))
                .put("value_type", getDbValue(resultSet, "KPI_VALUE_TYPE"));
    }

    private ImmutableMap.Builder<String, String> getKpiInfoPut(ResultSet resultSet) throws SQLException {
        return ImmutableMap.<String, String>builder()
                .put("id", getDbValue(resultSet, "ID"))
                .put("name", getDbValue(resultSet, "NAME"))
                .put("kpi_table", getDbValue(resultSet, "KPI_TABLE"))
                .put("default_time_grad", getDbValue(resultSet, "DEFAULT_TIME_GRAD"))
                .put("kpi_algorithm", getDbValue(resultSet, "KPI_ALGORITHM"))
                .put("attr_name", getDbValue(resultSet, "KPI_FIELD"))
                .put("field_name", getDbValue(resultSet, "KPI_NAME"))
                .put("value_unit", getDbValue(resultSet, "KPI_UNIT"))
                .put("for_lattice", getDbValue(resultSet, "KPI_LATTICE"))
                .put("threshold", getDbValue(resultSet, "KPI_THRESHOLD"))
                .put("value_type", getDbValue(resultSet, "KPI_VALUE_TYPE"));
    }

    private JSONArray getKpiValuesJsonArray(String kpiGrpId, String regionId, List<ImmutableMap<String, String>> kpiModels, String time) {
        JSONArray kpiValues = new JSONArray();
        JSONObject kpiValueJson = new JSONObject();
        JSONArray kpiLattice = new JSONArray();
        JSONArray kpiLists = new JSONArray();
        for (int j = 0; j < kpiModels.size(); j++) {
            Map<String, String> kpiValue = Maps.newLinkedHashMap();
            ImmutableMap<String, String> kpiModel = kpiModels.get(j);
            String maxTime = "";

            //查询指标表的值
            List<String> kpiValueMaps = getKpiValuesMap(regionId, kpiModel, time);
            kpiValues = new JSONArray();
            kpiValueJson = new JSONObject();

            if (kpiValueMaps != null && kpiValueMaps.size() > 0) {
                String[] kpiFields = kpiModel.get("attr_name").split(",");
                String[] kpiNames = kpiModel.get("field_name").split(",");
                String[] kpiValueUnits = kpiModel.get("value_unit").split(",");
                String[] kpiValueLattices = kpiModel.get("for_lattice").split(",");

                String[] kpiThresholds = kpiModel.get("threshold").split("@");
                Map<String, String> kpiThresholdMap = new HashMap<>();
                for (int i = 0; i < kpiThresholds.length; i++) {
                    if (!"".equals(kpiThresholds[i])) {
                        String[] sThreshold = kpiThresholds[i].split(",");
                        kpiThresholdMap.put(sThreshold[0], sThreshold[1]);
                    }
                }


                maxTime = kpiValueMaps.get(0);
                for (int i = 0; i < kpiValueMaps.size() - 1; i++) {
                    String kpiThreshold = "";
                    String kpiValueUnit = "";
                    String kpiValueLattice = "";

                    if (kpiThresholdMap.containsKey(kpiFields[i])) {
                        kpiThreshold = kpiThresholdMap.get(kpiFields[i]);
                    }

                    if (kpiValueUnits.length > i) kpiValueUnit = kpiValueUnits[i];
                    if (kpiValueLattices.length > i) kpiValueLattice = kpiValueLattices[i];

                    String kpiSearchValue = kpiValueMaps.get(i + 1);
                    kpiValue.put("attr", kpiNames[i]);


                    //处理数值颜色
                    dealChartColor(kpiValue, kpiSearchValue, kpiThreshold, kpiValueUnit);
                    kpiLattice.add(kpiValue);

                    //处理占位位置
                    if (("2").equals(kpiValueLattice)) {
                        kpiValues.add(kpiLattice);
                        kpiLattice = new JSONArray();
                    } else {
                        if (kpiLattice.size() == 2) {
                            kpiValues.add(kpiLattice);
                            kpiLattice = new JSONArray();
                        }
                    }
                }

            }
            kpiValueJson.put("time", maxTime);
            kpiValueJson.put("kpiname", kpiModel.get("name"));
            kpiValueJson.put("timegrad", kpiModel.get("default_time_grad"));
            kpiValueJson.put("matakpiId", kpiModel.get("id"));
            kpiValueJson.put("kpiGrpId", kpiModel.get("grp_id"));
            if (!StringUtils.isEmpty(kpiGrpId)) {
                kpiValueJson.put("collect", kpiModel.get("sel"));
            }
            kpiValueJson.put("algorithm", kpiModel.get("kpi_algorithm"));
            kpiValueJson.put("props", kpiValues);
            kpiLists.add(kpiValueJson);
        }
        return kpiLists;
    }

    private List<String> getKpiValuesMap(String regionId, ImmutableMap<String, String> kpiModel, String time) {
        String kpi_table = kpiModel.get("kpi_table");
        StringBuffer kpiValueSql = new StringBuffer();
        String[] kpiValueTypes = kpiModel.get("value_type").split(",");
        String[] kpiFields = kpiModel.get("attr_name").split(",");

        Date maxTime = getKpiMaxDate(regionId, kpiModel.get("default_time_grad"), time, kpi_table);

        if(maxTime==null) return null;

        kpiValueSql.append("SELECT TO_CHAR(STATISTICAL_TIME,'yyyy-mm-dd') STATISTICAL_TIME,");
        for (int i = 0; i < kpiFields.length; i++) {
            if (i >= kpiValueTypes.length) {
                kpiValueSql.append(kpiFields[i]).append(" as ").append(kpiFields[i]);
            } else if (("1").equals(kpiValueTypes[i])) {
                kpiValueSql.append("to_char(").append(kpiFields[i]).append(",'fm9999990.00')  as ").append(kpiFields[i]);
            } else {
                kpiValueSql.append(kpiFields[i]).append(" as ").append(kpiFields[i]);
            }
            if (i < kpiFields.length - 1) {
                kpiValueSql.append(",");
            }
        }
        kpiValueSql.append(" from ").append(kpi_table).append(" WHERE AREA_NUMBER = ? and STATISTICAL_TIME = ? ");

        System.out.print("kpiValueSql:" + kpiValueSql.toString());
        return (List<String>) jdbcTemplate.query(kpiValueSql.toString(), new Object[]{regionId, maxTime}, new ResultSetExtractor() {
            public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                List<String> values = new ArrayList<>();
                while (resultSet.next()) {
                    values.add(resultSet.getString("STATISTICAL_TIME"));

                    for (int i = 0; i < kpiFields.length; i++) {
                        String value = "";
                        if(kpiValueTypes.length > 0 && !"".equals(kpiValueTypes[0])){
                            if("1".equals(kpiValueTypes[i])){
                                DecimalFormat format=new DecimalFormat("0.00");
                                value = format.format(resultSet.getDouble(kpiFields[i]));
                            }else{
                                DecimalFormat format=new DecimalFormat("0");
                                value = format.format(resultSet.getDouble(kpiFields[i]));
                            }
                        }else{
                            DecimalFormat format=new DecimalFormat();
                            value = format.format(resultSet.getDouble(kpiFields[i]));
                        }
                        values.add(value);
                    }
                }
                return values;
            }
        });
    }

    private Date getKpiMaxDate(String regionId, String timeGrad, String time, String kpi_table) {
        String maxTimeSql = "SELECT  max(STATISTICAL_TIME) AS MAX_TIME FROM " + kpi_table + " WHERE AREA_NUMBER = ? and STATISTICAL_TIME <= TO_DATE(?, 'yyyy-MM-dd hh24:mi:ss') ";
        System.out.print("kpiValueSql:" + maxTimeSql);

        String[] timeRange = DateStringUtil.getTimeRange(timeGrad, time, 0).split(";");
        Object obj = jdbcTemplate.query(maxTimeSql, new Object[]{regionId, timeRange[1]}, (resultSet, i) -> ImmutableMap.<String, Object>builder()
                .put("MAX_TIME", (resultSet.getObject("MAX_TIME")==null)?"":resultSet.getObject("MAX_TIME"))
                .build()).get(0).get("MAX_TIME");

        if (obj instanceof Date) return (Date) obj;
        return null;
    }

    private void dealChartColor(Map<String, String> kpiValue, String kpiSearchValue, String kpiThreshold, String kpiValueUnit) {

        if (!("").equals(kpiThreshold)) {
            String[] thresholds = kpiThreshold.split(";");
            for (String threshold : thresholds) {
                String judge_hold = threshold.split(":")[0].replace("x", kpiSearchValue);
                Interpreter bs = new Interpreter();
                try {
                    if (("true").equals(bs.eval(judge_hold).toString())) {
                        String kpi_color = threshold.split(":")[1];
                        String kpi_color_level = "";
                        if (threshold.split(":").length >= 3) {
                            kpi_color_level = threshold.split(":")[2];
                        }
                        kpiValue.put("color", kpi_color);
                        kpiValue.put("value", kpiSearchValue + kpiValueUnit + kpi_color_level);
                        break;
                    } else {
                        kpiValue.put("color", defaultColor);
                        kpiValue.put("value", kpiSearchValue + kpiValueUnit);
                    }
                } catch (EvalError evalError) {
                    evalError.printStackTrace();
                }
            }
        } else {
            kpiValue.put("color", defaultColor);
            kpiValue.put("value", kpiSearchValue + kpiValueUnit);
        }
    }

    private String getDbValue(ResultSet resultSet, String chart_type) throws SQLException {
        return StringUtils.isEmpty(resultSet.getObject(chart_type)) ? "" : resultSet.getString(chart_type);
    }
}
