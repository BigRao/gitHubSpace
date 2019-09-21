package com.example.druid.service.impl;


import com.example.druid.dao.TestDao;
import com.example.druid.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TestServiceImpl implements TestService {
    @Resource
    TestDao testDao;

    @Override
    public Map<String, Object> getTopicNum() {
        return testDao.getTopicNum();
    }


    private Integer selectMust(String type) {
        Map<String, Object> selectMust = testDao.selectMust(type);
        return Object2Integer(selectMust.get("count"));
    }

    @Override
    public Integer getTopicNum(Map<String, Object> topicNum, String type) {
        return Object2Integer(topicNum.get(type));
    }

    @Override
    public List<Map<String, Object>> getQuestionList(int num, String type) {
        num = num-selectMust(type);
        List<Map<String, Object>> mustList = testDao.getMust(type);
        List<Map<String, Object>> topicList = testDao.getTopic(num,type);
        topicList.addAll(mustList);
        return topicList;
    }

    @Override
    public List<Map<String, Object>> getAnswer(String id) {
        return testDao.getAnswer(Integer.parseInt(id));
    }

    @Override
    public int getCount(String a, String b) {
        return testDao.count(a,b);
    }

    private Integer Object2Integer(Object object){
        return Integer.parseInt(String.valueOf(object));
    }


}
