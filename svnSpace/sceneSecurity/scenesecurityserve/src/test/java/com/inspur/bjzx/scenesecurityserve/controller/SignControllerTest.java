package com.inspur.bjzx.scenesecurityserve.controller;

import com.inspur.bjzx.scenesecurityserve.util.RestResult;
import net.sf.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SignControllerTest {

    @Resource
    SignController controller;

    private String json1;
    private String json2;
    private String json3;
    private String json4;
    @Before
    public void setUp() throws Exception {
        json1 = "{result='true', message='签退成功', pageCount='null', data=[]}";
        json2 = "{result='true', message='查询成功', pageCount='null', data=[{userId=zhang_yan, userName=张燕, signInTime=, signInAddress=, signBackTime=2019-07-10 15:14:14, signBackAddress=北京}]}";
        json3 = "{result='true', message='查询成功', pageCount='null', data=[{longitude=116.439573, latitude=39.947175}]}";
        json4 = "{result='true', message='查询成功', pageCount='null', data=[{userId=zhang_yan, userName=张燕, longitude=116.439573, latitude=39.947175, location=北京, userPhone=13901346658, videoNum=67010520, video=[], picture=[]}]}";
    }

    @Test
    public void realTimeSign() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userAccount","zhang_yan");
        jsonObject.put("longitude","116.439573");
        jsonObject.put("latitude","39.947175");
        jsonObject.put("isSignBack","true");
        jsonObject.put("location","北京");
        RestResult restResult = controller.realTimeSign(jsonObject);
        assertEquals(json1,restResult.toString());
    }

    @Test
    public void searchSign() {
        String userAccount = "zhang_yan";
        RestResult restResult = controller.searchSign(userAccount);
        assertEquals(json2,restResult.toString());
    }

    @Test
    public void searchSignLine() {
        String userAccount = "zhang_yan";
        String signUserId = "zhang_yan";
        RestResult restResult = controller.searchSignLine(userAccount, signUserId);
        assertEquals(json3,restResult.toString());
    }

    @Test
    public void getSecurityPersons() {
        String userAccount = "zflt_yjc1";
        String signUserId = "张燕";
        RestResult restResult = controller.getSecurityPersons(userAccount, signUserId);
        assertEquals(json4,restResult.toString());
    }
}