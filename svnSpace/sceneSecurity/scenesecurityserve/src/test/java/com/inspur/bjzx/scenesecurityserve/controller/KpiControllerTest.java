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
public class KpiControllerTest {
    @Resource
    KpiController kpiController;

    private String json1;
    @Before
    public void setUp() throws Exception {
        json1 = "{result='true', message='查询成功', pageCount='null', data=[{area=四环DQ, area_id=4670, broken_station_two=2, broken_station_four=2, broken_station_five=0, abnormal_cell= , abnormal_computer_room= }, {area=六环DQ, area_id=4671, broken_station_two=0, broken_station_four=1, broken_station_five=0, abnormal_cell= , abnormal_computer_room= }, {area=莲石路DQ, area_id=4675, broken_station_two=1, broken_station_four=1, broken_station_five=0, abnormal_cell= , abnormal_computer_room= }, {area=天坛公园DQ, area_id=4677, broken_station_two=0, broken_station_four=1, broken_station_five=0, abnormal_cell= , abnormal_computer_room= }, {area=长安街DQ, area_id=4683, broken_station_two=0, broken_station_four=1, broken_station_five=0, abnormal_cell= , abnormal_computer_room= }]}";
    }

    @Test
    public void searchKeyAreaBrokenStation() {
        String area_id ="";
        String time="2019-08-01";
        RestResult restResult = kpiController.searchKeyAreaBrokenStation(area_id, time);
        assertEquals(json1,restResult.toString());
    }

}