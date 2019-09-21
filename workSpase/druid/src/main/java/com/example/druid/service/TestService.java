package com.example.druid.service;


import java.util.List;
import java.util.Map;

public interface TestService {

    Map<String,Object> getTopicNum();

    Integer getTopicNum(Map<String, Object> topicNum, String type);

    List<Map<String, Object>> getQuestionList(int num, String type);

    List<Map<String, Object>> getAnswer(String id);

    int getCount(String a, String b);
}
