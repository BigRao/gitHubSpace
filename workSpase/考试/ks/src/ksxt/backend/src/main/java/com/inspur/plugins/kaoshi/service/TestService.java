package com.inspur.plugins.kaoshi.service;


import java.util.List;
import java.util.Map;

public interface TestService {
    Map<String,Object> searchTest(String userId);

    Map<String,Object> getTopicNum();


    Integer getTopicNum(Map<String, Object> topicNum, String type);

    List<Map<String, Object>> getQuestionList(int num, String type);

    List<Map<String, Object>> getAnswer(String id);

    int submitScore(String userId, String testId, String startTime, String endTime, Integer score, String validity);

    int submitChoice(String userId, String topicId, String topicChoice, String topicIsRight);

    List<Map<String, Object>> searchTestScore(String userId);

    List<Map<String, Object>> searchTestLog(String testLogId);

    Map<String, Object> getLogNum(String testLogId);
}
