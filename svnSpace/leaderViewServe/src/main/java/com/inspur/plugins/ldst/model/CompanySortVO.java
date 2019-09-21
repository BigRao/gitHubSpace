package com.inspur.plugins.ldst.model;

import com.alibaba.fastjson.annotation.JSONType;

import java.util.List;
@JSONType(orders = {"result","message","data"})
public class CompanySortVO {
    private String result;
    private String message;
    private List<CompanySortDataVO> data;


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

    public List<CompanySortDataVO> getData() {
        return data;
    }

    public void setData(List<CompanySortDataVO> data) {
        this.data = data;
    }
}
