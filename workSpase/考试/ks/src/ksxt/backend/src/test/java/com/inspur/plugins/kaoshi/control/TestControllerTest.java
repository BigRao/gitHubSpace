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
public class TestControllerTest {
    private static Logger log = LogManager.getLogger(TestControllerTest.class);
    @Resource
    TestController controller;

    private String json1;
    private String json2;
    @Before
    public void setUp() throws Exception {
        json1 = "";
        json2 = "";
    }
    @Test
    public void setQuestion() {
        //RestResult restResult = controller.setQuestion();
        //
        //assertEquals(json1,restResult.toString());
    }

    @Test
    public void searchTest() {
        //String userId = "20190425404";
        //RestResult restResult = controller.searchTest(userId);
        //
        //assertEquals(json1,restResult.toString());
    }


    @Test
    public void submitChoice() {
    }

    @Test
    public void submitScore() {
    }
}