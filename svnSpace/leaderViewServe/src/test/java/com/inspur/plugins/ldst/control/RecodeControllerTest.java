package com.inspur.plugins.ldst.control;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class RecodeControllerTest {
    @Resource
    private RecodeController controller;
    @Resource
    private HttpServletRequest request;
    @Resource
    private HttpServletResponse response;

    private String json1;

    @Before
    public void setUp(){
        json1 = "{\"result\":\"true\",\"message\":\"插入成功\",\"pageCount\":null,\"data\":null}";
    }
    @Test
    public void recodeLogs() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId",123);
        jsonObject.put("userName","1");
        jsonObject.put("typeName","2");
        jsonObject.put("appName","3");
        jsonObject.put("moduleName","4");
        jsonObject.put("params","5");
        jsonObject.put("result","6");
        assertEquals(json1,controller.recodeLogs(jsonObject,request,response));
    }
}