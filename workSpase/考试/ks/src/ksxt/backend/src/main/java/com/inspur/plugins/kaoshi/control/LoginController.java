package com.inspur.plugins.kaoshi.control;

import com.alibaba.fastjson.JSONObject;
import com.inspur.plugins.kaoshi.Interface.PublicInterface;
import com.inspur.plugins.kaoshi.Interface.PublicInterfaceProxy;
import com.inspur.plugins.kaoshi.model.User;
import com.inspur.plugins.kaoshi.service.LoginService;
import com.inspur.plugins.kaoshi.util.AESUtils;
import com.inspur.plugins.kaoshi.util.RestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class LoginController {
    private static Logger log = LogManager.getLogger(LoginController.class);
    @Resource
    LoginService loginService;

    private static final String TRUE = "true";
    private static final String FALSE = "false";

    @GetMapping(value = "verificationCode")
    public RestResult verificationCode(String userPhone) {
        try {
            String verificationCode = String.valueOf((int) (((Math.random() * 9 + 1) * 100000)));
            PublicInterface t = new PublicInterfaceProxy();
            String message = t.sendSMS("<sms username=\"sts\"password=\"Sts.123\"><head system=\"SEQ\" service=\"alarm_sms\"  priority=\"2\" seqno=\"\"/>"
                    + "<mobile>" + userPhone + "</mobile><message>您的验证码为:" + verificationCode + "</message></sms>");
            log.info(message);
            return new RestResult<>(TRUE, "发送成功", verificationCode);
        } catch (Exception e) {
            log.info("verificationCode error\n" + e.getMessage());
            return new RestResult(FALSE, "发送失败");
        } finally {
            log.info("\n\n");
        }

    }

    @PostMapping(value = "register")
    public RestResult register(@RequestBody JSONObject body) {
        try {
            String userId = AESUtils.decoder(body.getString("userId"));
            String userName = AESUtils.decoder(body.getString("userName"));
            String userPhone = AESUtils.decoder(body.getString("userPhone"));
            log.info("register == userId:" + userId + ",userName:" + userName + ",userPhone:" + userPhone);
            if (loginService.findUserById(userId) > 0) {
                return new RestResult(FALSE, "注册失败，该身份证号已被注册");
            }
            loginService.insertUser(userId, userName, userPhone);
            return new RestResult(TRUE, "注册成功");
        } catch (Exception e) {
            log.info("register error\n" + e.getMessage());
            return new RestResult(FALSE, "注册失败");
        } finally {
            log.info("\n\n");
        }

    }

    @PostMapping(value = "login")
    public RestResult login(@RequestBody JSONObject body) {
        try {
            String userId = AESUtils.decoder(body.getString("userId"));
            String userName = AESUtils.decoder(body.getString("userName"));
            String userPhone = AESUtils.decoder(body.getString("userPhone"));
            log.info("login == userId:" + userId + ",userName:" + userName + ",userPhone:" + userPhone);
            if (loginService.findUserById(userId) <= 0) {
                return new RestResult(FALSE, "登录失败，该身份证号没有注册");
            }
            User user = loginService.getUserById(userId);
            log.info(user);
            if (userName.equals(user.getName())) {
                return new RestResult(TRUE, "登录成功");
            } else {
                return new RestResult(FALSE, "登录失败，用户名错误");
            }
        } catch (Exception e) {
            log.info("login error\n" + e.getMessage());
            return new RestResult(FALSE, "登录失败");
        } finally {
            log.info("\n\n");
        }
    }
}
