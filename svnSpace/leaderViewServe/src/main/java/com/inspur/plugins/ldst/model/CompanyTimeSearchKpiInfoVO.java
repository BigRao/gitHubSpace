package com.inspur.plugins.ldst.model;

import com.alibaba.fastjson.annotation.JSONType;

@JSONType(orders = {"kpiId","kpiName","unit","tendency"})
public class CompanyTimeSearchKpiInfoVO {
    private String kpiId;
    private String kpiName;
    private String unit;
    private String tendency;

    public String getKpiId() {
        return kpiId;
    }

    public void setKpiId(String kpiId) {
        this.kpiId = kpiId;
    }

    public String getKpiName() {
        return kpiName;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTendency() {
        return tendency;
    }

    public void setTendency(String tendency) {
        this.tendency = tendency;
    }
}
