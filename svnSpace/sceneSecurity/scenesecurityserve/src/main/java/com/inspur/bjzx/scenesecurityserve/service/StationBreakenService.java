package com.inspur.bjzx.scenesecurityserve.service;

import java.util.List;
import java.util.Map;

public interface StationBreakenService {

     int getStationBreakenNum(String userAccount);
     int getStationBreakenNumAll();
     int getFaultAreaNum();
     List<Map<String,Object>> getDetail();
     List<Map<String,Object>> getDetail(String id);
     List<Map<String,Object>> getDetailByUserAccount(String userAccount);

}
