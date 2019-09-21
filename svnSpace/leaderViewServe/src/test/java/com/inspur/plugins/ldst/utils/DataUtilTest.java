package com.inspur.plugins.ldst.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class DataUtilTest {

    private static final Map<String, String> map;

    static {
        map = new HashMap<>();
        map.put("int","1");
        map.put("double","1.1");
        map.put("empty","");
        map.put("null",null);
    }

    @Test
    public void o2S() {
        DataUtil dataUtil = new DataUtil();
        Object a = "a";
        assertEquals("a",DataUtil.o2S(a));
    }

    @Test
    public void getInteger() {
        Integer a = 1;
        Integer b = 0;
        assertEquals(a,DataUtil.getInteger(map,"int"));
        assertEquals(b,DataUtil.getInteger(map,"empty"));
    }

    @Test
    public void getDouble() {
        Double a = 1.1;
        Double b = 0.0;
        assertEquals(a,DataUtil.getDouble(map,"double"));
        assertEquals(b,DataUtil.getDouble(map,"empty"));
    }

    @Test
    public void getString() {
        String a = "1";
        String b = "0";
        assertEquals(a,DataUtil.getString(map,"int"));
        assertEquals(b,DataUtil.getString(map,"null"));
    }
}