package com.inspur.plugins.ldst.control;

import com.inspur.plugins.ldst.model.*;
import com.inspur.plugins.ldst.service.CommIndicateZhcxService;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "kpi")
public class KpiController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private CommIndicateZhcxService commIndicateZhcxService;

    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String TRUE_MESSAGE = "查询数据成功";
    private static final String FALSE_MESSAGE = "当前时间无数据";
    private static final String ERROR_MESSAGE = "时间错误";

    @GetMapping(value = "manyTimeSearch", produces = "application/json;charset=UTF-8")
    public String manyTimeSearch(String kpiId, String time,String city) {
        ManyTimeSearchVO timeSearchVO = new ManyTimeSearchVO();
        CompanyTimeSearchDataVO dataVO = new CompanyTimeSearchDataVO();
        List<CommIndicateZhcx> rows = commIndicateZhcxService.getCommIndicateZhcxes(kpiId);
        List<CompanyComplanintKpiVO> kpiList = new ArrayList<>();
        List<String> timeList = null;
        try {
            for (CommIndicateZhcx commIndicateZhcx : rows) {
                CompanyTimeSearchKpiInfoVO kpiInfoVO = commIndicateZhcxService.getManyTimeKpiInfo(commIndicateZhcx);
                List<String> kpiValue = commIndicateZhcxService.kpiValue(commIndicateZhcx, time, city);

                CompanyComplanintKpiVO kpi = new CompanyComplanintKpiVO(kpiInfoVO, kpiValue);
                timeList = commIndicateZhcxService.timeList(commIndicateZhcx, time);
                kpiList.add(kpi);
            }
        } catch (Exception e) {
            logger.info(e.toString());
            timeSearchVO.setResult(FALSE);
            timeSearchVO.setMessage(ERROR_MESSAGE);
            return JSON.toJSONString(timeSearchVO);
        }
        dataVO.setTime(timeList);
        dataVO.setCity(city);
        dataVO.setKpis(kpiList);
        timeSearchVO.setData(dataVO);
        timeSearchVO.setResult(TRUE);
        timeSearchVO.setMessage(TRUE_MESSAGE);
        return JSON.toJSONString(timeSearchVO);
    }

    @GetMapping(value = "companyComplaint", produces = "application/json;charset=UTF-8")
    public String companyComplaint(String kpiId, String time) {

        CompanyComplaintVO companyComplaintVO = new CompanyComplaintVO();
        companyComplaintVO.setResult(TRUE);
        companyComplaintVO.setMessage(TRUE_MESSAGE);
        try {
            List<CompanyComplanintDataVO> companyComplanintDataVOS = commIndicateZhcxService.companyComplaint(kpiId, time);
            if (companyComplanintDataVOS.isEmpty()) {
                companyComplaintVO.setResult(FALSE);
                companyComplaintVO.setMessage(FALSE_MESSAGE);
            }
            companyComplaintVO.setData(companyComplanintDataVOS);

            return JSON.toJSONString(companyComplaintVO);
        } catch (Exception e) {
            logger.info(e.toString());
            companyComplaintVO.setResult(FALSE);
            companyComplaintVO.setMessage(FALSE_MESSAGE);
            companyComplaintVO.setData(null);
            return JSON.toJSONString(companyComplaintVO);

        }

    }


    @GetMapping(value = "companyOlt", produces = "application/json;charset=UTF-8")
    public String companyOlt( String kpiId, String time) {

        CompanyOltVO companyOltVO = new CompanyOltVO();
        companyOltVO.setResult(TRUE);
        companyOltVO.setMessage(TRUE_MESSAGE);
        try {
            List<CompanyOltDataVO> companyOltDataVOS = commIndicateZhcxService.companyOlt(kpiId, time);
            if (companyOltDataVOS.isEmpty()) {
                companyOltVO.setResult(FALSE);
                companyOltVO.setMessage(FALSE_MESSAGE);
            }
            companyOltVO.setData(companyOltDataVOS);

            return JSON.toJSONString(companyOltVO);
        } catch (Exception e) {
            logger.info(e.toString());
            companyOltVO.setResult(FALSE);
            companyOltVO.setMessage(FALSE_MESSAGE);
            companyOltVO.setData(null);
            return JSON.toJSONString(companyOltVO);

        }
    }

}
