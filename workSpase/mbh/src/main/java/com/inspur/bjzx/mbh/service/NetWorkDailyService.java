package com.inspur.bjzx.mbh.service;


import com.alibaba.fastjson.JSONObject;

public interface NetWorkDailyService {

    //获取单一纬度指标值
    JSONObject getSingleLatitudeKpi(String kpiIds, String time);

    JSONObject getRankKpi(String kpiId, String time, String neName);

    JSONObject getManyTimeKpi(String kpiId, String time, String neName, boolean isMax);




}
