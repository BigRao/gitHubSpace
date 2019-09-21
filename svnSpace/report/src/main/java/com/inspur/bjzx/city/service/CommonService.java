package com.inspur.bjzx.city.service;

import com.google.common.collect.ImmutableMap;

import java.util.List;

/**
 * Created by liurui on 2017/8/3.
 */
public interface CommonService {
    //获取登录信息
    List<ImmutableMap<String, String>> getLoginInfo(String userAccount, String userPwd);
    //获取地市信息
    List<ImmutableMap<String, String>> getRegionInfo(String type);
    //获取资讯信息
    List<ImmutableMap<String, Object>> getIntelligenceInfo(String maxNumber, String start, String type);
    //获取版本信息
    List<ImmutableMap<String, String>> checkVersion();

    boolean register(String userName, String userPwd, String phone, String email);

    Integer getPhoneCount(String phone);

    boolean hasEmail(String email);

    boolean hasPhone(String phone);

    String getUserId(String phone);

    boolean resetPwd(String phone, String userPwd);

    boolean setOpLog(String userId, String token, String typeName, String objectName, String message, String result);

    void setLoginLog(String token, String userAccount, String result);

    boolean setLogoutLog(String token);

    Integer getIntelligenceSize(String type);
}
