package com.inspur.plugins.ldst.model;

import com.alibaba.fastjson.annotation.JSONType;

import java.util.List;
@JSONType(orders = {"time","city","kpis"})
public class CompanyTimeSearchDataVO {
    private List<String> time;
    private String city;
    private List<CompanyComplanintKpiVO> kpis;

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<CompanyComplanintKpiVO> getKpis() {
        return kpis;
    }

    public void setKpis(List<CompanyComplanintKpiVO> kpis) {
        this.kpis = kpis;
    }
}
