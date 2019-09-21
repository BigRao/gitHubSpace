package com.inspur.bjzx.operationalog.controller;

import com.alibaba.fastjson.JSONObject;
import com.inspur.bjzx.operationalog.model.Recode;
import com.inspur.bjzx.operationalog.service.RecodeService;
import com.inspur.bjzx.operationalog.utils.RestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by bysocket on 07/02/2017.
 */
@RestController
public class RecodeController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private RecodeService recodeService;

    @PostMapping(value = "/recodeLogs")
    public RestResult recodeLogs(@RequestBody JSONObject body, HttpServletRequest request, HttpServletResponse response) {
        try {
            String userId = body.getString("userId");
            String userName = body.getString("userName");
            String typeName = body.getString("typeName");
            String appName = body.getString("appName");
            String moduleName = body.getString("moduleName");
            String params = body.getString("params")!=null?body.getString("params"):"";
            String result = body.getString("result");
            String ip = body.getString("ip");
            Recode recode = new Recode(userId,userName,typeName,appName,moduleName,params,result);
            recode.setInsertTime(new Date());
            recode.setIp(ip);
            logger.info(String.valueOf(recode));
            Boolean res = recodeService.setRecodeLogs(recode);
            if (res) return new RestResult("true", "插入成功");
            return new RestResult("false", "插入失败");
        }catch (Exception e){
            logger.error(e.getMessage());
            return new RestResult("false", "插入失败");
        }
    }

}
