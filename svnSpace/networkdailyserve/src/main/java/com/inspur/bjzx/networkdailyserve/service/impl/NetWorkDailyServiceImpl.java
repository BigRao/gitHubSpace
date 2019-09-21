package com.inspur.bjzx.networkdailyserve.service.impl;

import com.inspur.bjzx.networkdailyserve.service.NetWorkDailyService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class NetWorkDailyServiceImpl implements NetWorkDailyService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public JSONObject getSingleLatitudeKpi(String kpiId, String time, String neId) {
        String sql = "SELECT INDIC_ID,INDIC_NAME,TABLE_NAME,COLUMN_NAME,INDIC_TYPE,INDIC_UNIT,KEY_FIELD,START_TIME,IS_FLOAT,COLUMN_FZSD,COLUMN_ZHB,COLUMN_YHB,COLUMN_NTB,COLUMN_NLJ,INDIC_DEFINE FROM FMS_COMM_INDICATE_ZHCX WHERE INDIC_ID IN (:indic_ids)";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("indic_ids", Arrays.asList(kpiId.split(",")));
        List<Map<String, Object>> column_list = namedParameterJdbcTemplate.queryForList(sql, parameters);
        JSONObject resultObject = new JSONObject();
        JSONArray resultArray = new JSONArray();
        column_list.forEach((column) -> {
            Map<String, Object> column_map = new HashMap<>();
            resultObject.put("neId", neId);
            String table_name = column.get("TABLE_NAME") != null && column.get("TABLE_NAME") != "" ? column.get("TABLE_NAME").toString() : "";
            String column_name = column.get("COLUMN_NAME") != null && column.get("COLUMN_NAME") != "" ? column.get("COLUMN_NAME").toString() : "";
            String key_file = column.get("KEY_FIELD") != null && column.get("KEY_FIELD") != "" ? column.get("KEY_FIELD").toString() : "";
            String start_time = column.get("START_TIME") != null && column.get("START_TIME") != "" ? column.get("START_TIME").toString() : "";
            String is_float = column.get("IS_FLOAT") != null && column.get("IS_FLOAT") != "" ? column.get("IS_FLOAT").toString() : "0";
            String column_sql = "SELECT " + column_name + " AS column_name,to_char(" + start_time + ", 'yyyy-mm-dd') AS start_time";
            String column_fzsd = column.get("COLUMN_FZSD") != null && column.get("COLUMN_FZSD") != "" ? column.get("COLUMN_FZSD").toString() : "";
            if(!("").equals(column_fzsd)){
                column_sql += "," + column_fzsd + " AS column_fzsd";
            }
            String column_zhb = column.get("COLUMN_ZHB") != null && column.get("COLUMN_ZHB") != "" ? column.get("COLUMN_ZHB").toString() : "";
            if(!("").equals(column_zhb)){
                column_sql +=  "," + column_zhb + " AS column_zhb";
            }
            String column_yhb = column.get("COLUMN_YHB") != null && column.get("COLUMN_YHB") != "" ? column.get("COLUMN_YHB").toString() : "";
            if(!("").equals(column_yhb)){
                column_sql +=  "," + column_yhb + " AS column_yhb";
            }
            String column_ntb = column.get("COLUMN_NTB") != null && column.get("COLUMN_NTB") != "" ? column.get("COLUMN_NTB").toString() : "";
            if(!("").equals(column_ntb)){
                column_sql +=  "," + column_ntb + " AS column_ntb";
            }
            String column_nlj = column.get("COLUMN_NLJ") != null && column.get("COLUMN_NLJ") != "" ? column.get("COLUMN_NLJ").toString() : "";
            if(!("").equals(column_nlj)){
                column_sql +=  "," + column_nlj + " AS column_nlj";
            }
            column_sql +=  " FROM " + table_name;
            if (time.equals("max")) {
                column_sql += " WHERE " + start_time + " = (SELECT MAX(" + start_time + ") FROM " + table_name + " )";
            } else {
                column_sql += " WHERE " + start_time + " = TO_DATE('" + time + "', 'yyyy-mm-dd')";
            }
            try {
                Map<String, Object> result_map = jdbcTemplate.queryForMap(column_sql);
                resultObject.put("time", result_map.get("start_time") != null && result_map.get("start_time") != "" ? result_map.get("start_time").toString() : "");
                Map<String, Object> kpiInfo = new HashMap<>();
                kpiInfo.put("unit", column.get("INDIC_UNIT") != null && column.get("INDIC_UNIT") != "" ? column.get("INDIC_UNIT").toString().trim() : "");
                kpiInfo.put("tendency", column.get("INDIC_TYPE") != null && column.get("INDIC_TYPE") != "" ? column.get("INDIC_TYPE").toString().trim() : "");
                kpiInfo.put("kpiName", column.get("INDIC_NAME") != null && column.get("INDIC_NAME") != "" ? column.get("INDIC_NAME").toString().trim() : "");
                kpiInfo.put("kpiId", column.get("INDIC_ID") != null && column.get("INDIC_ID") != "" ? column.get("INDIC_ID").toString().trim() : "");
                kpiInfo.put("instructions", column.get("INDIC_DEFINE") != null && column.get("INDIC_DEFINE") != "" ? column.get("INDIC_DEFINE").toString().trim() : "");
                column_map.put("kpiInfo", kpiInfo);
                String kpiValue = result_map.get("column_name") != null && result_map.get("column_name") != "" ? result_map.get("column_name").toString().trim() : "";
                String regEx="[`~!@#$%^&*+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(kpiValue);
                kpiValue = m.replaceAll("").trim();
                System.out.print("kpiValue：" + kpiValue);
                if(kpiValue.contains(".") && !kpiValue.contains("(") && !kpiValue.contains(")")){
                    BigDecimal bg = new BigDecimal(Double.parseDouble(kpiValue));
                    kpiValue = String.valueOf(bg.setScale(Integer.parseInt(is_float), BigDecimal.ROUND_HALF_UP).doubleValue());
                }
                column_map.put("kpiValue", kpiValue);
                column_map.put("weeksForm", result_map.get("column_zhb") != null && result_map.get("column_zhb") != "" ? result_map.get("column_zhb").toString().trim() : "");
                column_map.put("monthsForm", result_map.get("column_yhb") != null && result_map.get("column_yhb") != "" ? result_map.get("column_yhb").toString().trim() : "");
                column_map.put("yearsForm", result_map.get("column_ntb") != null && result_map.get("column_ntb") != "" ? result_map.get("column_ntb").toString().trim() : "");
                column_map.put("peakTime", result_map.get("column_fzsd") != null && result_map.get("column_fzsd") != "" ? result_map.get("column_fzsd").toString().trim() : "");
                column_map.put("yearAccumulated", result_map.get("column_nlj") != null && result_map.get("column_nlj") != "" ? result_map.get("column_nlj").toString().trim() : "");
                resultArray.add(column_map);
            }catch (EmptyResultDataAccessException e){
                Map<String, Object> kpiInfo = new HashMap<>();
                kpiInfo.put("unit", column.get("INDIC_UNIT") != null && column.get("INDIC_UNIT") != "" ? column.get("INDIC_UNIT").toString().trim() : "");
                kpiInfo.put("tendency", column.get("INDIC_TYPE") != null && column.get("INDIC_TYPE") != "" ? column.get("INDIC_TYPE").toString().trim() : "");
                kpiInfo.put("kpiName", column.get("INDIC_NAME") != null && column.get("INDIC_NAME") != "" ? column.get("INDIC_NAME").toString().trim() : "");
                kpiInfo.put("kpiId", column.get("INDIC_ID") != null && column.get("INDIC_ID") != "" ? column.get("INDIC_ID").toString().trim() : "");
                kpiInfo.put("instructions", column.get("INDIC_DEFINE") != null && column.get("INDIC_DEFINE") != "" ? column.get("INDIC_DEFINE").toString().trim() : "");
                column_map.put("kpiInfo", kpiInfo);
                column_map.put("kpiValue", "");
                column_map.put("weeksForm", "");
                column_map.put("monthsForm", "");
                column_map.put("yearsForm", "");
                column_map.put("peakTime", "");
                column_map.put("yearAccumulated", "");
                resultArray.add(column_map);
            }
        });
        resultObject.put("kpis", resultArray);
        return resultObject;
    }

    @Override
    public JSONObject getManyTimeKpi(String kpiId, String time, String neId, String time_type) {
        String sql = "SELECT INDIC_ID,INDIC_NAME,TABLE_NAME,COLUMN_NAME,INDIC_TYPE,INDIC_UNIT,KEY_FIELD,START_TIME,IS_FLOAT FROM FMS_COMM_INDICATE_ZHCX WHERE INDIC_ID IN (:indic_ids)";
        Map<String, Object> parameters = new HashMap<String, Object>();
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
            String column_sql = "SELECT " + column_name + " AS column_name,to_char(" + start_time + ", 'mm-dd') AS start_time FROM " + table_name;
            switch (time_type) {
                case "day":
                    String lastTime_day = time.split("/")[0];
                    String day = time.split("/")[1];
                    if (time.contains("max")) {
                        column_sql += " WHERE " + start_time + " <= (SELECT MAX(" + start_time + ") FROM " + table_name + " ) AND " + start_time + " > (SELECT MAX(" + start_time + ") - " + day + " FROM " + table_name + " ) ORDER BY " + start_time;
                    } else {
                        LocalDate now = LocalDate.now();
                        String firstTime = now.minusDays(Long.parseLong(day)).format(dateFormat);
                        column_sql += " WHERE " + start_time + " <= TO_DATE('" + lastTime_day + "', 'yyyy-mm-dd') AND " + start_time + " > TO_DATE('" + firstTime + "', 'yyyy-mm-dd') ORDER BY " + start_time;
                    }
                    break;
                case "hour":
                    String lastTime_hour = time.split("/")[0];
                    if (time.contains("max")) {
                        String hours = time.split("/")[1] + "/24";
                        column_sql += " WHERE " + start_time + " <= (SELECT MAX(" + start_time + ") FROM " + table_name + " ) AND " + start_time + " > (SELECT MAX(" + start_time + ") - " + hours + " FROM " + table_name + " ) ORDER BY " + start_time;
                    } else {
                        LocalDateTime nowTime = LocalDateTime.now();
                        String firstTime = nowTime.minusHours(Long.parseLong(time.split("/")[1])).format(dateFormat);
                        column_sql += " WHERE " + start_time + " <= TO_DATE('" + lastTime_hour + "', 'yyyy-mm-dd') AND " + start_time + " > TO_DATE('" + firstTime + "', 'yyyy-mm-dd') ORDER BY " + start_time;
                    }
                    break;
                case "minutes":
                    String lastTime_minutes = time.split("/")[0];
                    if (time.contains("max")) {
                        String minutes = time.split("/")[1] + "/24/60";
                        column_sql += " WHERE " + start_time + " <= (SELECT MAX(" + start_time + ") FROM " + table_name + " ) AND " + start_time + " > (SELECT MAX(" + start_time + ") - " + minutes + " FROM " + table_name + " ) ORDER BY " + start_time;
                    } else {
                        LocalDateTime nowTime = LocalDateTime.now();
                        String firstTime = nowTime.minusMinutes(Long.parseLong(time.split("/")[1])).format(dateFormat);
                        column_sql += " WHERE " + start_time + " <= TO_DATE('" + lastTime_minutes + "', 'yyyy-mm-dd') AND " + start_time + " > TO_DATE('" + firstTime + "', 'yyyy-mm-dd') ORDER BY " + start_time;
                    }
                    break;
            }
            List<Map<String, Object>> result_maps = jdbcTemplate.queryForList(column_sql);
            String[] kpiTime = new String[result_maps.size()];
            String[] kpiValue = new String[result_maps.size()];
            Stream.iterate(0, i -> i + 1).limit(result_maps.size()).forEach(i -> {
                kpiTime[i] = result_maps.get(i).get("start_time") != null && result_maps.get(i).get("start_time") != "" ? result_maps.get(i).get("start_time").toString() : "";
                String column_kpiValue = result_maps.get(i).get("column_name") != null && result_maps.get(i).get("column_name") != "" ? result_maps.get(i).get("column_name").toString().trim() : "";
                if(column_kpiValue.contains(".") && !column_kpiValue.contains("(") && !column_kpiValue.contains(")")){
                    BigDecimal bg = new BigDecimal(Double.parseDouble(column_kpiValue));
                    column_kpiValue = String.valueOf(bg.setScale(Integer.parseInt(is_float), BigDecimal.ROUND_HALF_UP).doubleValue());
                }
                kpiValue[i] = column_kpiValue;
            });
            resultObject.put("time", kpiTime);
            Map<String, Object> kpiInfo = new HashMap<>();
            kpiInfo.put("unit", column.get("INDIC_UNIT") != null && column.get("INDIC_UNIT") != "" ? column.get("INDIC_UNIT").toString() : "");
            kpiInfo.put("tendency", column.get("INDIC_TYPE") != null && column.get("INDIC_TYPE") != "" ? column.get("INDIC_TYPE").toString() : "");
            kpiInfo.put("kpiName", column.get("INDIC_NAME") != null && column.get("INDIC_NAME") != "" ? column.get("INDIC_NAME").toString() : "");
            kpiInfo.put("kpiId", column.get("INDIC_ID") != null && column.get("INDIC_ID") != "" ? column.get("INDIC_ID").toString() : "");
            column_map.put("kpiInfo", kpiInfo);
            column_map.put("kpiValue", kpiValue);
            resultArray.add(column_map);
        });
        resultObject.put("kpis", resultArray);
        return resultObject;
    }

    @Override
    public JSONObject getManyLatitudeKpi(String kpiId, String time, String neId, String sort) {
        String sql = "SELECT INDIC_ID,INDIC_NAME,TABLE_NAME,COLUMN_NAME,INDIC_TYPE,INDIC_UNIT,KEY_FIELD,START_TIME,IS_FLOAT FROM FMS_COMM_INDICATE_ZHCX WHERE INDIC_ID IN (:indic_ids)";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("indic_ids", Arrays.asList(kpiId.split(",")));
        List<Map<String, Object>> column_list = namedParameterJdbcTemplate.queryForList(sql, parameters);
        JSONObject resultObject = new JSONObject();
        JSONArray resultArray = new JSONArray();
        column_list.forEach((column) -> {
            Map<String, Object> column_map = new HashMap<>();
            column_map.put("neId", neId);
            String table_name = column.get("TABLE_NAME").toString() != null && column.get("TABLE_NAME") != "" ? column.get("TABLE_NAME").toString() : "";
            String column_name = column.get("COLUMN_NAME").toString() != null && column.get("COLUMN_NAME") != "" ? column.get("COLUMN_NAME").toString() : "";
            String key_file = column.get("KEY_FIELD").toString() != null && column.get("KEY_FIELD") != "" ? column.get("KEY_FIELD").toString() : "";
            String start_time = column.get("START_TIME").toString() != null && column.get("START_TIME") != "" ? column.get("START_TIME").toString() : "";
            String column_sql = "SELECT " + column_name + " AS column_name, t1.区域 AS area, to_char(" + start_time + ", 'yyyy-mm-dd') AS start_time FROM " + table_name;
            if (time.equals("max")) {
                column_sql += " WHERE " + start_time + " = (SELECT MAX(" + start_time + ") FROM " + table_name + " )";
            } else {
                column_sql += " WHERE " + start_time + " = TO_DATE('" + time + "', 'yyyy-mm-dd')";
            }
            List<Map<String, Object>> result_maps = jdbcTemplate.queryForList(column_sql);
            String[] neNames = new String[result_maps.size()];
            String[] kpiValue = new String[result_maps.size()];
            /*if(sort.equals("desc")){
                result_maps.sort(Comparator.comparing(result_map -> result_map.get("column_name").toString()));
            }*/
            Stream.iterate(0, i -> i + 1).limit(result_maps.size()).forEach(i -> {
                neNames[i] = result_maps.get(i).get("area")  != null && result_maps.get(i).get("area") != "" ? result_maps.get(i).get("area").toString() : "";
                kpiValue[i] = result_maps.get(i).get("column_name")  != null && result_maps.get(i).get("column_name") != "" ? result_maps.get(i).get("column_name").toString().trim() : "";
            });

            resultObject.put("neName", neNames);
            resultObject.put("time", start_time);
            Map<String, Object> kpiInfo = new HashMap<>();
            kpiInfo.put("unit", column.get("INDIC_UNIT") != null && column.get("INDIC_UNIT") != "" ? column.get("INDIC_UNIT").toString() : "");
            kpiInfo.put("tendency", column.get("INDIC_TYPE") != null && column.get("INDIC_TYPE") != "" ? column.get("INDIC_TYPE").toString() : "");
            kpiInfo.put("kpiName", column.get("INDIC_NAME") != null && column.get("INDIC_NAME") != "" ? column.get("INDIC_NAME").toString() : "");
            kpiInfo.put("kpiId", column.get("INDIC_ID") != null && column.get("INDIC_ID") != "" ? column.get("INDIC_ID").toString() : "");
            column_map.put("kpiInfo", kpiInfo);
            column_map.put("kpiValue", kpiValue);
            resultArray.add(column_map);
        });
        resultObject.put("kpis", resultArray);
        return resultObject;
    }

    @Override
    public JSONObject getTopKpi(String kpiId, String time, String neId) {
        String sql = "SELECT INDIC_ID,INDIC_NAME,TABLE_NAME,COLUMN_NAME,INDIC_TYPE,INDIC_UNIT,KEY_FIELD,START_TIME,IS_FLOAT FROM FMS_COMM_INDICATE_ZHCX WHERE INDIC_ID IN (:indic_ids)";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("indic_ids", Arrays.asList(kpiId.split(",")));
        List<Map<String, Object>> column_list = namedParameterJdbcTemplate.queryForList(sql, parameters);
        JSONObject resultObject = new JSONObject();
        JSONArray resultArray = new JSONArray();
        column_list.forEach((column) -> {
            Map<String, Object> column_map = new HashMap<>();
            String table_name = column.get("TABLE_NAME").toString() != null && column.get("TABLE_NAME") != "" ? column.get("TABLE_NAME").toString() : "";
            String start_time = column.get("START_TIME").toString() != null && column.get("START_TIME") != "" ? column.get("START_TIME").toString() : "";
            String column_top = column.get("COLUMN_NAME").toString() != null && column.get("COLUMN_NAME") != "" ? column.get("COLUMN_NAME").toString() : "";
            String column_sql = "SELECT " + column_top + " AS column_top, to_char(" + start_time + ", 'yyyy-mm-dd') AS start_time FROM " + table_name;
            if (time.equals("max")) {
                column_sql += " WHERE " + start_time + " = (SELECT MAX(" + start_time + ") FROM " + table_name + " )";
            } else {
                column_sql += " WHERE " + start_time + " = TO_DATE('" + time + "', 'yyyy-mm-dd')";
            }
            try {
                Map<String, Object> result_map = jdbcTemplate.queryForMap(column_sql);
                List<String> tops = Stream.of(result_map.get("column_top").toString().split(","))
                        .map (elem -> new String(elem))
                        .collect(Collectors.toList());
                String[] neNames = new String[tops.size()];
                String[] kpiValue = new String[tops.size()];
                Stream.iterate(0, i -> i + 1).limit(tops.size()).forEach(i -> {
                    neNames[i] = tops.get(i).split("-")[0];
                    kpiValue[i] = tops.get(i).split("-")[1];
                });
                resultObject.put("neName", neNames);
                resultObject.put("time", result_map.get("start_time") != null && result_map.get("start_time") != "" ? result_map.get("start_time").toString() : "");
                Map<String, Object> kpiInfo = new HashMap<>();
                kpiInfo.put("unit", column.get("INDIC_UNIT") != null && column.get("INDIC_UNIT") != "" ? column.get("INDIC_UNIT").toString() : "");
                kpiInfo.put("tendency", column.get("INDIC_TYPE") != null && column.get("INDIC_TYPE") != "" ? column.get("INDIC_TYPE").toString() : "");
                kpiInfo.put("kpiName", column.get("INDIC_NAME") != null && column.get("INDIC_NAME") != "" ? column.get("INDIC_NAME").toString() : "");
                kpiInfo.put("kpiId", column.get("INDIC_ID") != null && column.get("INDIC_ID") != "" ? column.get("INDIC_ID").toString() : "");
                column_map.put("kpiInfo", kpiInfo);
                column_map.put("kpiValue", kpiValue);
                resultArray.add(column_map);
            }catch (EmptyResultDataAccessException ignored){
                resultObject.put("neName", new String[0]);
                resultObject.put("time", "");
                Map<String, Object> kpiInfo = new HashMap<>();
                kpiInfo.put("unit", column.get("INDIC_UNIT") != null && column.get("INDIC_UNIT") != "" ? column.get("INDIC_UNIT").toString() : "");
                kpiInfo.put("tendency", column.get("INDIC_TYPE") != null && column.get("INDIC_TYPE") != "" ? column.get("INDIC_TYPE").toString() : "");
                kpiInfo.put("kpiName", column.get("INDIC_NAME") != null && column.get("INDIC_NAME") != "" ? column.get("INDIC_NAME").toString() : "");
                kpiInfo.put("kpiId", column.get("INDIC_ID") != null && column.get("INDIC_ID") != "" ? column.get("INDIC_ID").toString() : "");
                column_map.put("kpiInfo", kpiInfo);
                column_map.put("kpiValue", new String[0]);
                resultArray.add(column_map);
            }
        });
        resultObject.put("kpis", resultArray);
        return resultObject;
    }

    @Override
    public String[] getCollection(String time, String neId, String userAccount) {
        String sql = "SELECT * FROM T_NETWORKDAILY_COLLECTION WHERE USERACCOUNT = ?";
        try {
            Map<String, Object> map = jdbcTemplate.queryForMap(sql, userAccount);
            return map.get("kpis").toString().split(",");
        }catch (EmptyResultDataAccessException e){
            return new String[0];
        }
    }

    @Override
    public String saveOrCancelCollection(String kpiId, String neId, String userAccount) {
        String getCollectSql = "SELECT * FROM T_NETWORKDAILY_COLLECTION WHERE USERACCOUNT = ?";
        String sql;
        String kpis = null;
        int isSuccess = 0;
        String saveOrCancel = "save";
        try{
            Map<String, Object> collection_map = jdbcTemplate.queryForMap(getCollectSql, userAccount);
            List<String> kpi_stream = Stream.of(collection_map.get("KPIS").toString().split(","))
                    .map (elem -> new String(elem))
                    .collect(Collectors.toList());
            int kpi_first = kpi_stream.size();
            kpi_stream.removeIf(kpi -> kpi.equals(kpiId));
            int kpi_last = kpi_stream.size();
            if(kpi_first != kpi_last){
                saveOrCancel = "cancel";
                if(kpi_stream.size() > 0){
                    kpis = String.join(",", kpi_stream);
                }else {
                    sql = "DELETE FROM T_NETWORKDAILY_COLLECTION WHERE USERACCOUNT = ?";
                    isSuccess = jdbcTemplate.update(sql, userAccount);
                    return isSuccess > 0 ? "取消收藏成功" : "取消收藏失败";
                }
            }else {
                kpis = collection_map.get("KPIS").toString() + "," + kpiId;
            }
            sql = "UPDATE T_NETWORKDAILY_COLLECTION SET KPIS = ? WHERE USERACCOUNT = ?";
        }catch (EmptyResultDataAccessException e){
            kpis = kpiId;
            sql = "INSERT INTO T_NETWORKDAILY_COLLECTION(KPIS, USERACCOUNT) VALUES (?, ?)";
        }
        isSuccess = jdbcTemplate.update(sql, kpis, userAccount);
        if(saveOrCancel.equals("cancel")){
            return isSuccess > 0 ? "取消收藏成功" : "取消收藏失败";
        }else {
            return isSuccess > 0 ? "收藏成功" : "收藏失败";
        }
    }
}
