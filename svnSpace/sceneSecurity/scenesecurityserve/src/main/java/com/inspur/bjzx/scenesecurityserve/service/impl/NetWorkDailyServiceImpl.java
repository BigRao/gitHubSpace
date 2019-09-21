package com.inspur.bjzx.scenesecurityserve.service.impl;

import com.inspur.bjzx.scenesecurityserve.service.NetWorkDailyService;
import com.inspur.bjzx.scenesecurityserve.util.DataUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

@Service
public class NetWorkDailyServiceImpl implements NetWorkDailyService {
    private static final Logger log = Logger.getLogger(NetWorkDailyServiceImpl.class);
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("secondaryNamedParameterJdbcTemplate")
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter minuteFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Map<String, String> PROPORTION_COMPLAINTS;
    private static final Map<String, String> VILLAGE_KPI_2G;
    private static final Map<String, String> VILLAGE_KPI_4G;
    private static final Map<String, String> ABNORMAL_VILLAGE;
    private static final Map<String, String> COLOUR;

    static {
        PROPORTION_COMPLAINTS = new LinkedHashMap<>();
        PROPORTION_COMPLAINTS.put("产品质量", "产品质量");
        PROPORTION_COMPLAINTS.put("服务触点", "服务触点");
        PROPORTION_COMPLAINTS.put("基础服务", "基础服务");
        PROPORTION_COMPLAINTS.put("网络质量", "网络质量");
        PROPORTION_COMPLAINTS.put("业务营销", "业务营销");
        PROPORTION_COMPLAINTS.put("合计", "合计");
        VILLAGE_KPI_4G = new LinkedHashMap<>();
        VILLAGE_KPI_4G.put("C_RRC_CONNSUCC_RATE", "RRC连接建立成功率/%");
        VILLAGE_KPI_4G.put("C_RRC_USERNUM", "RRC用户数/人");
        //VILLAGE_KPI_4G.put("C_VOLTE_DRAPRATE", "VOLTE掉线率");
        VILLAGE_KPI_4G.put("C_VOLTE_CONNRATE", "VOLTE接通率/%");
        VILLAGE_KPI_4G.put("C_VOLTE_NUM", "VOLTE用户数/人");
        VILLAGE_KPI_4G.put("C_ANN_VALUE", "干扰值/dBm");
        //VILLAGE_KPI_4G.put("C_TOP_TRAFFIC_USERNUM", "高流量用户数");
        VILLAGE_KPI_4G.put("C_UP_PRB_USERATE", "上行PRB利用率/%");
        //VILLAGE_KPI_4G.put("C_VIDEO_TRAFFIC", "视频流量");
        //VILLAGE_KPI_4G.put("C_WECHART_TRAFFIC", "微信流量");
        VILLAGE_KPI_4G.put("C_PUSCH_S", "上行PUSCH利用率/%");
        VILLAGE_KPI_4G.put("C_PUSCH_X", "下行PDCCH利用率/%");
        VILLAGE_KPI_4G.put("C_TOTAL_TRAFFIC", "总流量/MB");
        VILLAGE_KPI_4G.put("C_USER_NUM", "总用户数/人");
        VILLAGE_KPI_2G = new LinkedHashMap<>();
        VILLAGE_KPI_2G.put("MX_ERL", "每线话务量/ERL");
        VILLAGE_KPI_2G.put("TCH_ERL", "TCH掉话率/%");
        VILLAGE_KPI_2G.put("WIRELESS_CONN", "无线接通率/%");
        VILLAGE_KPI_2G.put("TCH_YSRATE", "TCH拥塞率(含切换)/%");
        VILLAGE_KPI_2G.put("CMCC_DROPRATE", "CMCC掉话率不含切换/%");
        ABNORMAL_VILLAGE = new HashMap<>();
        ABNORMAL_VILLAGE.put("红", "red");
        ABNORMAL_VILLAGE.put("黄", "yellow");
        ABNORMAL_VILLAGE.put("蓝", "blue");
        COLOUR = new HashMap<>();
        COLOUR.put("red", "红");
        COLOUR.put("yellow", "黄");
        COLOUR.put("blue", "蓝");
    }

