package com.inspur.plugins.kaoshi.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inspur.plugins.kaoshi.model.Topic;
import com.inspur.plugins.kaoshi.service.TestService;
import com.inspur.plugins.kaoshi.util.AESUtils;
import com.inspur.plugins.kaoshi.util.DataUtil;
import com.inspur.plugins.kaoshi.util.DateUtil;
import com.inspur.plugins.kaoshi.util.RestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController
public class TestController {
    private static Logger log = LogManager.getLogger(TestController.class);
    @Resource
    TestService testService;

    private static final String TRUE = "true";
    private static final String FALSE = "false";

    @GetMapping(value = "/test/searchTest")
    public RestResult searchTest(String userId) {
        try {
            Map<String, Object> map = testService.searchTest(AESUtils.decoder(userId));
            return new RestResult<>(TRUE, "查询成功", map);
        } catch (Exception e) {
            log.info("/test/searchTest error\n" + e.getMessage());
            return new RestResult(FALSE, "查询失败");
        } finally {
            log.info("\n\n");
        }
    }

    @GetMapping(value = "/test/setQuestion")
    public RestResult setQuestion() {
        try {
            Map<String, Object> topicNum = testService.getTopicNum();
            Integer singleNum = testService.getTopicNum(topicNum, "single_num");
            Integer multipleNum = testService.getTopicNum(topicNum, "multiple_num");
            Integer judgeNum = testService.getTopicNum(topicNum, "judge_num");
            List<Map<String, Object>> questionList = testService.getQuestionList(singleNum, "单选");
            questionList.addAll(testService.getQuestionList(multipleNum, "多选"));
            questionList.addAll(testService.getQuestionList(judgeNum, "判断"));
            log.info(questionList);
            List<Topic> topicList = new ArrayList<>();
            for (Map<String, Object> question : questionList) {

                String id = DataUtil.O2S(question.get("id"));
                String name = DataUtil.O2S(question.get("name"));
                String type = DataUtil.O2S(question.get("type"));
                String score = DataUtil.O2S(question.get("score"));
                List<Map<String, Object>> answerList = testService.getAnswer(id);
                List<String> answers = new ArrayList<>();
                StringBuilder trueAnswer = new StringBuilder();
                for (Map<String, Object> answer : answerList) {
                    answers.add(DataUtil.O2S(answer.get("answer")));
                    trueAnswer.append(DataUtil.O2S(answer.get("right")));
                }
                topicList.add(new Topic(id, name, score, trueAnswer.toString(), type, answers));
            }
            return new RestResult<>(TRUE, "查询成功", topicList);
        } catch (Exception e) {
            log.info("/test/setQuestion error\n" + e.getMessage());
            return new RestResult(FALSE, "查询失败");
        } finally {
            log.info("\n\n");
        }
    }

    @PostMapping(value = "/test/submitChoice")
    public RestResult submitChoice(@RequestBody JSONObject body) {
        try {

            String userId = AESUtils.decoder(body.getString("user_id"));
            JSONArray choices = body.getJSONArray("choices");
            for (Object obj : choices) {
                JSONObject json = (JSONObject) JSON.toJSON(obj);
                String topicId = json.getString("topicId");
                String topicChoice = json.getString("topicChoice");
                StringBuilder stringBuilder = new StringBuilder();
                if (topicChoice.contains("1")) {
                    for (int i = 0; i < topicChoice.length(); i++) {
                        int s = topicChoice.indexOf("1", i);
                        i = s;
                        if (s == -1) {
                            break;
                        }
                        stringBuilder.append(DataUtil.backChar(s + 65));
                    }
                } else {
                    stringBuilder.append("未选择");
                }
                String topicIsRight = json.getString("topicIsRight");
                testService.submitChoice(userId, topicId, stringBuilder.toString(), topicIsRight);
            }
            return new RestResult<>(TRUE, "提交成功");
        } catch (Exception e) {
            log.info("/test/submitChoice error\n" + e.getMessage());
            return new RestResult(FALSE, "失败");
        } finally {
            log.info("\n\n");
        }
    }

    @PostMapping(value = "/test/submitScore")
    public RestResult submitScore(@RequestBody JSONObject body) {
        try {

            String userId = AESUtils.decoder(body.getString("userId"));
            String testId = body.getString("testId");
            String startTime = body.getString("startTime");
            String endTime = body.getString("endTime");
            Integer score = DataUtil.O2I(body.getString("score"));
            Calendar cal = Calendar.getInstance();
            int month = cal.get(Calendar.MONTH) + 1;
            int result;
            if (month == 12) {
                int year = cal.get(Calendar.YEAR) + 1;
                String yearLast = DateUtil.getYearLast(year);
                result = testService.submitScore(userId, testId, startTime, endTime, score, yearLast);
            } else {
                result = testService.submitScore(userId, testId, startTime, endTime, score, DateUtil.getCurrYearLast());
            }

            if (result == 1) {
                return new RestResult<>(TRUE, "提交成功");
            } else {
                return new RestResult(FALSE, "提交失败");
            }
        } catch (Exception e) {
            log.info("/test/submitScore error\n" + e.getMessage());
            return new RestResult(FALSE, "失败");
        } finally {
            log.info("\n\n");
        }
    }

    @GetMapping(value = "test/searchTestScore")
    public RestResult searchTestScore(String userId) {
        try {

            List<Map<String, Object>> searchTestScore = testService.searchTestScore(AESUtils.decoder(userId));
            if (searchTestScore.size() == 0) {
                return new RestResult<>(TRUE, "查询成功", "当前无考试成绩");
            }
            return new RestResult<>(TRUE, "查询成功", searchTestScore);

        } catch (Exception e) {
            log.info("/test/searchTestScore error\n" + e.getMessage());
            return new RestResult(FALSE, "查询失败");
        } finally {
            log.info("\n\n");
        }
    }

    @GetMapping(value = "/test/searchTestLog")
    public RestResult searchTestLog(String userId, String testLogId) {
        try {
            Map<String, Object> logNum = testService.getLogNum(testLogId);
            Integer count = DataUtil.O2I(logNum.get("count"));
            List<Map<String, Object>> searchTestLog;
            if (count > 0) {
                searchTestLog = testService.searchTestLog(testLogId);
            } else {
                return new RestResult(FALSE, "超出有效时间");
            }
            return new RestResult<>(TRUE, "查询成功", searchTestLog);

        } catch (Exception e) {
            log.info("/test/searchTestLog error\n" + e.getMessage());
            return new RestResult(FALSE, "查询失败");
        } finally {
            log.info("\n\n");
        }
    }

}
