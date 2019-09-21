package com.inspur.plugins.ldst.service;

import net.sf.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class IndicateDayServiceTest {
    @Resource
    private IndicateDayService indicateDayService;
    private String json3;
    @Before
    public void setUp(){
        json3 = "{\"result\":\"true\",\"message\":\"查询数据成功\",\"data\":[{\"viewName\":\"2G退服\",\"viewData\":[{\"kpiId\":\"244\",\"kpiName\":\"退服数\",\"kpiValue\":\"0\"},{\"kpiId\":\"245\",\"kpiName\":\"基站数\",\"kpiValue\":\"0\"},{\"kpiId\":\"0\",\"kpiName\":\"占比\",\"kpiValue\":\"0%\"}]},{\"viewName\":\"4G退服\",\"viewData\":[{\"kpiId\":\"246\",\"kpiName\":\"退服数\",\"kpiValue\":\"0\"},{\"kpiId\":\"247\",\"kpiName\":\"基站数\",\"kpiValue\":\"0\"},{\"kpiId\":\"0\",\"kpiName\":\"占比\",\"kpiValue\":\"0%\"}]},{\"viewName\":\"OLT退服\",\"viewData\":[{\"kpiId\":\"248\",\"kpiName\":\"退服数\",\"kpiValue\":\"0\"},{\"kpiId\":\"249\",\"kpiName\":\"OLT总数\",\"kpiValue\":\"0\"},{\"kpiId\":\"250\",\"kpiName\":\"占比\",\"kpiValue\":\"0%\"}]},{\"viewName\":\"投诉工单\",\"viewData\":[{\"kpiId\":\"30\",\"kpiName\":\"家客\",\"kpiValue\":\"0\"},{\"kpiId\":\"31\",\"kpiName\":\"移动\",\"kpiValue\":\"0\"}]},{\"viewName\":\"5G退服\",\"viewData\":[{\"kpiId\":\"259\",\"kpiName\":\"退服数\",\"kpiValue\":\"0\"},{\"kpiId\":\"260\",\"kpiName\":\"基站数\",\"kpiValue\":\"0\"},{\"kpiId\":\"0\",\"kpiName\":\"占比\",\"kpiValue\":\"0%\"}]}]}";
    }
    @Test
    public void retireNum3() {
        String yesterday = "2019-04-02";
        String today = "2019-04-01";
        JSONObject jsonObject = indicateDayService.retireNum(today, yesterday);
        assertEquals(json3,jsonObject.toString());
    }
}