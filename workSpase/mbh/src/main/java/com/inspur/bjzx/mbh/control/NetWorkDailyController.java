package com.inspur.bjzx.mbh.control;

import com.alibaba.fastjson.JSONObject;
import com.inspur.bjzx.mbh.service.NetWorkDailyService;
import com.inspur.bjzx.mbh.utils.RestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class NetWorkDailyController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    NetWorkDailyService netWorkDailyService;

    @GetMapping(value = "/singleLatitudeGetKpi", produces = "application/json;charset=UTF-8")
    public RestResult singleLatitudeGetKpi(String kpiIds, String time, HttpServletRequest request, HttpServletResponse response) {
        try {
            JSONObject kpis = netWorkDailyService.getSingleLatitudeKpi(kpiIds, time);
            logger.info("singleLatitudeGetKpi____kpis_length：" + kpis);
            return returnResult(response, kpis);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResult(response, null);
        }
    }
    @GetMapping(value = "/rankKpi", produces = "application/json;charset=UTF-8")
    public RestResult rankKpi(String kpiId, String time, String neName , HttpServletRequest request, HttpServletResponse response) {
        try {
            JSONObject kpis = netWorkDailyService.getRankKpi(kpiId, time, neName);
            logger.info("manyTimeSearch____kpis_length：" + kpis);
            return returnResult(response, kpis);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResult(response, null);
        }
    }
    @GetMapping(value = "/manyTimeSearch", produces = "application/json;charset=UTF-8")
    public RestResult manyTimeSearch(String kpiId, String time, String neName , boolean isMax, HttpServletRequest request, HttpServletResponse response) {
        try {
            JSONObject kpis = netWorkDailyService.getManyTimeKpi(kpiId, time, neName,isMax);
            logger.info("manyTimeSearch____kpis_length：" + kpis);
            return returnResult(response, kpis);
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