    @Override
    public JSONObject getSingleLatitudeKpi(String kpiIds, String time, String neId, boolean isMax) throws Exception {

        String sql = "SELECT INDIC_ID,INDIC_NAME,TABLE_NAME,COLUMN_NAME,TIME_INTERVAL,INDIC_TYPE,INDIC_UNIT,CONDITION,KEY_FIELD,START_TIME,IS_FLOAT,COLUMN_W,COLUMN_M,COLUMN_Y,COLUMN_RC FROM FMS_COMM_INDICATE_ZHCX WHERE INDIC_ID IN (:indic_ids) ORDER BY instr('" + Arrays.asList(kpiIds.split(",")) + "',INDIC_ID)";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("indic_ids", Arrays.asList(kpiIds.split(",")));
        List<Map<String, Object>> column_list = namedParameterJdbcTemplate.queryForList(sql, parameters);
        JSONObject resultObject = new JSONObject();
        JSONArray resultArray = new JSONArray();
        for (Map<String, Object> column : column_list) {

            String kpiId = column.get("INDIC_ID") != null && column.get("INDIC_ID") != "" ? column.get("INDIC_ID").toString().trim() : "";

            Map<String, Object> column_map = new HashMap<>();
            if ("190801012".equals(kpiId)) {
                putProportionComplaints(resultArray, column_map, kpiId);
                continue;
            }
            resultObject.put("neId", neId);
            String table_name = column.get("TABLE_NAME") != null && column.get("TABLE_NAME") != "" ? column.get("TABLE_NAME").toString().trim() : "";
            String column_name = column.get("COLUMN_NAME") != null && column.get("COLUMN_NAME") != "" ? column.get("COLUMN_NAME").toString().trim() : "";
            String start_time = column.get("START_TIME") != null && column.get("START_TIME") != "" ? column.get("START_TIME").toString().trim() : "";
            String condition = column.get("CONDITION") != null && column.get("CONDITION") != "" ? column.get("CONDITION").toString().trim() : "";
            String is_float = column.get("IS_FLOAT") != null && column.get("IS_FLOAT") != "" ? column.get("IS_FLOAT").toString().trim() : "0";
            StringBuilder column_sql = new StringBuilder();
            column_sql.append("select ").append(column_name).append(" AS column_name,to_char(").append(start_time).append(", 'yyyy-mm-dd hh24:mi') AS start_time");
            String column_w = column.get("COLUMN_W") != null && column.get("COLUMN_W") != "" ? column.get("COLUMN_W").toString() : "";
            if (!("").equals(column_w)) {
                column_sql.append(",").append(column_w).append(" AS column_w");
            }
            String column_m = column.get("COLUMN_M") != null && column.get("COLUMN_M") != "" ? column.get("COLUMN_M").toString() : "";
            if (!("").equals(column_m)) {
                column_sql.append(",").append(column_m).append(" AS column_m");
            }
            String column_rc = column.get("COLUMN_RC") != null && column.get("COLUMN_RC") != "" ? column.get("COLUMN_RC").toString() : "";
            if (!("").equals(column_rc)) {
                column_sql.append(",").append(column_rc).append(" AS column_rc");
            }
            column_sql.append(" FROM ").append(table_name);
            Integer interval = DataUtil.getInteger(column, "TIME_INTERVAL");
            LocalDateTime hourTime;
            LocalDateTime hour;
            String firstTime;
            if (isMax) {
                firstTime = getMaxTime(table_name);
            } else {
                hourTime = LocalDateTime.parse(time, minuteFormat).withSecond(0);
                hour = LocalDateTime.now();
                switch (interval) {
                    case 1440:
                        hour = hourTime.withHour(0).withMinute(0);
                        break;
                    case 60:
                        hour = hourTime.withMinute(0);
                        break;
                    case 15:
                        hour = hourTime.withMinute(DataUtil.judge15(hourTime.getMinute()));
                        break;
                    case 5:
                        hour = hourTime.withMinute(DataUtil.judge5(hourTime.getMinute()));
                        break;
                }
                firstTime = hour.format(minuteFormat);

            }
            column_sql.append(" WHERE to_char(").append(start_time).append(", 'yyyy-mm-dd hh24:mi:ss') = '").append(firstTime).append("'");

            if (!("").equals(condition)) {
                column_sql.append("and ").append(condition);
            }

            if ("IPM_WS_15M".equals(table_name) || "V_ZB_ZD_TOTAL_15M".equals(table_name) || "V_ZB_ZD_TOTAL_H".equals(table_name) ||
                    "V_ZB_ZD_TOTAL_15M_Y".equals(table_name) || "V_ZB_ZD_TOTAL_H_Y".equals(table_name)) {
                column_sql.append(" and neNAME = '2019年XW保障'");
            }
            if ("IPM_WS3_15M".equals(table_name)) {
                column_sql.append(" and WS_NAME = '2019年XW保障'");
            }
            if ("V_ZB_ZD_TOTAL_3_H_Y".equals(table_name) || "V_ZB_ZD_TOTAL_3_15M_Y".equals(table_name) || "V_ZB_ZD_TOTAL_15M_Y".equals(table_name)) {
                column_sql.append(" and WA_NAME = '2019年XW保障'");
            }
            if (StringUtils.isNotBlank(neId)) {
                column_sql.append(" and WS3_NAME = '").append(neId).append("'");
            }
            try {
                log.info(kpiId);
                Map<String, Object> result_map = jdbcTemplate.queryForMap(column_sql.toString());
                resultObject.put("time", result_map.get("start_time") != null && result_map.get("start_time") != "" ? result_map.get("start_time").toString() : "");
                Map<String, Object> kpiInfo = new HashMap<>();
                kpiInfo.put("unit", column.get("INDIC_UNIT") != null && column.get("INDIC_UNIT") != "" ? column.get("INDIC_UNIT").toString().trim() : "");
                kpiInfo.put("tendency", column.get("INDIC_TYPE") != null && column.get("INDIC_TYPE") != "" ? column.get("INDIC_TYPE").toString().trim() : "");
                kpiInfo.put("kpiName", column.get("INDIC_NAME") != null && column.get("INDIC_NAME") != "" ? column.get("INDIC_NAME").toString().trim() : "");
                kpiInfo.put("kpiId", kpiId);
                kpiInfo.put("time_interval", column.get("TIME_INTERVAL") != null && column.get("TIME_INTERVAL") != "" ? column.get("TIME_INTERVAL").toString().trim() : "");
                column_map.put("kpiInfo", kpiInfo);
                String kpiValue = result_map.get("column_name") != null && result_map.get("column_name") != "" ? result_map.get("column_name").toString().trim() : "";
                if (kpiValue.contains(".") && !kpiValue.contains("(") && !kpiValue.contains(")")) {
                    BigDecimal bg = new BigDecimal(Double.parseDouble(kpiValue));
                    kpiValue = String.valueOf(bg.setScale(Integer.parseInt(is_float), BigDecimal.ROUND_HALF_UP).doubleValue());
                }
                column_map.put("kpiValue", kpiValue);
                column_map.put("weeksForm", result_map.get("column_w") != null && result_map.get("column_w") != "" ? result_map.get("column_w").toString().trim() : "");
                column_map.put("monthsForm", result_map.get("column_m") != null && result_map.get("column_m") != "" ? result_map.get("column_m").toString().trim() : "");
                column_map.put("yearsForm", "");
                column_map.put("dailyCompare", result_map.get("column_rc") != null && result_map.get("column_rc") != "" ? result_map.get("column_rc").toString().trim() : "");
                resultArray.add(column_map);
            } catch (EmptyResultDataAccessException e) {
                Map<String, Object> kpiInfo = new HashMap<>();
                kpiInfo.put("unit", column.get("INDIC_UNIT") != null && column.get("INDIC_UNIT") != "" ? column.get("INDIC_UNIT").toString().trim() : "");
                kpiInfo.put("tendency", column.get("INDIC_TYPE") != null && column.get("INDIC_TYPE") != "" ? column.get("INDIC_TYPE").toString().trim() : "");
                kpiInfo.put("kpiName", column.get("INDIC_NAME") != null && column.get("INDIC_NAME") != "" ? column.get("INDIC_NAME").toString().trim() : "");
                kpiInfo.put("kpiId", kpiId);
                kpiInfo.put("time_interval", column.get("TIME_INTERVAL") != null && column.get("TIME_INTERVAL") != "" ? column.get("TIME_INTERVAL").toString().trim() : "");
                columnMapPutEmpty(kpiInfo, column_map, resultArray, "");
            }
        }
        resultObject.put("kpis", resultArray);

        return resultObject;
    }

