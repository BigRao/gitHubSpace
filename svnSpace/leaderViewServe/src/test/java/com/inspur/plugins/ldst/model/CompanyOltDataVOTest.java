package com.inspur.plugins.ldst.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class CompanyOltDataVOTest {

    @Test
    public void getOltBack() {
        CompanyOltDataVO companyOltDataVO = new CompanyOltDataVO();
        String a1="0";
        String a2="1";
        String a3="1";
        companyOltDataVO.setOltBack("");
        assertEquals(a1,companyOltDataVO.getOltBack());
        companyOltDataVO.setOltBack("1.");
        assertEquals(a2,companyOltDataVO.getOltBack());
        companyOltDataVO.setOltBack("1.00");
        assertEquals(a3,companyOltDataVO.getOltBack());
    }

    @Test
    public void getOltNum() {
        CompanyOltDataVO companyOltDataVO = new CompanyOltDataVO();
        String a1="0";
        String a2="1";
        String a3="1";
        companyOltDataVO.setOltNum("");
        assertEquals(a1,companyOltDataVO.getOltNum());
        companyOltDataVO.setOltNum("1.");
        assertEquals(a2,companyOltDataVO.getOltNum());
        companyOltDataVO.setOltNum("1.00");
        assertEquals(a3,companyOltDataVO.getOltNum());
    }
}