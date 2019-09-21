package com.inspur.plugins.ldst.model;

import com.alibaba.fastjson.annotation.JSONType;

@JSONType(orders = {"id","name","value","oversee","kpiCategory","standard","netValue"})
public class CompanySortKpiVO {
    private String id;
    private String name;
    private String value;
    private String kpiCategory;
    private String standard;
    private String netValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKpiCategory() {
        return kpiCategory;
    }

    public void setKpiCategory(String kpiCategory) {
        this.kpiCategory = "代维巡检效益".equals(kpiCategory)?"代维效益":kpiCategory;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getNetValue() {
        return netValue;
    }

    public void setNetValue(String netValue) {
        this.netValue = netValue;
    }

}
