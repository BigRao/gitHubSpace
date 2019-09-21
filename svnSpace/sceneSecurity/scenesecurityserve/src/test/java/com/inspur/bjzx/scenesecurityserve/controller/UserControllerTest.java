package com.inspur.bjzx.scenesecurityserve.controller;

import com.inspur.bjzx.scenesecurityserve.util.RestResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Resource
    UserController controller;

    private String json1;
    private String json2;

    @Before
    public void setUp() throws Exception {
        json1 = "{result='true', message='没有签到人员信息', pageCount='null', data=null}";
        json2 = "{result='true', message='没有签到人员详细信息', pageCount='null', data=null}";
    }
    @Test
    public void nearlimit() {
        String longitude = "118.4386";
        String latitude = "40.9372";
        String distance = "2000";
        RestResult restResult = controller.nearlimit(longitude, latitude, distance);
        assertEquals(json1,restResult.toString());
    }

    @Test
    public void userInfo() {
        String useraccount = "111.438573";
        String type = "2";
        String depart = "网管中心";
        String longitude = "116.4395";
        String latitude= "39.9371";
        RestResult restResult = controller.userInfo(useraccount,type,depart,longitude,latitude);
        assertEquals(json2,restResult.toString());
    }
}