package com.inspur.plugins.ldst.service;

import com.inspur.plugins.ldst.model.CommIndicateZhcx;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class CommIndicateZhcxServiceTest {

    @Resource
    private CommIndicateZhcxService commIndicateZhcxService;

    private String json3;
    @Before
    public void setUp(){
        json3 = "{indicId=248, indicName='OLT退服总数', indicNameAlias='OLT退服', vendorIds='999', spaceDesc='全省', objectClass=111, tableName='pm_gfs_indicate_day', columnName='outline_olt_num', condition='null', indicType=0, indicUnit='%', timeInterval=1440, keyField='NENAME', startTime='time', isLv=0, cityField='999', isFloat=2, columnGroup='sum', fieldName='null', columnRc='null', columnJsj='null', columnJz='null', columnD='null', columnW='null', columnM='null', columnY='null', neIndicId='null', vendorCol='null', colSource='null', note='null', influxDbname='null'}";
    }

    @Test
    public void getCommIndicateZhcxes() {
        List<CommIndicateZhcx> commIndicateZhcxes = commIndicateZhcxService.getCommIndicateZhcxes("248,249");
        String string = commIndicateZhcxes.get(0).toString();
        assertEquals(json3,string);
    }

    @Test
    public void startTime1() {
        try {
            String startTime = commIndicateZhcxService.startTime("2019-08-08/3", "10080");
            assertEquals("2019-07-18",startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void startTime2() {
        try {
            String startTime = commIndicateZhcxService.startTime("2019-08-08/3", "100");
            assertEquals("2019-08-08",startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void startTime3() {
        try {
            String startTime = commIndicateZhcxService.startTime("2019-08-08/3", "1440");
            assertEquals("2019-08-05",startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}