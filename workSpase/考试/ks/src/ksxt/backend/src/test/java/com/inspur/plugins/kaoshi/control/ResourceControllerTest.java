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
public class ResourceControllerTest {
    private static Logger log = LogManager.getLogger(ResourceControllerTest.class);
    @Resource
    ResourceController controller;

    private String json1;
    private String json2;
    @Before
    public void setUp() throws Exception {
        json1 = "";
        json2 = "";
    }

    @Test
    public void resourceList() {
        //String userId = "1";
        //String pageStart = "2";
        //String pageSize = "3";
        //
        //RestResult restResult = controller.resourceList(userId,pageStart, pageSize);
        //
        //log.info(restResult.toString());
    }
    /*@Test
    public void resourceLook() {
        String resourceId = "3";
        RestResult restResult = controller.resourceLook(resourceId);
        assertEquals(json1,restResult.toString());
    }*/

    @Test
    public void recordCurrentTime() {
        //JSONObject jsonObject = new JSONObject();
        //jsonObject.put("resourceId","1");
        //jsonObject.put("userId","20190425404");
        //jsonObject.put("startTime","2019-07-19 09:20:32");
        //jsonObject.put("endTime","2019-07-19 10:13:28");
        //jsonObject.put("isPass","1");

        //RestResult restResult = controller.recordCurrentTime(jsonObject);
        //log.info(restResult.toString());
    }

    @Test
    public void resourceSearch() {
        //String resourceId = "1";
        //String userId = "20190425404";
        //RestResult restResult = controller.resourceSearch(resourceId,userId);
        //
        //log.info(restResult.toString());
    }
}