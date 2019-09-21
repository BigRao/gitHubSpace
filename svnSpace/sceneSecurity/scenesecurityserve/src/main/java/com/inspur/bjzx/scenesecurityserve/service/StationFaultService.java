package com.inspur.bjzx.scenesecurityserve.service;

import java.util.List;
import java.util.Map;

public interface StationFaultService {

    int getStationFaultNum(String userAccount);
    int getStationFaultNumAll();
    List<Map<String,Object>> getDetail();
    List<Map<String,Object>> getDetail(String id);
    List<Map<String,Object>> getDetailByUserAccount(String userAccount);
}
