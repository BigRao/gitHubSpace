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
public class PersonControllerTest {

    @Resource
    PersonController controller;

    private String json1;
    private String json2;
    private String json3;
    @Before
    public void setUp() throws Exception {
        json1 = "{result='true', message='查询成功', pageCount='null', data=[{persons=[{type=person, hierarchyId=zhang_yan, name=张燕, videoNum=67010520, phone=13901346658, isCheck=false, send_depart=延庆分公司, onLine=false}], isCheck=false, isOpened=false, companyNumber=9, hierarchyId=, name=延庆分公司, onLineCompanyNumber=0, type=company, onLine=}, {persons=[{type=person, hierarchyId=zhangyan1, name=张燕, videoNum=, phone=13901346658, isCheck=false, send_depart=网络部, onLine=false}], isCheck=false, isOpened=false, companyNumber=185, hierarchyId=, name=网络部, onLineCompanyNumber=0, type=company, onLine=}]}";
        json2 = "{result='true', message='查询成功', pageCount='null', data=[{type=person, hierarchyId=sunxun, name=孙逊, videoNum=67010515, phone=13910866121, isCheck=false, send_depart=延庆分公司, onLine=false}, {type=person, hierarchyId=xujianfeng, name=徐健峰, videoNum=67010516, phone=15201639660, isCheck=false, send_depart=延庆分公司, onLine=false}, {type=person, hierarchyId=xijianguo, name=郤建国, videoNum=67010517, phone=13910579908, isCheck=false, send_depart=延庆分公司, onLine=false}, {type=person, hierarchyId=wuqifan, name=吴琦凡, videoNum=67010518, phone=13910057011, isCheck=false, send_depart=延庆分公司, onLine=false}, {type=person, hierarchyId=jianglei, name=蒋磊, videoNum=67010519, phone=13910050532, isCheck=false, send_depart=延庆分公司, onLine=false}, {type=person, hierarchyId=zhang_yan, name=张燕, videoNum=67010520, phone=13901346658, isCheck=false, send_depart=延庆分公司, onLine=false}, {type=person, hierarchyId=zhangyingxu, name=张英旭, videoNum=67010521, phone=13810015242, isCheck=false, send_depart=延庆分公司, onLine=false}, {type=person, hierarchyId=support, name=技术支持1, videoNum=67010522, phone=17810218353, isCheck=false, send_depart=延庆分公司, onLine=false}, {type=person, hierarchyId=xiejy, name=技术支持2, videoNum=67010523, phone=18810433727, isCheck=false, send_depart=延庆分公司, onLine=false}]}";
        json3 = "{result='true', message='查询成功', pageCount='null', data={\"company\":\"延庆分公司\",\"videoNum\":\"67010520\"}}";
    }
    @Test
    public void getCompanyOrPerson() {
        String userAccount = "";
        String searchType = "person";
        String searchType1 = "person";
        String searchText = "张燕";

        //RestResult restResult = controller.getGuaranteeTeam(userAccount, searchType,searchType1, searchText);
        //assertEquals(json1,restResult.toString());
    }

    @Test
    public void getPerson() {
        String userAccount = "张燕";
        String searchText = "延庆分公司";
        RestResult restResult = controller.getPerson(userAccount,searchText);
        assertEquals(json2,restResult.toString());
    }

    @Test
    public void test1() {
        String userAccount = "zhang_yan";
        RestResult restResult = controller.test(userAccount);
        assertEquals(json3,restResult.toString());
    }

}