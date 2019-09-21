package com.inspur.plugins.ldst.model;

import com.alibaba.fastjson.annotation.JSONType;

@JSONType(orders = {"result","message","data"})
public class ManyTimeSearchVO {
    private String result;
    private String message;
    private CompanyTimeSearchDataVO data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CompanyTimeSearchDataVO getData() {
        return data;
    }

    public void setData(CompanyTimeSearchDataVO data) {
        this.data = data;
    }
}
