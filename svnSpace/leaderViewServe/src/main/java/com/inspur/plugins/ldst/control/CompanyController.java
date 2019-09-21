package com.inspur.plugins.ldst.control;

import com.inspur.plugins.ldst.model.*;
import com.inspur.plugins.ldst.service.*;
import com.alibaba.fastjson.JSON;
import com.inspur.plugins.ldst.utils.DataUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "company")
public class CompanyController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String TRUE_MESSAGE = "查询数据成功";
    private static final String FALSE_MESSAGE = "当前时间无数据";
    private static final String[] ONE = {"城一分公司", "城二分公司", "城三分公司"};
    private static final String[] TWO = {"大兴分公司", "昌平分公司", "通州分公司", "顺义分公司", "房山分公司"};
    private static final String[] THREE = {"密云分公司", "怀柔分公司", "平谷分公司", "延庆分公司"};
    private static final Map<String, String[]> categoryToName;

    static {
        categoryToName = new HashMap<>();
        categoryToName.put("一类公司", ONE);
        categoryToName.put("二类公司", TWO);
        categoryToName.put("三类公司", THREE);
    }

    @Resource
    private PersonService personService;

    @Resource
    private IndicateDayService indicateDayService;

    @Resource
    private CommIndicateZhcxService commIndicateZhcxService;

    @GetMapping(value = "companySort", produces = "application/json;charset=UTF-8")
    public String companySort(String time) {
        CompanySortVO companySortVO = new CompanySortVO();
        companySortVO.setResult(TRUE);
        companySortVO.setMessage(TRUE_MESSAGE);
        List<CompanySortDataVO> dataList = new ArrayList<>();
        try {
            for (Map.Entry<String, String[]> category : categoryToName.entrySet()) {
                CompanySortDataVO data = new CompanySortDataVO();
                data.setCompanyCategory(category.getKey());
                List<CompanySortCompanyVO> companyList = new ArrayList<>();
                int notStandardNum = 0;
                for (String name : category.getValue()) {
                    CompanySortCompanyVO company = new CompanySortCompanyVO();
                    company.setName(name);
                    Map<String, Object> oversee = commIndicateZhcxService.getOversee(name);
                    company.setOversee(DataUtil.o2S(oversee.get("oversee")));
                    company.setPhone(DataUtil.o2S(oversee.get("phone")));
                    List<CompanySortKpiVO> kpi = commIndicateZhcxService.companyDayWeek(name, time);
                    notStandardNum += kpi.size();
                    company.setKpi(kpi);
                    companyList.add(company);
                }
                data.setNotStandardNum(notStandardNum);
                Collections.sort(companyList);
                data.setCompany(companyList);

                dataList.add(data);
                Collections.sort(dataList);
            }
        } catch (Exception e) {
            companySortVO.setResult(FALSE);
            companySortVO.setMessage(FALSE_MESSAGE);
            logger.error(e.toString());
            return JSON.toJSONString(companySortVO);
        }
        companySortVO.setData(dataList);
        return JSON.toJSONString(companySortVO);
    }

    @GetMapping(value = "retireNum", produces = "application/json;charset=UTF-8")
    public JSONObject retireNum() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yesterday = indicateDayService.getYesterday();
        String today = sdf.format(new Date());
        return indicateDayService.retireNum(today, yesterday);

    }

    @PostMapping(value = "sms", produces = "application/json;charset=UTF-8")
    public RestResult<String> sms(@RequestBody String body) {
        try {
            String message = personService.sms(body);
            return new RestResult<>(TRUE, "发送短信成功", message);
        } catch (Exception e) {
            logger.error(e.toString());
            return new RestResult<>(FALSE, "发送短信失败");
        }


    }

}