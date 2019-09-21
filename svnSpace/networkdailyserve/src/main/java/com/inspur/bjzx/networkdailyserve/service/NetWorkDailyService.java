package com.inspur.bjzx.networkdailyserve.service;

import net.sf.json.JSONObject;

public interface NetWorkDailyService {
    //获取单一纬度指标值
    JSONObject getSingleLatitudeKpi(String kpiId, String time, String neId);
    //获取多时间纬度指标值
    JSONObject getManyTimeKpi(String kpiId, String time, String neId, String time_type);
    //获取多网元纬度指标值
    JSONObject getManyLatitudeKpi(String kpiId, String time, String neId, String sort);
    //获取top指标值
    JSONObject getTopKpi(String kpiId, String time, String neId);
    //获取收藏的指标
    String[] getCollection(String time, String neId, String userAccount);
    //收藏指标
    String saveOrCancelCollection(String kpiId, String neId, String userAccount);
}
