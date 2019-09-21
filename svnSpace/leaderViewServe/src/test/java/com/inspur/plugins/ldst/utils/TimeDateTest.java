package com.inspur.plugins.ldst.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class TimeDateTest {


    @Test
    public void sunday() throws ParseException {
        TimeDate timeDate = new TimeDate();
        String sunday = timeDate.sunday("2019-08-04");
        assertEquals("2019-08-04",sunday);
    }
}