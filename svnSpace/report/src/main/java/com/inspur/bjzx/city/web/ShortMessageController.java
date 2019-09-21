package com.inspur.bjzx.city.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inspur.bjzx.city.service.ShortMessageService;
import com.inspur.bjzx.city.util.HttpUtils;
import com.inspur.bjzx.city.util.RestResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ShortMessageController {

    @Autowired
    ShortMessageService shortMessageService;
    private static Log log = LogFactory.getLog(ShortMessageController.class);

    //三网短信接口
    @RequestMapping(value = "/rest/getvcode", method = {RequestMethod.POST})
    public RestResult getvcode(HttpServletRequest httpServletRequest) {
        String phone = httpServletRequest.getParameter("phone");
        String host = "https://cdcxdxjk.market.alicloudapi.com";
        String path = "/chuangxin/dxjk";
        String method = "POST";
        String appcode = "b612cd77ce234c92ad505db4f477d9a7";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        String code = smsCode();
        querys.put("content", "【创信】你的验证码是：" + code + "，3分钟内有效！");
        //测试可用短信模板：【创信】你的验证码是：#code#，3分钟内有效！，如需自定义短信内容或改动任意字符，请联系旺旺或QQ：726980650进行申请。
        querys.put("mobile", phone);
        Map<String, String> bodys = new HashMap<String, String>();

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            JSONObject json = JSON.parseObject(EntityUtils.toString(response.getEntity()));
            System.out.println(json);
            JSONObject data = new JSONObject();
            data.put("code", code);
            log.info("-----------" + json.getString("ReturnStatus") + "---------");
            if ("Success".equals(json.getString("ReturnStatus"))) {
                return new RestResult<>("success", json.getString("Message"), data);
            } else {
                return new RestResult<>("fault", json.getString("Message"));
            }
        } catch (Exception e) {
            return null;
        }
    }

    //@RequestMapping(value = "/rest/getvcode", method = {RequestMethod.POST})
    public RestResult smsCode1(HttpServletRequest httpServletRequest) {
        String random = (int) ((Math.random() * 9 + 1) * 100000) + "";
        JSONObject data = new JSONObject();
        log.info("验证码为" + random);
        data.put("code", random);
        return new RestResult<>("success", "ok", data);
    }

    private static String smsCode() {
        String random = (int) ((Math.random() * 9 + 1) * 100000) + "";
        log.info("验证码为" + random);
        return random;
    }
}
