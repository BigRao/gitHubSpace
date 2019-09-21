package com.inspur.bjzx.operationalog.model;


import java.util.Date;

public class Recode {

    private String userId;

    private String userName;

    private String typeName;

    private String appName;

    private String moduleName;

    private String params;

    private String result;

    private Date insertTime;

    private String ip;

    public Recode(String userId, String userName, String typeName, String appName, String moduleName, String params, String result) {
        this.userId = userId;
        this.userName = userName;
        this.typeName = typeName;
        this.appName = appName;
        this.moduleName = moduleName;
        this.params = params;
        this.result = result;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "Recode{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", typeName='" + typeName + '\'' +
                ", appName='" + appName + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", params='" + params + '\'' +
                ", result='" + result + '\'' +
                ", insertTime=" + insertTime +
                ", ip='" + ip + '\'' +
                '}';
    }
}
