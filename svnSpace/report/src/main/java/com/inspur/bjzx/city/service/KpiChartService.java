package com.inspur.bjzx.city.service;

import net.sf.json.JSONArray;

public interface KpiChartService {
    //获取指标详情
    JSONArray getKpiInfo(String time, String condition, String type);
}