    private void putProportionComplaints(JSONArray resultArray, Map<String, Object> column_map, String kpiId) throws Exception {

        String jKSql = "select nvl(ALARM_TOTAL,0) result \n" +
                "from IPM_USD_BASEINFO_JT_TOTAL \n" +
                "where TYPE_WS=?";
        String yDSql = "select nvl(ALARM_TOTAL,0) result \n" +
                "from IPM_USD_BASEINFO_YD_TOTAL \n" +
                "where TYPE_WS=?";
        putComplaints(resultArray, column_map, kpiId, jKSql);
        putComplaints(resultArray, column_map, kpiId, yDSql);

    }

    private void putComplaints(JSONArray resultArray, Map<String, Object> column_map, String kpiId, String sql) {
        PROPORTION_COMPLAINTS.forEach((kpiName, value) -> {

            Map<String, Object> kpiInfo = new HashMap<>();
            try {
                Map<String, Object> classifyMap = jdbcTemplate.queryForMap(sql, value);
                kpiInfo.put("unit", "");
                kpiInfo.put("tendency", "");
                kpiInfo.put("kpiName", kpiName);
                kpiInfo.put("kpiId", kpiId);
                String proportion = DataUtil.O2S(classifyMap.get("result"));
                columnMapPutEmpty(kpiInfo, column_map, resultArray, proportion);
            } catch (EmptyResultDataAccessException e) {
                kpiInfo.put("unit", "");
                kpiInfo.put("tendency", "");
                kpiInfo.put("kpiName", kpiName);
                kpiInfo.put("kpiId", kpiId);
                columnMapPutEmpty(kpiInfo, column_map, resultArray, "0");
            }

        });
    }

