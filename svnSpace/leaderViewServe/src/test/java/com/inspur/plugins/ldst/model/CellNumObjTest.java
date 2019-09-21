package com.inspur.plugins.ldst.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class CellNumObjTest {


    private String json3;
    @Before
    public void setUp(){
        json3 = "{maintainArea='1', zongJi2g=1, zongJiLte=2}";
    }
    @Test
    public void getMaintainArea() {
        CellNumObj cellNumObj = new CellNumObj();
        cellNumObj.setMaintainArea("1");
        cellNumObj.setZongJi2g(1);
        cellNumObj.setZongJiLte(2);
        Integer i1 = 1;
        Integer i2 = 2;
        assertEquals("1",cellNumObj.getMaintainArea());
        assertEquals(i1,cellNumObj.getZongJi2g());
        assertEquals(i2,cellNumObj.getZongJiLte());
        assertEquals(json3,cellNumObj.toString());
    }

}