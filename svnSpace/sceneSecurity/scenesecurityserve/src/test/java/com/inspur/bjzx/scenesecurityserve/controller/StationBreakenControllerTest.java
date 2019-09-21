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
public class StationBreakenControllerTest {

    @Resource
    StationBreakenController controller;

    private String json1;
    private String json2;
    private String json3;
    private String json4;
    @Before
    public void setUp() throws Exception {
        json1 = "{result='false', message='查询失败', pageCount='null', data=null}";
        json2 = "{result='false', message='查询失败', pageCount='null', data=null}";
        json3 = "{result='false', message='查询失败', pageCount='null', data=null}";
    }

    @Test
    public void getNum() {
        String userAccount = "yuehezhang";
        RestResult restResult = controller.getNum(userAccount);
        assertEquals(json1,restResult.toString());
    }

    @Test
    public void getDetail() {
        String userAccount = "zhang_yan";
        String associatedId = "55594";
        RestResult restResult = controller.getDetail(userAccount,associatedId);
        assertEquals(json2,restResult.toString());
    }

    @Test
    public void searchFaultWorks() {
        String userAccount = "zhang_yan";
        String associatedId = "55594";
        RestResult restResult = controller.searchFaultWorks(userAccount,associatedId);
        assertEquals(json3,restResult.toString());
    }
}