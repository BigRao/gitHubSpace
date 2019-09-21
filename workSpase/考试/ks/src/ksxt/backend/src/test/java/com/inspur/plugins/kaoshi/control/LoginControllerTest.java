package com.inspur.plugins.kaoshi.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginControllerTest {
    private static Logger log = LogManager.getLogger(LoginControllerTest.class);
    @Resource
    LoginController controller;

    private String json1;
    private String json2;
    @Before
    public void setUp() throws Exception {
        json1 = "";
        json2 = "";
    }

    @Test
    public void verificationCode() {

    }

    @Test
    public void register() {
        //JSONObject jsonObject = new JSONObject();
        //jsonObject.put("id","3");
        //jsonObject.put("userName","2");
        //jsonObject.put("userPhone","3");
        //
        //RestResult restResult = controller.register(jsonObject);
        //log.info(restResult.toString());

    }

    @Test
    public void login() {
        //JSONObject jsonObject = new JSONObject();
        //jsonObject.put("userId","50207fa2814e81a067bd2662ba10b0f1");
        //jsonObject.put("userName","刘蕊");
        //jsonObject.put("userPhone","18210198436");
        //
        //RestResult restResult = controller.login(jsonObject);
        //
        //log.info(restResult.toString());

    }
}