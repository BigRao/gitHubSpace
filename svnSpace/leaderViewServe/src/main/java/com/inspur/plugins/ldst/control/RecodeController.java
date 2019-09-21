package com.inspur.plugins.ldst.control;

import com.alibaba.fastjson.JSONObject;
import com.inspur.plugins.ldst.utils.UrlUtil;
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
    @PostMapping(value = "/recodeLogs")
    public String recodeLogs(@RequestBody JSONObject body, HttpServletRequest request, HttpServletResponse response) {
        body.put("ip", UrlUtil.getIpAddress(request));
        logger.info("parameters:{}",body);
        ResponseEntity<String> response2 = restTemplate.postForEntity(
                url, body, String.class);
        String result = response2.getBody();
        logger.info("result:{}",result);
        return result;
    }

}
