package com.inspur.bjzx.mbh.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inspur.bjzx.mbh.dao.CommIndicateZhcxDao;
import com.inspur.bjzx.mbh.service.NetWorkDailyService;
import com.inspur.bjzx.mbh.utils.DataUtil;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

@Service
public class NetWorkDailyServiceImpl implements NetWorkDailyService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter minuteFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DecimalFormat df2 = new DecimalFormat("0.00%");//设置double格式1
    @Resource
    private CommIndicateZhcxDao commIndicateZhcxDao;
    @Override
    public JSONObject getSingleLatitudeKpi(String kpiIds, String time) {

        List<String> list = Arrays.asList(kpiIds.split(","));
        log.info("ids:{}",list);
        List<Map<String, Object>> listById = commIndicateZhcxDao.getListById(list);
        JSONObject resultObject = new JSONObject();
        JSONArray resultArray = new JSONArray();
        for (Map<String, Object> column : listById) {

            String kpiId = column.get("INDIC_ID") != null && column.get("INDIC_ID") != "" ? column.get("INDIC_ID").toString().trim() : "";

            Map<String, Object> column_map = new HashMap<>();

            String table_name = column.get("TABLE_NAME") != null && column.get("TABLE_NAME") != "" ? column.get("TABLE_NAME").toString().trim() : "";
            String column_name = column.get("COLUMN_NAME") != null && column.get("COLUMN_NAME") != "" ? column.get("COLUMN_NAME").toString().trim() : "";
            String is_float = column.get("IS_FLOAT") != null && column.get("IS_FLOAT") != "" ? column.get("IS_FLOAT").toString().trim() : "0";
            Integer interval = DataUtil.getInteger(column, "TIME_INTERVAL");
            LocalDateTime hourTime = LocalDateTime.parse(time, minuteFormat).withSecond(0);
            LocalDateTime hour = LocalDateTime.now();
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
            }
            String firstTime = hour.format(minuteFormat);

            try {
                log.info(kpiId);
                Map<String, Object> result_map = commIndicateZhcxDao.getByKpiId(column_name,table_name,firstTime);
                resultObject.put("time", firstTime);
                Map<String, Object> kpiInfo = new HashMap<>();
                kpiInfo.put("unit", DataUtil.getString(column,"INDIC_UNIT"));
                kpiInfo.put("tendency",DataUtil.getString(column,"INDIC_TYPE"));
                kpiInfo.put("kpiName",DataUtil.getString(column,"INDIC_NAME"));
                kpiInfo.put("kpiId", kpiId);
                kpiInfo.put("time_interval",DataUtil.getString(column,"TIME_INTERVAL"));
                column_map.put("kpiInfo", kpiInfo);
                String kpiValue =DataUtil.getString(result_map,"column_name");
                //if (kpiValue.contains(".")) {
                //    kpiValue = df2.format(Double.parseDouble(kpiValue));//当前指标数值百分制
                //}
                column_map.put("kpiValue", kpiValue);
                column_map.put("weeksForm", "");
                column_map.put("monthsForm", "");
                column_map.put("yearsForm", "");
                column_map.put("dailyCompare", "");
                resultArray.add(column_map);
            } catch (EmptyResultDataAccessException e) {
                Map<String, Object> kpiInfo = new HashMap<>();
                kpiInfo.put("unit", column.get("INDIC_UNIT") != null && column.get("INDIC_UNIT") != "" ? column.get("INDIC_UNIT").toString().trim() : "");
                kpiInfo.put("tendency", column.get("INDIC_TYPE") != null && column.get("INDIC_TYPE") != "" ? column.get("INDIC_TYPE").toString().trim() : "");
                kpiInfo.put("kpiName", column.get("INDIC_NAME") != null && column.get("INDIC_NAME") != "" ? column.get("INDIC_NAME").toString().trim() : "");
                kpiInfo.put("kpiId", kpiId);
                kpiInfo.put("time_interval", column.get("TIME_INTERVAL") != null && column.get("TIME_INTERVAL") != "" ? column.get("TIME_INTERVAL").toString().trim() : "");
                columnMapPutEmpty(kpiInfo, column_map, resultArray);
            }
        }
        resultObject.put("kpis", resultArray);

        return resultObject;
    }

    private void columnMapPutEmpty(Map<String, Object> kpiInfo, Map<String, Object> column_map, JSONArray resultArray) {
        column_map.put("kpiInfo", kpiInfo);
        column_map.put("kpiValue", "");
        column_map.put("weeksForm", "");
        column_map.put("monthsForm", "");
        column_map.put("yearsForm", "");
        column_map.put("dailyCompare", "");
        resultArray.add(column_map);
    }

    @Override
    public JSONObject getRankKpi(String kpiIds, String time, String neName) {

        List<String> list = Arrays.asList(kpiIds.split(","));
        List<Map<String, Object>> listById = commIndicateZhcxDao.getListById(list);
        JSONObject resultObject = new JSONObject();
        List<String> neNameList = new ArrayList<>();
        List<String> kpiList = new ArrayList<>();
        for (Map<String, Object> column : listById) {
            String kpiId = column.get("INDIC_ID") != null && column.get("INDIC_ID") != "" ? column.get("INDIC_ID").toString().trim() : "";
            String table_name = column.get("TABLE_NAME") != null && column.get("TABLE_NAME") != "" ? column.get("TABLE_NAME").toString().trim() : "";
            String column_name = column.get("COLUMN_NAME") != null && column.get("COLUMN_NAME") != "" ? column.get("COLUMN_NAME").toString().trim() : "";
            String is_float = column.get("IS_FLOAT") != null && column.get("IS_FLOAT") != "" ? column.get("IS_FLOAT").toString().trim() : "0";
            Integer interval = DataUtil.getInteger(column, "TIME_INTERVAL");
            LocalDateTime hourTime = LocalDateTime.parse(time, minuteFormat).withSecond(0);
            LocalDateTime hour = LocalDateTime.now();
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
            }
            String firstTime = hour.format(minuteFormat);

            try {
                log.info("kpiId:{}",kpiId);
                log.info("column_name:{}",column_name);
                log.info("table_name:{}",table_name);
                log.info("firstTime:{}",firstTime);
                List<Map<String, Object>> result_List = commIndicateZhcxDao.getRankByKpiId(column_name,table_name,firstTime);
                resultObject.put("time", firstTime);
                resultObject.put("unit", column.get("INDIC_UNIT") != null && column.get("INDIC_UNIT") != "" ? column.get("INDIC_UNIT").toString().trim() : "");
                resultObject.put("tendency", column.get("INDIC_TYPE") != null && column.get("INDIC_TYPE") != "" ? column.get("INDIC_TYPE").toString().trim() : "");
                resultObject.put("kpiName", column.get("INDIC_NAME") != null && column.get("INDIC_NAME") != "" ? column.get("INDIC_NAME").toString().trim() : "");
                resultObject.put("kpiId", kpiId);
                resultObject.put("time_interval", column.get("TIME_INTERVAL") != null && column.get("TIME_INTERVAL") != "" ? column.get("TIME_INTERVAL").toString().trim() : "");
                for (Map<String, Object> result_map :result_List){
                    String kpiValue = result_map.get("column_name") != null && result_map.get("column_name") != "" ? result_map.get("column_name").toString().trim() : "";
                    kpiList.add(kpiValue);
                    //if (kpiValue.contains(".")) {
                    //    kpiValue = df2.format(Double.parseDouble(kpiValue));//当前指标数值百分制
                    //}
                    neNameList.add(result_map.get("NENAME").toString().trim());
                    //kpiList.add(kpiValue);
                }
            } catch (EmptyResultDataAccessException e) {
                resultObject.put("time", firstTime);
                resultObject.put("unit", column.get("INDIC_UNIT") != null && column.get("INDIC_UNIT") != "" ? column.get("INDIC_UNIT").toString().trim() : "");
                resultObject.put("tendency", column.get("INDIC_TYPE") != null && column.get("INDIC_TYPE") != "" ? column.get("INDIC_TYPE").toString().trim() : "");
                resultObject.put("kpiName", column.get("INDIC_NAME") != null && column.get("INDIC_NAME") != "" ? column.get("INDIC_NAME").toString().trim() : "");
                resultObject.put("kpiId", kpiId);
                resultObject.put("time_interval", column.get("TIME_INTERVAL") != null && column.get("TIME_INTERVAL") != "" ? column.get("TIME_INTERVAL").toString().trim() : "");

            }
        }
        resultObject.put("nename", neNameList);
        resultObject.put("kpis", kpiList);

        return resultObject;
    }

    @Override
    public JSONObject getManyTimeKpi(String kpiId, String time, String neName, boolean isMax) {
        List<String> list = Arrays.asList(kpiId.split(","));
        List<Map<String, Object>> column_list = commIndicateZhcxDao.getListById(list);
        JSONObject resultObject = new JSONObject();
        resultObject.put("neName",neName);
        JSONArray resultArray = new JSONArray();
        column_list.forEach((column) -> {
            Map<String, Object> column_map = new HashMap<>();
            String table_name = DataUtil.getString(column,"TABLE_NAME");
            String column_name = DataUtil.getString(column,"COLUMN_NAME");
            String is_float = DataUtil.getString(column,"IS_FLOAT");
            String interval = DataUtil.getString(column, "TIME_INTERVAL");
            String firstTime = "";
            String lastTime = "";
            String timeType = "";
            switch (interval) {
                case "1440"://天
                    if (isMax){
                        lastTime = getMaxTime(table_name);
                    }else {
                        lastTime = time.split("/")[0];
                    }
                    String day = time.split("/")[1];
                    LocalDate date = LocalDateTime.parse(lastTime, minuteFormat).toLocalDate();
                    firstTime = date.minusDays(Long.parseLong(day)).toString();
                    timeType = "mm-dd";
                    break;
                case "60"://小时
                    if (isMax){
                        lastTime = getMaxTime(table_name);
                    }else {
                        lastTime = time.split("/")[0];
                    }
                    LocalDateTime hourTime = LocalDateTime.parse(lastTime, minuteFormat);
                    firstTime = hourTime.minusHours(Long.parseLong(time.split("/")[1])).format(minuteFormat);
                    timeType = "mm-dd hh24";
                    break;
                case "15"://15分钟
                    if (isMax){
                        lastTime = getMaxTime(table_name);
                    }else {
                        lastTime = time.split("/")[0];
                    }
                    LocalDateTime minuteTime = LocalDateTime.parse(lastTime, minuteFormat);
                    firstTime = minuteTime.minusMinutes(Long.parseLong(time.split("/")[1]) * 15).format(minuteFormat);
                    timeType = "hh24:mi";
                    break;
            }
            List<Map<String, Object>> result_maps = commIndicateZhcxDao.manyTimeSearch(table_name,column_name,neName,timeType,firstTime,lastTime);
            String[] kpiTime = new String[result_maps.size()];
            String[] kpiValue = new String[result_maps.size()];
            Stream.iterate(0, i -> i + 1).limit(result_maps.size()).forEach(i -> {
                Map<String, Object> map = result_maps.get(i);
                kpiTime[i] = DataUtil.getString(map,"TIME");
                kpiValue[i] = DataUtil.getString(map,"column_name");
                //if (column_kpiValue.contains(".") && !column_kpiValue.contains("(") && !column_kpiValue.contains(")")) {
                //    BigDecimal bg = new BigDecimal(Double.parseDouble(column_kpiValue));
                //    column_kpiValue = String.valueOf(bg.setScale(Integer.parseInt(is_float), BigDecimal.ROUND_HALF_UP).doubleValue());
                //}
                //kpiValue[i] = column_kpiValue;
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

    private String getMaxTime(String tableName){
        Map<String, Object> maxTime = commIndicateZhcxDao.getMaxTime(tableName);
        return DataUtil.getString(maxTime,"maxTime");
    }


}
