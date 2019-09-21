package com.inspur.bjzx.scenesecurityserve.service;

import java.util.List;
import java.util.Map;

public interface KpiService {

    List<Map<String, Object>> searchKpi(String searchContent, String time);

    List<Map<String, Object>> getAreaTwo(Map<String, Object> areaIdMap);

    List<Map<String, Object>> getAreaThree(String areaTwo,Map<String, Object> areaIdMap);

    List<Map<String, Object>> getBrokenStation(String area_id);

    Map<String, Object> getUserArea(String userNameApp);

    int getBrokenStationNum(String type,String regionId);
}
