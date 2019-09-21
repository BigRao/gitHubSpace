package com.inspur.plugins.ldst.control;

import com.inspur.plugins.ldst.model.RestResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CompanyControllerTest {
    @Resource
    private CompanyController companyController;

    private String json1;

    private String json2;
    private String json3;
    private String json4;
    @Before
    public void setUp(){
        json1 = "{\"result\":\"true\",\"message\":\"查询数据成功\",\"data\":[{\"viewName\":\"2G退服\",\"viewData\":[{\"kpiId\":\"244\",\"kpiName\":\"退服数\",\"kpiValue\":\"79\"},{\"kpiId\":\"245\",\"kpiName\":\"基站数\",\"kpiValue\":\"38929\"},{\"kpiId\":\"0\",\"kpiName\":\"占比\",\"kpiValue\":\"0.2%\"}]},{\"viewName\":\"4G退服\",\"viewData\":[{\"kpiId\":\"246\",\"kpiName\":\"退服数\",\"kpiValue\":\"308\"},{\"kpiId\":\"247\",\"kpiName\":\"基站数\",\"kpiValue\":\"55247\"},{\"kpiId\":\"0\",\"kpiName\":\"占比\",\"kpiValue\":\"0.56%\"}]},{\"viewName\":\"OLT退服\",\"viewData\":[{\"kpiId\":\"248\",\"kpiName\":\"退服数\",\"kpiValue\":\"84\"},{\"kpiId\":\"249\",\"kpiName\":\"OLT总数\",\"kpiValue\":\"2787\"},{\"kpiId\":\"250\",\"kpiName\":\"占比\",\"kpiValue\":\"16.71%\"}]},{\"viewName\":\"投诉工单\",\"viewData\":[{\"kpiId\":\"30\",\"kpiName\":\"家客\",\"kpiValue\":\"736\"},{\"kpiId\":\"31\",\"kpiName\":\"移动\",\"kpiValue\":\"489\"}]},{\"viewName\":\"5G退服\",\"viewData\":[{\"kpiId\":\"259\",\"kpiName\":\"退服数\",\"kpiValue\":\"0\"},{\"kpiId\":\"260\",\"kpiName\":\"基站数\",\"kpiValue\":\"0\"},{\"kpiId\":\"0\",\"kpiName\":\"占比\",\"kpiValue\":\"0%\"}]}]}";
    	json2 = "{\"result\":\"true\",\"message\":\"查询数据成功\",\"data\":[{\"companyCategory\":\"一类公司\",\"notStandardNum\":8,\"company\":[{\"name\":\"城一分公司\",\"oversee\":\"武勇\",\"phone\":\"12345678901\",\"kpi\":[{\"id\":\"234\",\"name\":\"完成及时率\",\"value\":\"86.64%\",\"kpiCategory\":\"故障工单\",\"standard\":\">=95.0%\",\"netValue\":\"87.06%\"},{\"id\":\"232\",\"name\":\"无线退服率_2G\",\"value\":\"4.55‰\",\"kpiCategory\":\"网络运行\",\"standard\":\"<=4.0‰\",\"netValue\":\"5.14‰\"},{\"id\":\"237\",\"name\":\"一次质检通过率\",\"value\":\"94.41%\",\"kpiCategory\":\"故障工单\",\"standard\":\">=95.0%\",\"netValue\":\"95.75%\"}]},{\"name\":\"城二分公司\",\"oversee\":\"沈国悦\",\"phone\":\"1111111111\",\"kpi\":[{\"id\":\"234\",\"name\":\"完成及时率\",\"value\":\"84.5%\",\"kpiCategory\":\"故障工单\",\"standard\":\">=95.0%\",\"netValue\":\"87.06%\"},{\"id\":\"232\",\"name\":\"无线退服率_2G\",\"value\":\"9.70‰\",\"kpiCategory\":\"网络运行\",\"standard\":\"<=4.0‰\",\"netValue\":\"5.14‰\"},{\"id\":\"233\",\"name\":\"无线退服率_4G\",\"value\":\"4.17‰\",\"kpiCategory\":\"网络运行\",\"standard\":\"<=4.0‰\",\"netValue\":\"3.68‰\"}]},{\"name\":\"城三分公司\",\"oversee\":\"张小明\",\"phone\":\"2222222222222\",\"kpi\":[{\"id\":\"234\",\"name\":\"完成及时率\",\"value\":\"77.93%\",\"kpiCategory\":\"故障工单\",\"standard\":\">=95.0%\",\"netValue\":\"87.06%\"},{\"id\":\"232\",\"name\":\"无线退服率_2G\",\"value\":\"4.50‰\",\"kpiCategory\":\"网络运行\",\"standard\":\"<=4.0‰\",\"netValue\":\"5.14‰\"}]}]},{\"companyCategory\":\"二类公司\",\"notStandardNum\":8,\"company\":[{\"name\":\"通州分公司\",\"oversee\":\"崔文占\",\"phone\":\"6666666666666\",\"kpi\":[{\"id\":\"234\",\"name\":\"完成及时率\",\"value\":\"87.82%\",\"kpiCategory\":\"故障工单\",\"standard\":\">=95.0%\",\"netValue\":\"87.06%\"},{\"id\":\"232\",\"name\":\"无线退服率_2G\",\"value\":\"6.90‰\",\"kpiCategory\":\"网络运行\",\"standard\":\"<=4.0‰\",\"netValue\":\"5.14‰\"}]},{\"name\":\"顺义分公司\",\"oversee\":\"田本坤\",\"phone\":\"777777777777\",\"kpi\":[{\"id\":\"234\",\"name\":\"完成及时率\",\"value\":\"88.58%\",\"kpiCategory\":\"故障工单\",\"standard\":\">=95.0%\",\"netValue\":\"87.06%\"},{\"id\":\"237\",\"name\":\"一次质检通过率\",\"value\":\"90.6%\",\"kpiCategory\":\"故障工单\",\"standard\":\">=95.0%\",\"netValue\":\"95.75%\"}]},{\"name\":\"房山分公司\",\"oversee\":\"常艳海\",\"phone\":\"8888888888888\",\"kpi\":[{\"id\":\"241\",\"name\":\"派发一线率\",\"value\":\"8.82%\",\"kpiCategory\":\"工单直派\",\"standard\":\">=15.0%\",\"netValue\":\"29.92%\"},{\"id\":\"234\",\"name\":\"完成及时率\",\"value\":\"94.76%\",\"kpiCategory\":\"故障工单\",\"standard\":\">=95.0%\",\"netValue\":\"87.06%\"}]},{\"name\":\"大兴分公司\",\"oversee\":\"李晨珂\",\"phone\":\"333333333333\",\"kpi\":[{\"id\":\"237\",\"name\":\"一次质检通过率\",\"value\":\"87.5%\",\"kpiCategory\":\"故障工单\",\"standard\":\">=95.0%\",\"netValue\":\"95.75%\"}]},{\"name\":\"昌平分公司\",\"oversee\":\"路振明\",\"phone\":\"555555555555\",\"kpi\":[{\"id\":\"234\",\"name\":\"完成及时率\",\"value\":\"92.49%\",\"kpiCategory\":\"故障工单\",\"standard\":\">=95.0%\",\"netValue\":\"87.06%\"}]}]},{\"companyCategory\":\"三类公司\",\"notStandardNum\":2,\"company\":[{\"name\":\"怀柔分公司\",\"oversee\":\"崔继辉\",\"phone\":\"00000000000\",\"kpi\":[{\"id\":\"237\",\"name\":\"一次质检通过率\",\"value\":\"93.33%\",\"kpiCategory\":\"故障工单\",\"standard\":\">=95.0%\",\"netValue\":\"95.75%\"}]},{\"name\":\"平谷分公司\",\"oversee\":\"张锋\",\"phone\":\"1321313123141\",\"kpi\":[{\"id\":\"234\",\"name\":\"完成及时率\",\"value\":\"92.31%\",\"kpiCategory\":\"故障工单\",\"standard\":\">=95.0%\",\"netValue\":\"87.06%\"}]},{\"name\":\"密云分公司\",\"oversee\":\"杨凤辉\",\"phone\":\"99999999999\",\"kpi\":[]},{\"name\":\"延庆分公司\",\"oversee\":\"方博雄\",\"phone\":\"565745756745\",\"kpi\":[]}]}]}";
        json3 = "{\"result\":\"false\",\"message\":\"当前时间无数据\"}";
        json4 = "{\"result\":\"true\",\"message\":\"查询数据成功\",\"data\":[{\"companyCategory\":\"一类公司\",\"notStandardNum\":5,\"company\":[{\"name\":\"城一分公司\",\"oversee\":\"武勇\",\"phone\":\"12345678901\",\"kpi\":[{\"id\":\"243\",\"name\":\"无线处理质量\",\"value\":\"95.60%\",\"kpiCategory\":\"代维效益\",\"standard\":\">=96%\",\"netValue\":\"96.70%\"},{\"id\":\"242\",\"name\":\"故障机房占比\",\"value\":\"2.30%\",\"kpiCategory\":\"代维效益\",\"standard\":\"<1%\",\"netValue\":\"1.50%\"}]},{\"name\":\"城二分公司\",\"oversee\":\"沈国悦\",\"phone\":\"1111111111\",\"kpi\":[{\"id\":\"243\",\"name\":\"无线处理质量\",\"value\":\"95.90%\",\"kpiCategory\":\"代维效益\",\"standard\":\">=96%\",\"netValue\":\"96.70%\"},{\"id\":\"242\",\"name\":\"故障机房占比\",\"value\":\"1.90%\",\"kpiCategory\":\"代维效益\",\"standard\":\"<1%\",\"netValue\":\"1.50%\"}]},{\"name\":\"城三分公司\",\"oversee\":\"张小明\",\"phone\":\"2222222222222\",\"kpi\":[{\"id\":\"242\",\"name\":\"故障机房占比\",\"value\":\"1.10%\",\"kpiCategory\":\"代维效益\",\"standard\":\"<1%\",\"netValue\":\"1.50%\"}]}]},{\"companyCategory\":\"二类公司\",\"notStandardNum\":2,\"company\":[{\"name\":\"大兴分公司\",\"oversee\":\"李晨珂\",\"phone\":\"333333333333\",\"kpi\":[{\"id\":\"242\",\"name\":\"故障机房占比\",\"value\":\"1.10%\",\"kpiCategory\":\"代维效益\",\"standard\":\"<1%\",\"netValue\":\"1.50%\"}]},{\"name\":\"通州分公司\",\"oversee\":\"崔文占\",\"phone\":\"6666666666666\",\"kpi\":[{\"id\":\"242\",\"name\":\"故障机房占比\",\"value\":\"1.20%\",\"kpiCategory\":\"代维效益\",\"standard\":\"<1%\",\"netValue\":\"1.50%\"}]},{\"name\":\"昌平分公司\",\"oversee\":\"路振明\",\"phone\":\"555555555555\",\"kpi\":[]},{\"name\":\"顺义分公司\",\"oversee\":\"田本坤\",\"phone\":\"777777777777\",\"kpi\":[]},{\"name\":\"房山分公司\",\"oversee\":\"常艳海\",\"phone\":\"8888888888888\",\"kpi\":[]}]},{\"companyCategory\":\"三类公司\",\"notStandardNum\":0,\"company\":[{\"name\":\"密云分公司\",\"oversee\":\"杨凤辉\",\"phone\":\"99999999999\",\"kpi\":[]},{\"name\":\"怀柔分公司\",\"oversee\":\"崔继辉\",\"phone\":\"00000000000\",\"kpi\":[]},{\"name\":\"平谷分公司\",\"oversee\":\"张锋\",\"phone\":\"1321313123141\",\"kpi\":[]},{\"name\":\"延庆分公司\",\"oversee\":\"方博雄\",\"phone\":\"565745756745\",\"kpi\":[]}]}]}";

    }

    @Test
    public void retireNum() {
        String retireNum = companyController.retireNum().toString();
        assertEquals(json1,retireNum);

    }

    @Test
    public void companySort1() {
        String companySort = companyController.companySort("2019-07-02");
        assertEquals(json2,companySort);

    }
    @Test
    public void companySort2() {
        String companySort = companyController.companySort("20190803");
        assertEquals(json3,companySort);

    }
    @Test
    public void companySort3() {
        String companySort = companyController.companySort("2019-06-12");
        assertEquals(json4,companySort);

    }
    @Test
    public void sms1() {
        String body = "{\n" +
                "    \"name\": \"武勇\",\n" +
                "    \"message\": \"开开开\"\n" +
                "}";
        RestResult sms = companyController.sms(body);
        String result = "{result='true', message='发送短信成功', data=开开开城一分公司武勇}";
        assertEquals(result,sms.toString());

    }
    @Test
    public void sms2() {
        String body = "{\n" +
                "    \"name\": \"武勇11\",\n" +
                "    \"message\": \"开开开\"\n" +
                "}";
        RestResult sms = companyController.sms(body);
        String result = "{result='false', message='发送短信失败', data=null}";
        assertEquals(result,sms.toString());

    }


}