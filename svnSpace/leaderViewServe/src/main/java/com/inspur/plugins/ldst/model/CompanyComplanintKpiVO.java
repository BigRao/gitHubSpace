package com.inspur.plugins.ldst.model;

import com.alibaba.fastjson.annotation.JSONType;

import java.util.List;
@JSONType(orders = {"kpiInfo","kpiValue"})
public class CompanyComplanintKpiVO {
    private final CompanyTimeSearchKpiInfoVO kpiInfo;
    private final List<String> kpiValue;

    public CompanyComplanintKpiVO(CompanyTimeSearchKpiInfoVO kpiInfo, List<String> kpiValue) {
        this.kpiInfo = kpiInfo;
        this.kpiValue = kpiValue;
    }

    public CompanyTimeSearchKpiInfoVO getKpiInfo() {
        return kpiInfo;
    }

    public List<String> getKpiValue() {
        return kpiValue;
    }

}
