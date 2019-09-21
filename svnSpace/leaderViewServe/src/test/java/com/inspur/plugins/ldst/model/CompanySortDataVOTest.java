package com.inspur.plugins.ldst.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class CompanySortDataVOTest {

    @Test
    public void testEquals() {
        CompanySortDataVO companySortDataVO1 = new CompanySortDataVO();
        CompanySortDataVO companySortDataVO2 = new CompanySortDataVO();
        companySortDataVO1.setNotStandardNum(1);
        companySortDataVO2.setNotStandardNum(1);
        boolean equals = companySortDataVO1.equals(companySortDataVO2);
        companySortDataVO1.hashCode();
        assertNotEquals("1", equals);
    }

}