    private void columnMapPutEmpty(Map<String, Object> kpiInfo, Map<String, Object> column_map, JSONArray resultArray, String proportion) {
        column_map.put("kpiInfo", kpiInfo);
        column_map.put("kpiValue", proportion);
        column_map.put("weeksForm", "");
        column_map.put("monthsForm", "");
        column_map.put("yearsForm", "");
        column_map.put("dailyCompare", "");
        resultArray.add(column_map);
    }

    @Override
    public JSONObject getManyTimeKpi(String kpiId, String time, String neId, String time_type, boolean isMax) throws Exception {
        String sql = "SELECT INDIC_ID,INDIC_NAME,TABLE_NAME,COLUMN_NAME,INDIC_TYPE,INDIC_UNIT,KEY_FIELD,START_TIME,IS_FLOAT FROM FMS_COMM_INDICATE_ZHCX WHERE INDIC_ID IN (:indic_ids)";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("indic_ids", Arrays.asList(kpiId.split(",")));
        List<Map<String, Object>> column_list = namedParameterJdbcTemplate.queryForList(sql, parameters);
        JSONObject resultObject = new JSONObject();
        JSONArray resultArray = new JSONArray();
        column_list.forEach((column) -> {
            Map<String, Object> column_map = new HashMap<>();
            resultObject.put("neId", neId);
            String table_name = column.get("TABLE_NAME").toString() != null && column.get("TABLE_NAME") != "" ? column.get("TABLE_NAME").toString() : "";
            String column_name = column.get("COLUMN_NAME").toString() != null && column.get("COLUMN_NAME") != "" ? column.get("COLUMN_NAME").toString() : "";
            String start_time = column.get("START_TIME").toString() != null && column.get("START_TIME") != "" ? column.get("START_TIME").toString() : "";
            String is_float = column.get("IS_FLOAT").toString() != null && column.get("IS_FLOAT") != "" ? column.get("IS_FLOAT").toString() : "0";
            String column_sql = "SELECT " + column_name + " AS column_name,to_char(" + start_time;
            switch (time_type) {
                case "1440"://天
                    String lastTime_day;
                    if (isMax) {
                        lastTime_day = getMaxTime(table_name);
                    } else {
                        lastTime_day = time.split("/")[0];
                    }

                    String day = time.split("/")[1];
                    LocalDate date = LocalDateTime.parse(lastTime_day, minuteFormat).toLocalDate();
                    String firstTime = date.minusDays(Long.parseLong(day)).format(dateFormat);
                    column_sql += ", 'mm-dd') AS start_time FROM " + table_name;
                    column_sql += " WHERE " + start_time + " between TO_DATE('" + firstTime + "', 'yyyy-mm-dd hh24:mi:ss') AND TO_DATE('" + lastTime_day + "', 'yyyy-mm-dd hh24:mi:ss') ";

                    break;
                case "60"://小时
                    String lastTime_hour;
                    if (isMax) {
                        lastTime_hour = getMaxTime(table_name);
                    } else {
                        lastTime_hour = time.split("/")[0];
                    }
                    LocalDateTime hourTime = LocalDateTime.parse(lastTime_hour, minuteFormat);
                    String firstHourTime = hourTime.minusHours(Long.parseLong(time.split("/")[1])).format(minuteFormat);
                    column_sql += ", 'mm-dd hh24') AS start_time FROM " + table_name;
                    column_sql += " WHERE " + start_time + " between TO_DATE('" + firstHourTime + "', 'yyyy-mm-dd hh24:mi:ss') AND TO_DATE('" + lastTime_hour + "', 'yyyy-mm-dd hh24:mi:ss') ";

                    break;
                case "15"://15分钟
                    String lastTime_minutes;
                    if (isMax) {
                        lastTime_minutes = getMaxTime(table_name);
                    } else {
                        lastTime_minutes = time.split("/")[0];
                    }
                    LocalDateTime minuteTime = LocalDateTime.parse(lastTime_minutes, minuteFormat);
                    String firstMinuteTime = minuteTime.minusMinutes(Long.parseLong(time.split("/")[1]) * 15).format(minuteFormat);
                    column_sql += ", 'hh24:mi') AS start_time FROM " + table_name;
                    column_sql += " WHERE " + start_time + " between TO_DATE('" + firstMinuteTime + "', 'yyyy-mm-dd hh24:mi:ss') AND TO_DATE('" + lastTime_minutes + "', 'yyyy-mm-dd hh24:mi:ss') ";

                    break;
            }
            if (StringUtils.isNotBlank(neId)) {
                column_sql += " and WS3_NAME = '" + neId + "'";
            }
            if ("IPM_WS_15M".equals(table_name) || "V_ZB_ZD_TOTAL_15M".equals(table_name) || "V_ZB_ZD_TOTAL_H".equals(table_name) ||
                    "V_ZB_ZD_TOTAL_15M_Y".equals(table_name) || "V_ZB_ZD_TOTAL_H_Y".equals(table_name)) {
                column_sql += " and neNAME = '2019年XW保障'";
            }
            if ("IPM_WS3_15M".equals(table_name)) {
                column_sql += " and WS_NAME = '2019年XW保障'";
            }
            if ("V_ZB_ZD_TOTAL_3_H_Y".equals(table_name) || "V_ZB_ZD_TOTAL_3_15M_Y".equals(table_name) || "V_ZB_ZD_TOTAL_15M_Y".equals(table_name)) {
                column_sql += " and WA_NAME = '2019年XW保障'";
            }
            column_sql += "ORDER BY  start_time";
            List<Map<String, Object>> result_maps = jdbcTemplate.queryForList(column_sql);
            String[] kpiTime = new String[result_maps.size()];
            String[] kpiValue = new String[result_maps.size()];
            Stream.iterate(0, i -> i + 1).limit(result_maps.size()).forEach(i -> {
                kpiTime[i] = result_maps.get(i).get("start_time") != null && result_maps.get(i).get("start_time") != "" ? result_maps.get(i).get("start_time").toString() : "";
                String column_kpiValue = result_maps.get(i).get("column_name") != null && result_maps.get(i).get("column_name") != "" ? result_maps.get(i).get("column_name").toString().trim() : "";
                if (column_kpiValue.contains(".") && !column_kpiValue.contains("(") && !column_kpiValue.contains(")")) {
                    BigDecimal bg = new BigDecimal(Double.parseDouble(column_kpiValue));
                    column_kpiValue = String.valueOf(bg.setScale(Integer.parseInt(is_float), BigDecimal.ROUND_HALF_UP).doubleValue());
                }
                kpiValue[i] = column_kpiValue;
            });
            resultObject.put("time", kpiTime);
            Map<String, Object> kpiInfo = new HashMap<>();
            kpiInfo.put("unit", DataUtil.getString(column, "INDIC_UNIT"));
            kpiInfo.put("tendency", DataUtil.getString(column, "INDIC_TYPE"));
            kpiInfo.put("kpiName", DataUtil.getString(column, "INDIC_NAME"));
            kpiInfo.put("kpiId", DataUtil.getString(column, "INDIC_ID"));
            column_map.put("kpiInfo", kpiInfo);
            column_map.put("kpiValue", kpiValue);
            resultArray.add(column_map);
        });
        resultObject.put("kpis", resultArray);
        return resultObject;
    }

