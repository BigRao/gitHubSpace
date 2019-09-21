package com.inspur.plugins.ldst.service;

import com.inspur.plugins.ldst.model.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


public interface CommIndicateZhcxService {

    /**
     * 通过指标id及time获取指标趋势图
     * @return JSONObject
     */
    List<String> kpiValue(CommIndicateZhcx commIndicateZhcx, String time, String city) throws ParseException;

    CompanyTimeSearchKpiInfoVO getManyTimeKpiInfo(CommIndicateZhcx commIndicateZhcx);

    List<CommIndicateZhcx> getCommIndicateZhcxes(String kpiId);

    /**
     * 获取各个分公司投诉工单数量报表
     * @return JSONObject
     */
    List<CompanyComplanintDataVO> companyComplaint(String kpiId, String time);

    /**
     * 获取各个分公司olt指标报表
     * @return JSONObject
     */
    List<CompanyOltDataVO> companyOlt(String kpiId, String time);

    List<CompanySortKpiVO> companyDayWeek(String name,String time) throws ParseException;

    List<String> timeList(CommIndicateZhcx commIndicateZhcx, String time) throws ParseException;

    String startTime(String time, String timeInterval) throws ParseException;

    Map<String, Object> getOversee(String name);
}
