package com.inspur.bjzx.city.web;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.inspur.bjzx.city.service.CommonService;
import com.inspur.bjzx.city.util.DataUtil;
import com.inspur.bjzx.city.util.RestResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by liurui on 2017/8/3.
 */

@RestController
public class CommonController {
    private static Log log = LogFactory.getLog(CommonController.class);

    @Autowired
    CommonService commonService;

    @Value("${app.url}")
    String appUrl;
    
    private static final String EMPTY = "";
    private static final String SUCCESS = "成功";
    private static final String FAIL = "失败";
    private static final String RESET_PASSWORD = "密码重置";
    private static final String REGISTRATION_MODULE = "注册模块";
    
    //登录
    @RequestMapping(value = "/rest/login", method = {RequestMethod.POST})
    public RestResult login(HttpServletRequest httpServletRequest) {
        String userAccount = httpServletRequest.getParameter("useraccount");
        String userPwd = httpServletRequest.getParameter("userpwd");
        log.info("/rest/login接口输入参数：{useraccount = "+userAccount+",userpwd = "+userPwd+"}");
        if (userAccount == null || EMPTY.equals(userAccount)) return getRestResultFault("/rest/login","参数错误，请重新查询");
        try {
            List<ImmutableMap<String, String>> loginInfoMap = commonService.getLoginInfo(userAccount, userPwd);
            if (loginInfoMap != null) {
                if (loginInfoMap.size() > 0) {
                    commonService.setLoginLog(loginInfoMap.get(0).get("token"),userAccount,SUCCESS);
                    return getRestResultSuccess("/rest/login","查询成功", loginInfoMap.get(0));
                } else {
                    commonService.setLoginLog(DataUtil.getToken(),userAccount,FAIL);
                    return getRestResultSuccess("/rest/login","没有登录人员信息", null);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return getRestResultFault("/rest/login","查询出错，请重新查询");
    }
    //注册
    @RequestMapping(value = "/rest/register", method = {RequestMethod.POST})
    public RestResult register(HttpServletRequest httpServletRequest) {
        String phone = httpServletRequest.getParameter("phone");
        String userPwd = httpServletRequest.getParameter("userpwd");
        String email = httpServletRequest.getParameter("email");
        String userName = httpServletRequest.getParameter("username");
        log.info("/rest/register接口输入参数：{phone = "+phone+",userpwd = "+userPwd+",email = "+email+",username = "+userName+"}");
        if(commonService.hasEmail(email)){
            return getRestResultFault("/rest/register","邮箱已被注册，请重新注册");
        }
        if(commonService.hasPhone(phone)){
            return getRestResultFault("/rest/register","手机号已被注册，请重新注册");
        }
        boolean result = commonService.register(userName, userPwd,phone,email);
        if (result){
            JSONObject jsonObject = new JSONObject();
            String userId = commonService.getUserId(phone);
            jsonObject.put("userEmail",email);
            jsonObject.put("userId",userId);
            jsonObject.put("userName",userName);
            jsonObject.put("userPhone",phone);
            commonService.setOpLog(userId,DataUtil.getToken(),REGISTRATION_MODULE,"注册",EMPTY,SUCCESS);
            return getRestResultSuccess("/rest/register","注册成功", jsonObject);
        }
        commonService.setOpLog(EMPTY,DataUtil.getToken(),REGISTRATION_MODULE,"注册",EMPTY,FAIL);
        return getRestResultFault("/rest/register","注册出错，请重新注册");
    }
    //密码重置
    @RequestMapping(value = "/rest/pwdreset", method = {RequestMethod.POST})
    public RestResult pwdreset(HttpServletRequest httpServletRequest) {
        String phone = httpServletRequest.getParameter("phone");
        String userPwd = httpServletRequest.getParameter("userpwd");
        log.info("/rest/pwdreset接口输入参数：{phone = "+phone+",userpwd = "+userPwd+"}");
        Integer phoneCount = commonService.getPhoneCount(phone);
        if(phoneCount==0){
            commonService.setOpLog(EMPTY,EMPTY,REGISTRATION_MODULE,RESET_PASSWORD,"没有查询到手机号",FAIL);
            return getRestResultFault("/rest/pwdreset","重置出错,没有查询到手机号");
        }
        boolean result = commonService.resetPwd(phone,userPwd);
        if (result){
            String userId = commonService.getUserId(phone);
            commonService.setOpLog(userId,EMPTY,REGISTRATION_MODULE,RESET_PASSWORD,phone,SUCCESS);
            return getRestResultSuccess("/rest/pwdreset","密码重置成功", EMPTY);
        }
        commonService.setOpLog(EMPTY,DataUtil.getToken(),REGISTRATION_MODULE,RESET_PASSWORD,"修改密码失败",FAIL);
        return getRestResultFault("/rest/pwdreset","修改密码失败");
    }
    //操作日志
    @RequestMapping(value = "/rest/saveoplog", method = {RequestMethod.POST})
    public RestResult saveoplog(HttpServletRequest httpServletRequest) {
        String userId = httpServletRequest.getParameter("userId");
        String token = httpServletRequest.getParameter("token");
        String typeName = httpServletRequest.getParameter("typeName");
        String objectName = httpServletRequest.getParameter("objectName");
        String message = httpServletRequest.getParameter("message");
        log.info("/rest/saveoplog接口输入参数：{userId = "+userId+",token = "+token+",typeName = "+typeName+",objectName = "+objectName+",message = "+message+"}");
        boolean result = commonService.setOpLog(userId,token,typeName,objectName,message,SUCCESS);
        if (result){
            return getRestResultSuccess("/rest/saveoplog","保存成功", EMPTY);
        }
        return getRestResultFault("/rest/saveoplog","保存失败");
    }
    //登出日志
    @RequestMapping(value = "/rest/logout", method = {RequestMethod.POST})
    public RestResult logout(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getParameter("token");
        log.info("/rest/logout接口输入参数：{token = "+token+"}");
        boolean result = commonService.setLogoutLog(token);
        if(result){
            return getRestResultSuccess("/rest/saveLogoutLog","登出成功", EMPTY);
        }
        return getRestResultFault("/rest/saveLogoutLog","登出保存失败");
    }
    //地市
    @RequestMapping(value = "/rest/getRegion", method = {RequestMethod.GET})
    public RestResult getRegion(String type) {
        log.info("/rest/getRegion接口输入参数：{type = "+type+"}");
        if (type == null || EMPTY.equals(type)) return getRestResultFault("/rest/getRegion","参数错误，请重新查询");

        try {
            List<ImmutableMap<String, String>> regionInfoMap = commonService.getRegionInfo(type);
            if (regionInfoMap != null) {
                if (regionInfoMap.size() > 0) {
                    return getRestResultSuccess("/rest/getRegion","查询成功", regionInfoMap);
                } else {
                    return getRestResultSuccess("/rest/getRegion","没有地市信息", null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getRestResultFault("/rest/getRegion","查询出错，请重新查询");
    }

    //资讯
    @RequestMapping(value = "/rest/getIntelligence", method = {RequestMethod.GET})
    public RestResult getIntelligence(String maxNumber,String start,String type){
        log.info("/rest/getIntelligence接口输入参数：{maxNumber = "+maxNumber+",start = "+start+",type = "+type+"}");
        if (maxNumber == null || EMPTY.equals(maxNumber)){
            maxNumber = "5";
        }
        try {
            List<ImmutableMap<String, Object>> newsInfoMap = commonService.getIntelligenceInfo(maxNumber,start,type);
                if (newsInfoMap.size() > 0) {
                    Integer size = commonService.getIntelligenceSize(type);
                    return new RestResult("success","查询成功",size, newsInfoMap);
                } else {
                    return getRestResultSuccess("/rest/getIntelligence","没有资讯信息", null);
                }
        }catch (Exception e){
            e.printStackTrace();
        }
        return getRestResultFault("/rest/getIntelligence","查询出错，请重新查询");
    }

    //版本检查
    @RequestMapping(value = "/rest/checkVersion", method = {RequestMethod.POST})
    public RestResult checkVersion(HttpServletRequest httpServletRequest){
        String version = httpServletRequest.getParameter("version");
        log.info("/rest/checkVersion接口输入参数：{version = "+version+"}");
        try {
            List<ImmutableMap<String, String>> versionInfoMap = commonService.checkVersion();
            if (versionInfoMap != null && versionInfoMap.size() > 0) {
                if(version.equals(versionInfoMap.get(0).get("versionCode"))){
                    return getRestResultSuccess("/rest/checkVersion","最新版本，无需更新版本",null);
                }else{
                    return getRestResultSuccess("/rest/checkVersion","您的版本不是最新版本，是否要更新到最新版本", appUrl);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return getRestResultFault("/rest/checkVersion","查询版本信息失败，请更新最新版本");
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
