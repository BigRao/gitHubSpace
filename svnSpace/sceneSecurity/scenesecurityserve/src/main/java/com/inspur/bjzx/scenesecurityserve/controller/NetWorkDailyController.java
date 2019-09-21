package com.inspur.bjzx.scenesecurityserve.controller;

import com.inspur.bjzx.scenesecurityserve.service.NetWorkDailyService;
import com.inspur.bjzx.scenesecurityserve.util.RestResult;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
public class NetWorkDailyController {
    private static final Logger logger = LoggerFactory.getLogger(NetWorkDailyController.class);
    @Autowired
    NetWorkDailyService netWorkDailyService;

    @GetMapping(value = "/singleLatitudeGetKpi", produces = "application/json;charset=UTF-8")
    public RestResult singleLatitudeGetKpi(String kpiId, String time, String neId,boolean isMax ,HttpServletRequest request, HttpServletResponse response) {
        try {
            JSONObject kpis = netWorkDailyService.getSingleLatitudeKpi(kpiId, time, neId,isMax);
            logger.info("singleLatitudeGetKpi____kpis_length：" + kpis);
            return returnResult(response, kpis);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResult(response, null);
        }
    }

    @GetMapping(value = "/manyTimeSearch", produces = "application/json;charset=UTF-8")
    public RestResult manyTimeSearch(String kpiId, String time, String neId, String time_type,boolean isMax, HttpServletRequest request, HttpServletResponse response) {
        try {
            JSONObject kpis = netWorkDailyService.getManyTimeKpi(kpiId, time, neId, time_type,isMax);
            logger.info("manyTimeSearch____kpis_length：" + kpis);
            return returnResult(response, kpis);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResult(response, null);
        }
    }

    @GetMapping(value = "/brokenStation", produces = "application/json;charset=UTF-8")
    public RestResult getBrokenStation(String stationType, String time, String neId, HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Map<String, Object>> brokenStation = netWorkDailyService.getBrokenStation(stationType, time, neId);
            if (brokenStation.size() > 0) {
                return returnResult(response, brokenStation);
            }
            return new RestResult<>("true", "当前时间无数据", brokenStation);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResult(response, null);
        }
    }

    @GetMapping(value = "/abnormalPlot", produces = "application/json;charset=UTF-8")
    public RestResult getAbnormalPlot(String neId,String type, HttpServletRequest request, HttpServletResponse response) {
        try {
        Map<String, Object> abnormalPlot =  netWorkDailyService.getAbnormalPlot(neId,type);
        return returnResult(response, abnormalPlot);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResult(response, null);
        }
    }

    @GetMapping(value = "/searchVillage", produces = "application/json;charset=UTF-8")
    public RestResult searchVillage(String neId, String type, String cellType , HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Map<String, Object>> villageList = netWorkDailyService.searchVillage(neId, type ,cellType);
            return returnResult(response, villageList);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResult(response, null);
        }
    }

    @GetMapping(value = "/searchVillageKpi", produces = "application/json;charset=UTF-8")
    public RestResult searchVillageKpi(String regionId, String villageName, String type,HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Map<String, Object>> villageList = netWorkDailyService.searchVillageKpi(regionId, villageName,type);
            return returnResult(response, villageList);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResult(response, null);
        }
    }
    @GetMapping(value = "/manyTimeSearchVillageKpi", produces = "application/json;charset=UTF-8")
    public RestResult manyTimeSearchVillageKpi(String cloumnName,String regionId, String villageName, String time,String cellType ,HttpServletRequest request, HttpServletResponse response) {
        try {
            JSONObject villageList = netWorkDailyService.manyTimeSearchVillageKpi(cloumnName,regionId,villageName,time,cellType);
            return returnResult(response, villageList);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResult(response, null);
        }
    }
    private RestResult returnResult(HttpServletResponse response, Object kpiObject) {
        if (kpiObject != null) {
            return new RestResult<>("true", "查询成功", kpiObject);
        } else {
            return new RestResult("false", "查询失败");
        }
    }
}
