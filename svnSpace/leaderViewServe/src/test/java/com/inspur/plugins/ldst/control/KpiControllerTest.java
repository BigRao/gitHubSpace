package com.inspur.plugins.ldst.control;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KpiControllerTest {

    @Resource
    private KpiController kpiController;

    private String json1;

    private String json2;

    private String json3;
    private String json4;
    private String json5;
    private String json6;
    @Before
    public void setUp(){

        json1 = "{\"result\":\"true\",\"message\":\"查询数据成功\",\"data\":{\"time\":[\"2019-06-21\",\"2019-06-22\",\"2019-06-23\",\"2019-06-24\",\"2019-06-25\",\"2019-06-26\",\"2019-06-27\",\"2019-06-28\",\"2019-06-29\",\"2019-06-30\",\"2019-07-01\"],\"city\":\"城一分公司\",\"kpis\":[{\"kpiInfo\":{\"kpiId\":\"248\",\"kpiName\":\"OLT退服总数\",\"unit\":\"%\",\"tendency\":\"\"},\"kpiValue\":[\"4\",\"4\",\"3\",\"0\",\"0\",\"1\",\"12\",\"1\",\"25\",\"0\",\"0\"]},{\"kpiInfo\":{\"kpiId\":\"249\",\"kpiName\":\"OLT基站数\",\"unit\":\"%\",\"tendency\":\"\"},\"kpiValue\":[\"309\",\"309\",\"309\",\"309\",\"0\",\"309\",\"309\",\"309\",\"309\",\"309\",\"0\"]},{\"kpiInfo\":{\"kpiId\":\"250\",\"kpiName\":\"OLT退服率\",\"unit\":\"%\",\"tendency\":\"\"},\"kpiValue\":[\"0.1\",\"0.3\",\"0.04\",\"0\",\"0\",\"0.06\",\"0.02\",\"0.01\",\"0.04\",\"0\",\"0\"]}]}}";
        json2 = "{\"result\":\"true\",\"message\":\"查询数据成功\",\"data\":[{\"category\":\"一类\",\"company\":\"城二分公司\",\"plmnComplaint\":\"67\",\"hwnNum\":\"151\"},{\"category\":\"二类\",\"company\":\"大兴分公司\",\"plmnComplaint\":\"36\",\"hwnNum\":\"124\"},{\"category\":\"二类\",\"company\":\"房山分公司\",\"plmnComplaint\":\"27\",\"hwnNum\":\"40\"},{\"category\":\"三类\",\"company\":\"平谷分公司\",\"plmnComplaint\":\"2\",\"hwnNum\":\"7\"},{\"category\":\"一类\",\"company\":\"城一分公司\",\"plmnComplaint\":\"129\",\"hwnNum\":\"125\"},{\"category\":\"二类\",\"company\":\"通州分公司\",\"plmnComplaint\":\"30\",\"hwnNum\":\"75\"},{\"category\":\"三类\",\"company\":\"怀柔分公司\",\"plmnComplaint\":\"7\",\"hwnNum\":\"14\"},{\"category\":\"一类\",\"company\":\"城三分公司\",\"plmnComplaint\":\"69\",\"hwnNum\":\"138\"},{\"category\":\"三类\",\"company\":\"密云分公司\",\"plmnComplaint\":\"6\",\"hwnNum\":\"19\"},{\"category\":\"合计\",\"company\":\"合计\",\"plmnComplaint\":\"419\",\"hwnNum\":\"838\"},{\"category\":\"二类\",\"company\":\"顺义分公司\",\"plmnComplaint\":\"20\",\"hwnNum\":\"69\"},{\"category\":\"二类\",\"company\":\"昌平分公司\",\"plmnComplaint\":\"24\",\"hwnNum\":\"68\"},{\"category\":\"三类\",\"company\":\"延庆分公司\",\"plmnComplaint\":\"2\",\"hwnNum\":\"8\"}]}";
        json3 = "{\"result\":\"true\",\"message\":\"查询数据成功\",\"data\":[{\"category\":\"三类\",\"company\":\"延庆分公司\",\"oltBack\":\"0\",\"oltBackProportion\":\"0\",\"oltNum\":\"76\"},{\"category\":\"二类\",\"company\":\"顺义分公司\",\"oltBack\":\"3\",\"oltBackProportion\":\"0.34\",\"oltNum\":\"171\"},{\"category\":\"二类\",\"company\":\"昌平分公司\",\"oltBack\":\"1\",\"oltBackProportion\":\"0\",\"oltNum\":\"220\"},{\"category\":\"三类\",\"company\":\"密云分公司\",\"oltBack\":\"0\",\"oltBackProportion\":\"0\",\"oltNum\":\"80\"},{\"category\":\"二类\",\"company\":\"房山分公司\",\"oltBack\":\"0\",\"oltBackProportion\":\"0\",\"oltNum\":\"153\"},{\"category\":\"一类\",\"company\":\"城三分公司\",\"oltBack\":\"2\",\"oltBackProportion\":\"0.06\",\"oltNum\":\"387\"},{\"category\":\"一类\",\"company\":\"城一分公司\",\"oltBack\":\"42\",\"oltBackProportion\":\"0.11\",\"oltNum\":\"309\"},{\"category\":\"二类\",\"company\":\"通州分公司\",\"oltBack\":\"1\",\"oltBackProportion\":\"0.03\",\"oltNum\":\"238\"},{\"category\":\"三类\",\"company\":\"怀柔分公司\",\"oltBack\":\"0\",\"oltBackProportion\":\"0\",\"oltNum\":\"78\"},{\"category\":\"三类\",\"company\":\"平谷分公司\",\"oltBack\":\"0\",\"oltBackProportion\":\"0\",\"oltNum\":\"79\"},{\"category\":\"一类\",\"company\":\"城二分公司\",\"oltBack\":\"3\",\"oltBackProportion\":\"0.01\",\"oltNum\":\"476\"},{\"category\":\"二类\",\"company\":\"大兴分公司\",\"oltBack\":\"0\",\"oltBackProportion\":\"0\",\"oltNum\":\"137\"},{\"category\":\"合计\",\"company\":\"合计\",\"oltBack\":\"100\",\"oltBackProportion\":\"0.1\",\"oltNum\":\"2000\"}]}";
        json4 = "{\"result\":\"true\",\"message\":\"查询数据成功\",\"data\":{\"time\":[\"2019-06-07\",\"2019-06-08\",\"2019-06-09\",\"2019-06-10\",\"2019-06-11\",\"2019-06-12\",\"2019-06-13\",\"2019-06-14\"],\"city\":\"城一分公司\",\"kpis\":[{\"kpiInfo\":{\"kpiId\":\"242\",\"kpiName\":\"代维巡检效益\",\"unit\":\"%\",\"tendency\":\"\"},\"kpiValue\":[\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0.023\",\"0\"]}]}}";
        json5 = "{\"result\":\"false\",\"message\":\"当前时间无数据\",\"data\":[]}";
        json6 = "{\"result\":\"false\",\"message\":\"当前时间无数据\"}";
    }
    @Test
    public void manyTimeSearch1()  {

        String kpiId ="248,249,250";
        String time ="2019-07-01/10";
        String city ="城一分公司";

        String manyTimeSearch = kpiController.manyTimeSearch(kpiId, time, city);
        assertEquals(json1,manyTimeSearch);

    }
    @Test
    public void manyTimeSearch2()  {

        String kpiId ="242";
        String time ="2019-06-14/1";
        String city ="城一分公司";

        String manyTimeSearch = kpiController.manyTimeSearch(kpiId, time, city);
        assertEquals(json4,manyTimeSearch);

    }
    @Test
    public void manyTimeSearch3()  {

        String kpiId ="242";
        String time ="201";
        String city ="城一分公司";

        String manyTimeSearch = kpiController.manyTimeSearch(kpiId, time, city);
        String result = "{\"result\":\"false\",\"message\":\"时间错误\"}";
        assertEquals(result,manyTimeSearch);

    }
    @Test
    public void companyComplaint1(){
        String kpiId ="30,31";
        String time ="2019-06-14";
        String companyComplaint = kpiController.companyComplaint(kpiId, time);
        assertEquals(json2,companyComplaint);
    }
    @Test
    public void companyComplaint2(){
        String kpiId ="30,31";
        String time ="2019";
        String companyComplaint = kpiController.companyComplaint(kpiId, time);
        assertEquals(json5,companyComplaint);
    }

    @Test
    public void companyComplaint3(){
        String kpiId ="3";
        String time ="2019";
        String companyComplaint = kpiController.companyComplaint(kpiId, time);
        assertEquals(json6,companyComplaint);
    }

    @Test
    public void companyOlt1(){

        String kpiId ="248,249,250";
        String time ="2019-07-02";
        String companyOlt = kpiController.companyOlt(kpiId, time);

        assertEquals(json3,companyOlt);
    }

    @Test
    public void companyOlt2(){

        String kpiId ="248,249,250";
        String time ="2019";
        String companyOlt = kpiController.companyOlt(kpiId, time);
        String result = "{\"result\":\"false\",\"message\":\"当前时间无数据\",\"data\":[]}";
        assertEquals(result,companyOlt);
    }

    @Test
    public void companyOlt3(){

        String kpiId ="2";
        String time ="2019";
        String companyOlt = kpiController.companyOlt(kpiId, time);
        String result = "{\"result\":\"false\",\"message\":\"当前时间无数据\"}";
        assertEquals(result,companyOlt);
    }


}
