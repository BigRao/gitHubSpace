package com.inspur.plugins.kaoshi.service.impl;

import com.inspur.plugins.kaoshi.dao.ResourceDao;
import com.inspur.plugins.kaoshi.service.ResourceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Resource
    ResourceDao resourceDao;

    @Override
    public List<Map<String, Object>> getResourceList(String userId,String pageStart, String pageSize) {
        int num1 = Integer.parseInt(pageStart);
        int num2 = Integer.parseInt(pageSize);
        int number1 = num1*num2;
        int number2 = (num1-1)*num2;
        return resourceDao.getResourceList(userId,number1,number2);
    }

    @Override
    public Map<String, Object> resourceLook(String resourceId) {
        return resourceDao.resourceLook(resourceId);
    }

    @Override
    public int recordCurrentTime(String resourceId, String userId, String startTime, String endTime, String isPass) {
        return resourceDao.recordCurrentTime(resourceId, userId, startTime, endTime, isPass);
    }

    @Override
    public Map<String, Object> getResourceSearch(String resourceId, String userId) {
        return resourceDao.getResourceSearch(resourceId,userId);
    }

    @Override
    public Map<String, Object> resourceCount() {
        return resourceDao.getResourceCount();
    }

    @Override
    public Map<String, Object> resourceLogCount(String resourceId, String userId) {
        return resourceDao.resourceLogCount(resourceId  ,userId);
    }

    @Override
    public Map<String, Object> getResourceName(String resourceId) {
        return resourceDao.getResourceName(resourceId);
    }
}
