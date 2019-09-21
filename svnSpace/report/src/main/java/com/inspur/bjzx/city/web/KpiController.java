package com.inspur.bjzx.city.web;

import com.google.common.collect.ImmutableMap;
import com.inspur.bjzx.city.service.KpiChartService;
import com.inspur.bjzx.city.service.KpiService;
import com.inspur.bjzx.city.util.RestResult;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by liurui on 2017/8/3.
 */

@RestController
public class KpiController {
    private static Log log = LogFactory.getLog(KpiController.class);

    @Autowired
    KpiService kpiService;

    @Autowired
    KpiChartService kpiChartService;

    //指标分组
    @RequestMapping(value = "/rest/getKpiGrp",method = {RequestMethod.GET})
    public RestResult getKpiGrp(String UserId){
        log.info("/rest/getKpiGrp接口输入参数：{UserId = "+UserId);
        try {
            List<ImmutableMap<String, String>> KpiGrpInfoMap = kpiService.getKpiGrpInfo(UserId);
            if (KpiGrpInfoMap != null){
                if (KpiGrpInfoMap.size() > 0) {
                    return new RestResult<>("success", "查询成功", KpiGrpInfoMap);
                } else {
                    return new RestResult<>("success", "没有指标分组信息");
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return new RestResult<>("fault", "查询出错，请重新查询");
    }

    //指标单页签显示
    //http://localhost:8080/rest/kpiLists1?kpiGrpId=1&regionId=370100&time=2017-08-08
    //http://localhost:8080/rest/kpiLists1?userId=1&regionId=370100&time=2017-08-08
    @RequestMapping(value = "/rest/kpiLists",method = {RequestMethod.GET})
    public RestResult kpiLists(HttpServletRequest httpServletRequest){
        String UserId = httpServletRequest.getParameter("userId");
        String KpiGrpId = httpServletRequest.getParameter("kpiGrpId");
        String time = httpServletRequest.getParameter("time");
        String regionId = httpServletRequest.getParameter("regionId");
        log.info("/rest/kpiLists接口输入参数：{userId = "+UserId+",KpiGrpId = "+KpiGrpId+",time = "+time+",regionId = "+regionId);
        try {
            JSONArray KpiLists = kpiService.getKpiLists(UserId, KpiGrpId, time, regionId);
            if (KpiLists != null){
                if (KpiLists.size() > 0) {
                    return getRestResultSuccess("/rest/kpiLists","查询成功",KpiLists);
                } else {
                    return getRestResultSuccess("/rest/kpiLists","没有指标分组信息",null);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return getRestResultFault("/rest/kpiLists", "查询出错，请重新查询");
    }

    //指标详情显示
    @RequestMapping(value = "/rest/kpiInfo",method = {RequestMethod.GET})
    public RestResult kpiInfo(String time, String mataKpiId){
        log.info("/rest/kpiInfo接口输入参数：{time = "+time+",mataKpiId = "+mataKpiId);
        try {
            JSONArray KpiInfos = kpiChartService.getKpiInfo(time, mataKpiId,"MATAKPI_ID");
            if (KpiInfos != null){
                if (KpiInfos.size() > 0) {
                    return new RestResult<>("success", "查询成功", KpiInfos);
                } else {
                    return new RestResult<>("success", "没有指标详细信息");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RestResult<>("fault", "查询出错，请重新查询");
    }

    //指标详情显示
    @RequestMapping(value = "/rest/kpiChart",method = {RequestMethod.GET})
    public RestResult kpiChart(String time, String chartId){
        log.info("/rest/kpiChart接口输入参数：{time = "+time+",chartId = "+chartId);
        try {
            JSONArray KpiInfos = kpiChartService.getKpiInfo(time, chartId,"ID");
            if (KpiInfos != null){
                if (KpiInfos.size() > 0) {
                    return new RestResult<>("success", "查询成功", KpiInfos);
                } else {
                    return new RestResult<>("success", "没有指标详细信息");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RestResult<>("fault", "查询出错，请重新查询");
    }

    private RestResult<Object> getRestResultFault(String fromApi,String message) {
        RestResult restResult=new RestResult<>("fault", message);
        log.info(fromApi + ":"+restResult.toString());
        //System.out.println(fromApi + ":"+restResult.toString());
        return restResult;
    }

    private RestResult getRestResultSuccess(String fromApi,String message, Object obj) {
        RestResult restResult;
        if (obj != null) {
            restResult = new RestResult<>("success", message, obj);
        } else {
            restResult = new RestResult<>("success", message);
        }
        log.info(fromApi + ":"+restResult.toString());
        //System.out.println(fromApi + ":"+restResult.toString());
        return restResult;
    }

}
