package com.inspur.plugins.kaoshi.service.impl;

import com.inspur.plugins.kaoshi.dao.TestDao;
import com.inspur.plugins.kaoshi.service.TestService;
import com.inspur.plugins.kaoshi.util.DataUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TestServiceImpl implements TestService {
    @Resource
    TestDao testDao;
    @Override
    public Map<String, Object> searchTest(String userId) {
        return testDao.getSearchTest(userId);
    }

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
    public int submitScore(String userId, String testId, String startTime, String endTime, Integer score, String validity) {
        Map<String, Object> passScoreMap = testDao.getPassScore(testId);
        int passScore = Object2Integer(passScoreMap.get("pass_score"));
        int isPass = 0;
        if (score >= passScore) {
            isPass=1;
        }
        return testDao.submitScore(userId,testId,startTime,endTime,score,isPass,validity);
    }

    @Override
    public int submitChoice(String userId, String topicId, String topicChoice, String topicIsRight) {
        Map<String, Object> testIdMap = testDao.getTestId(userId);
        String testId = DataUtil.O2S(testIdMap.get("testId"));
        return testDao.submitChoice(testId,topicId,topicChoice,topicIsRight);
    }

    @Override
    public List<Map<String, Object>> searchTestScore(String userId) {
        return testDao.searchTestScore(userId);
    }

    @Override
    public List<Map<String, Object>> searchTestLog(String testLogId) {
        return testDao.searchTestLog(testLogId);
    }

    @Override
    public Map<String, Object> getLogNum(String testLogId) {
        return testDao.getLogNum(testLogId);
    }

    private Integer Object2Integer(Object object){
        return Integer.parseInt(String.valueOf(object));
    }
}
