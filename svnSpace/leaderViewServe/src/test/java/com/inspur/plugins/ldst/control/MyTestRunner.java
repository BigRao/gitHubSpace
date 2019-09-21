package com.inspur.plugins.ldst.control;
import com.inspur.plugins.ldst.model.CellNumObjTest;
import com.inspur.plugins.ldst.model.CompanyOltDataVOTest;
import com.inspur.plugins.ldst.service.CommIndicateZhcxServiceTest;
import com.inspur.plugins.ldst.service.IndicateDayServiceTest;
import com.inspur.plugins.ldst.utils.*;
import org.junit.runner.JUnitCore;
public class MyTestRunner {
    public static void main(String[] args) {
        JUnitCore runner = new JUnitCore();
        runner.addListener(new CommonListener());
        runner.run(CompanyControllerTest.class,KpiControllerTest.class,
                CellNumObjTest.class, CompanyOltDataVOTest.class,
                CommIndicateZhcxServiceTest.class, IndicateDayServiceTest.class,
                DataUtilTest.class, TimeDateTest.class, TrimZeroTest.class,
                WildCardTest.class);
    }
}
