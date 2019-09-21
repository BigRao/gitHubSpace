package com.inspur.bjzx.networkdailyserve.web;

import com.inspur.bjzx.networkdailyserve.service.NetWorkDailyService;
import com.inspur.bjzx.networkdailyserve.util.RestResult;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
public class NetWorkDailyController {
    private static Logger logger = LoggerFactory.getLogger(NetWorkDailyController.class);
    @Autowired
    NetWorkDailyService netWorkDailyService;

    @RequestMapping(value = "/singleLatitudeGetKpi", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public RestResult singleLatitudeGetKpi(String kpiId, String time, String neId, HttpServletRequest request, HttpServletResponse response){
        try{
            JSONObject kpis = netWorkDailyService.getSingleLatitudeKpi(kpiId, time, neId);
            logger.info("singleLatitudeGetKpi____kpis_length：" +kpis);
            return returnResult(response, kpis);
        }catch (Exception e){
            e.printStackTrace();
            return returnResult(response, null);
        }
    }

    @RequestMapping(value = "/manyTimeSearch", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public RestResult manyTimeSearch(String kpiId, String time, String neId, String time_type, HttpServletRequest request, HttpServletResponse response){
        try{
            JSONObject kpis = netWorkDailyService.getManyTimeKpi(kpiId, time, neId, time_type);
            logger.info("manyTimeSearch____kpis_length：" +kpis);
            return returnResult(response, kpis);
        }catch (Exception e){
            e.printStackTrace();
            return returnResult(response, null);
        }
    }

    @RequestMapping(value = "/manyLatitudeGetKpi", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public RestResult manyLatitudeGetKpi(String kpiId, String time, String neId, String sort, HttpServletRequest request, HttpServletResponse response){
        try{
            JSONObject kpis = netWorkDailyService.getManyLatitudeKpi(kpiId, time, neId, sort);
            logger.info("manyLatitudeGetKpi____kpis_length：" +kpis);
            return returnResult(response, kpis);
        }catch (Exception e){
            e.printStackTrace();
            return returnResult(response, null);
        }
    }

    @RequestMapping(value = "/getTopKpi", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public RestResult getTopKpi(String kpiId, String time, String neId, HttpServletRequest request, HttpServletResponse response){
        try{
            JSONObject kpis = netWorkDailyService.getTopKpi(kpiId, time, neId);
            logger.info("getTopKpi____kpis_length：" +kpis);
            return returnResult(response, kpis);
        }catch (Exception e){
            e.printStackTrace();
            return returnResult(response, null);
        }
    }

    @RequestMapping(value = "/getCollection", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public RestResult getCollection(String time, String neId, String userAccount, HttpServletRequest request, HttpServletResponse response){
        try{
            String[] kpis = netWorkDailyService.getCollection(time, neId, userAccount);
            logger.info("getCollection____kpis_length：" + kpis.length);
            return returnResult(response, kpis);
        }catch (Exception e){
            e.printStackTrace();
            return returnResult(response, null);
        }
    }

    @RequestMapping(value = "/saveOrCancelCollection", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public RestResult saveOrCancelCollection(String kpiId, String neId, String userAccount, HttpServletRequest request, HttpServletResponse response){
        try{
            String isSuccess = netWorkDailyService.saveOrCancelCollection(kpiId, neId, userAccount);
            logger.info("saveCollection____kpis_length：" +isSuccess);
            return new RestResult("true", isSuccess);
        }catch (Exception e){
            e.printStackTrace();
            return new RestResult("false", "失败");
        }
    }

    private RestResult returnResult(HttpServletResponse response, Object kpiObject) {
        if(kpiObject != null){
            return new RestResult("true", "查询成功", kpiObject);
        }else {
            return new RestResult("false", "查询失败");
        }
    }
}
