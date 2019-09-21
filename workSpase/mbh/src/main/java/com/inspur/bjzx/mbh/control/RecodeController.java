package com.inspur.bjzx.mbh.control;

import com.alibaba.fastjson.JSONObject;
import com.inspur.bjzx.mbh.utils.Base64Util;
import com.inspur.bjzx.mbh.utils.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by bysocket on 07/02/2017.
 */
@RestController
public class RecodeController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RestTemplate restTemplate;

    @Value("${app-log-url}")
    private String url;
    @PostMapping(value = "/recordLog")
    public String recodeLogs(@RequestBody JSONObject body, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId",getString(body,"userId"));
        jsonObject.put("userName",getString(body,"userName"));
        jsonObject.put("typeName",getString(body,"typeName"));
        jsonObject.put("appName",getString(body,"appName"));
        jsonObject.put("moduleName",getString(body,"moduleName"));
        jsonObject.put("result",getString(body,"result"));
        jsonObject.put("params",getString(body,body.getString("params")!=null?body.getString("params"):""));

        jsonObject.put("ip", UrlUtil.getIpAddress(request));
        logger.info("parameters:{}",body);
        ResponseEntity<String> response2 = restTemplate.postForEntity(
                url, jsonObject, String.class);
        String result = response2.getBody();
        logger.info("result:{}",result);
        return result;
    }
    private String getString (JSONObject body,String key){
        return Base64Util.decoder(body.getString(key));
    }
}
