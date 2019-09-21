package com.inspur.bjzx.operationalog.controller;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class RecodeControllerTest {
    @Resource
    private	RecodeController controller;
    @Resource
    private HttpServletRequest httpServletRequest;
    @Resource
    private HttpServletResponse httpServletResponse;
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
        jsonObject.put("ip","7");
        assertEquals("{result='true', message='插入成功', pageCount='null', data=null}",controller.recodeLogs(jsonObject,httpServletRequest,httpServletResponse).toString());
    }
}