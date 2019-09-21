package com.inspur.plugins.kaoshi.service;

import java.util.List;
import java.util.Map;

public interface ResourceService {
    List<Map<String,Object>> getResourceList(String userId, String pageStart, String pageSize);

    Map<String, Object> resourceLook(String resourceId);

    int recordCurrentTime(String resourceId, String userId, String startTime, String endTime, String isPass);

    Map<String, Object> getResourceSearch(String resourceId, String userId);

    Map<String, Object> resourceCount();

    Map<String, Object> resourceLogCount(String resourceId, String userId);

    Map<String, Object> getResourceName(String resourceId);
}
