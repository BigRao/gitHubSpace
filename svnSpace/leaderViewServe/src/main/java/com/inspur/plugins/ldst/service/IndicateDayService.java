package com.inspur.plugins.ldst.service;


import net.sf.json.JSONObject;

public interface IndicateDayService {

    JSONObject retireNum(String today, String yesterday);

    String getYesterday();
}
