package com.example.druid.control;


import com.example.druid.model.Topic;
import com.example.druid.service.TestService;
import com.example.druid.utils.DataUtil;
import com.example.druid.utils.RestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.applet.Main;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class TestController {
    private static Logger log = LogManager.getLogger(TestController.class);
    @Resource
    TestService testService;

    private static final String TRUE = "true";
    private static final String FALSE = "false";

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
            log.info("/test/setQuestion error\n{}", e.getMessage());
            return new RestResult(FALSE, "查询失败");
        } finally {
            log.info("\n\n");
        }
    }
    @GetMapping(value = "/count")
    public RestResult getCount(String a,String b) {
        try{
            log.info("进入/count");
            int count = testService.getCount(a, b);
            log.info("count:{}",count);
            return new RestResult<>(TRUE, "查询成功", count);
        }catch (Exception e){
            return new RestResult<>(FALSE, "查询失败");
        }
    }

    public static void main(String[] args) {
        Pattern p = Pattern.compile("^\\w\\S+_(20190920\\d+)_\\S+(.csv|.zip)");
        Matcher m = p.matcher("PM_My_hw_4G_201909201500_1hr.zip");
        boolean r =m.matches();
        log.info(r);

    }

}