    @Override
    public List<Map<String, Object>> getBrokenStation(String stationType, String time, String neId) throws Exception {
        LocalDateTime hourTime = LocalDateTime.parse(time, minuteFormat);
        String date = hourTime.toLocalDate().format(dateFormat);
        String sql = "select * from(select distinct SHEET_ID \"sheet_id\",STATION_NAME \"station_name\"," +
                "STATION_ID \"station_id\",STATION_TYPE \"station_type\" ,SHEET_CURRENT_DEPARTMENT \"sheet_current_department\" ," +
                "HONEYCOMB_TYPE \"honeycomb_type\" , FGS \"fgs\"," +
                "LONGITUDE \"longitude\", LATITUDE \"latitude\" " +
                "from T_STATION_BREAKEN_THREE t " +
                "where t.region_id= ? " +
                "and t.station_type= ? )";
        return jdbcTemplate.queryForList(sql, neId, stationType);
    }

    @Override
    public Map<String, Object> getAbnormalPlot(String neId, String type) throws Exception {

        try {
            Map<String, Object> data = new HashMap<>();
            switch (type) {
                case "2G":
                    String sql2g = "select count(*) count from cell_alarm_2g t where time = (select max(time) from cell_alarm_2g) and flag = ? and region_id = ?";
                    for (Map.Entry<String, String> entry : ABNORMAL_VILLAGE.entrySet()) {
                        Map<String, Object> map = jdbcTemplate.queryForMap(sql2g, entry.getKey(), neId);
                        data.put(entry.getValue(), map.get("count"));
                    }
                    String num2g = "select count(*) count from cell_alarm_2g t where time = (select max(time) from cell_alarm_2g) and flag <> '无' and region_id = ?";
                    data.put("total", jdbcTemplate.queryForMap(num2g, neId).get("count"));
                    return data;
                case "4G":
                    String sql4g = "select count(*) count from CELL_ALARM_OTHER t where GFH_YJ = ? and region_id = ? and WA_NAME = '2019年XW保障'";
                    for (Map.Entry<String, String> entry : ABNORMAL_VILLAGE.entrySet()) {
                        Map<String, Object> map = jdbcTemplate.queryForMap(sql4g, entry.getKey(), neId);
                        data.put(entry.getValue(), map.get("count"));
                    }
                    String num4g = "select count(*) count from CELL_ALARM_OTHER t where GFH_YJ <> '无' and region_id = ?  and WA_NAME = '2019年XW保障'";
                    data.put("total", jdbcTemplate.queryForMap(num4g, neId).get("count"));
                    return data;
                case "5G":
                    for (Map.Entry<String, String> entry : ABNORMAL_VILLAGE.entrySet()) {
                        data.put(entry.getValue(), "-");
                    }
                    data.put("total", "-");
                    return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> searchVillage(String neId, String type, String cellType) throws Exception {
        String sql = null;
        if ("2G".equals(type)) {
            if ("all".equals(cellType)) {
                sql = "SELECT distinct a.cell_name \"villageName\",a.cell_id \"villageId\" ,'2G' \"villageType\" " +
                        "FROM cell_alarm_2g a where a.region_id = ?";
                return jdbcTemplate.queryForList(sql, neId);
            }

            sql = "SELECT distinct a.cell_name \"villageName\",a.cell_id \"villageId\", '2G' \"villageType\" " +
                    "FROM cell_alarm_2g a where a.region_id = ? and flag= ?";
        }
        if ("4G".equals(type)) {
            if ("all".equals(cellType)) {
                sql = "SELECT distinct cell_name \"villageName\", ci \"villageId\" ,'4G' \"villageType\" " +
                        "FROM CELL_ALARM_OTHER where region_id = ? and WA_NAME = '2019年XW保障'";
                return jdbcTemplate.queryForList(sql, neId);
            }

            sql = "SELECT distinct cell_name \"villageName\", ci \"villageId\" ,'4G' \"villageType\" " +
                    "FROM CELL_ALARM_OTHER where region_id = ? and gfh_yj = ? and WA_NAME = '2019年XW保障'";

        }
        if ("5G".equals(type)) {
            return new ArrayList<>();
        }

        return jdbcTemplate.queryForList(sql, neId, COLOUR.get(cellType));
    }


    @Override
    public List<Map<String, Object>> searchVillageKpi(String regionId, String villageName, String type) throws Exception {
        String sql = null;
        if ("2G".equals(type)) {
            sql = "select * from (select MX_ERL,CMCC_DROPRATE,WIRELESS_CONN,TCH_YSRATE,TCH_ERL " +
                    "from CELL_ALARM_2g where cell_name = ? and region_id = ? order by time desc) where rownum =1";
        }
        if ("4G".equals(type)) {
            sql = "select * from (select C_RRC_CONNSUCC_RATE,C_RRC_USERNUM," +
                    "C_VOLTE_CONNRATE,C_VOLTE_NUM,C_ANN_VALUE," +
                    "C_UP_PRB_USERATE,C_PUSCH_S," +
                    "C_PUSCH_X,C_TOTAL_TRAFFIC,C_USER_NUM " +
                    "from CELL_ALARM_OTHER where cell_name = ? and region_id = ? and WA_NAME = '2019年XW保障' order by time desc) where rownum =1 ";
        }
        try {
            log.info("{villageName==" + villageName + ",regionId==" + regionId + "}");
            Map<String, Object> map = jdbcTemplate.queryForMap(sql, villageName, regionId);
            log.info("map:" + map);
            List<Map<String, Object>> dataList = new ArrayList<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Map<String, Object> data = new HashMap<>();
                if ("2G".equals(type)) {
                    data.put("name", VILLAGE_KPI_2G.get(entry.getKey()).split("/")[0]);
                    data.put("unit", VILLAGE_KPI_2G.get(entry.getKey()).split("/")[1]);
                }
                if ("4G".equals(type)) {
                    data.put("name", VILLAGE_KPI_4G.get(entry.getKey()).split("/")[0]);
                    data.put("unit", VILLAGE_KPI_4G.get(entry.getKey()).split("/")[1]);
                }
                data.put("value", entry.getValue());
                data.put("cloum_name", entry.getKey());
                dataList.add(data);
            }
            log.info("dataList:" + dataList);
            return dataList;
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

    @Override
    public JSONObject manyTimeSearchVillageKpi(String cloumnName, String regionId, String villageName, String time, String cellType) throws Exception {
        JSONObject resultObject = new JSONObject();
        JSONArray resultArray = new JSONArray();
        Map<String, Object> column_map = new HashMap<>();
        String lastTime_minutes = time.split("/")[0];
        if ("2G".equals(cellType)){
            lastTime_minutes = getMaxTime("CELL_ALARM_2g");
        }else if ("4G".equals(cellType)){
            lastTime_minutes = getMaxTime("CELL_ALARM_OTHER");
        }
        LocalDateTime minuteTime = LocalDateTime.parse(lastTime_minutes, minuteFormat);
        String firstMinuteTime = null;
        String sql = null;
        if ("C_ANN_VALUE".equals(cloumnName)) {
            return resultObject;
        }
        String kpiName = "";
        switch (cellType) {
            case "2G":
                kpiName = VILLAGE_KPI_2G.get(cloumnName).split("/")[0];
                firstMinuteTime = minuteTime.minusHours(Long.parseLong(time.split("/")[1])).format(minuteFormat);
                sql = "select " + cloumnName + ",to_char(time,'mm-dd hh24') as TIME ";
                sql += "from CELL_ALARM_2g where cell_name = ? and region_id = ? " +
                        "and time between to_date(?,'yyyy-mm-dd hh24:mi:ss') and to_date(?,'yyyy-mm-dd hh24:mi:ss') order by time";
                break;
            case "4G":
                kpiName = VILLAGE_KPI_4G.get(cloumnName).split("/")[0];
                firstMinuteTime = minuteTime.minusMinutes(Long.parseLong(time.split("/")[1])).format(minuteFormat);
                sql = "select " + cloumnName + ",to_char(time,'hh24:mi') as TIME ";
                sql += "from CELL_ALARM_OTHER where cell_name = ? and region_id = ? and WA_NAME = '2019年XW保障' " +
                        "and time between to_date(?,'yyyy-mm-dd hh24:mi:ss') and to_date(?,'yyyy-mm-dd hh24:mi:ss')order by time";
                break;
            case "5G":
                return new JSONObject();
        }
        log.info("{villageName:" + villageName + ",regionId:" + regionId + ",firstMinuteTime:" + firstMinuteTime + ",lastTime_minutes:" + lastTime_minutes + "}");
        List<Map<String, Object>> result_maps = jdbcTemplate.queryForList(sql, villageName, regionId, firstMinuteTime, lastTime_minutes);
        String[] kpiTime = new String[result_maps.size()];
        String[] kpiValue = new String[result_maps.size()];
        log.info(result_maps);
        Stream.iterate(0, i -> i + 1).limit(result_maps.size()).forEach(i -> {
            String kTime = result_maps.get(i).get("TIME") != null && result_maps.get(i).get("TIME") != "" ? result_maps.get(i).get("TIME").toString() : "";
            if (kTime.contains(".")) {
                int i1 = kTime.lastIndexOf(".");
                kTime = kTime.substring(0, i1);
            }
            kpiTime[i] = kTime;
            String column_kpiValue = result_maps.get(i).get(cloumnName) != null && result_maps.get(i).get(cloumnName) != "" ? result_maps.get(i).get(cloumnName).toString().trim() : "";
            kpiValue[i] = column_kpiValue;
        });

        resultObject.put("time", kpiTime);
        Map<String, Object> kpiInfo = new HashMap<>();
        kpiInfo.put("unit", "");
        kpiInfo.put("tendency", "");
        kpiInfo.put("kpiName", kpiName);
        kpiInfo.put("kpiId", cloumnName);
        column_map.put("kpiInfo", kpiInfo);
        column_map.put("kpiValue", kpiValue);
        resultArray.add(column_map);
        resultObject.put("kpis", resultArray);
        return resultObject;
    }

    private String getMaxTime(String tableName) {
        String maxTimeSql = "select to_char(Max(time),'yyyy-mm-dd hh24:mi:ss') \"maxTime\" from " + tableName;
        return String.valueOf(jdbcTemplate.queryForMap(maxTimeSql).get("maxTime"));
    }

}
