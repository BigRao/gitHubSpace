package com.inspur.plugins.ldst.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class CompanySortCompanyVOTest {

    @Test
    public void testEquals() {
        CompanySortCompanyVO companySortCompanyVO1 = new CompanySortCompanyVO();
        CompanySortCompanyVO companySortCompanyVO2 = new CompanySortCompanyVO();
        companySortCompanyVO1.setName("a");
        companySortCompanyVO2.setName("a");
        boolean equals = companySortCompanyVO1.equals(companySortCompanyVO2);
        companySortCompanyVO1.hashCode();
        assertNotEquals("1", equals);
    }

}