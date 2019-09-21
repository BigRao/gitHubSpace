package com.inspur.bjzx.scenesecurityserve.service;

import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface NetWorkDailyService {
    //获取单一纬度指标值
    JSONObject getSingleLatitudeKpi(String kpiId, String time, String neId, boolean isMax) throws Exception;
    //获取多时间纬度指标值
    JSONObject getManyTimeKpi(String kpiId, String time, String neId, String time_type, boolean isMax) throws Exception;

    List<Map<String, Object>> getBrokenStation(String stationType, String time, String neId) throws Exception;

    List<Map<String, Object>> searchVillage(String neId, String type, String cellType) throws Exception;

    List<Map<String, Object>> searchVillageKpi(String regionId, String villageName, String type) throws Exception;

    JSONObject manyTimeSearchVillageKpi(String cloumnName, String regionId, String villageName, String time, String cellType) throws Exception;

    Map<String, Object> getAbnormalPlot(String neId, String type) throws Exception;
}
