package com.inspur.plugins.ldst.model;

import com.alibaba.fastjson.annotation.JSONType;

import java.util.List;
import java.util.Objects;

@JSONType(orders = {"name","oversee","phone","kpi"})
public class CompanySortCompanyVO implements Comparable<CompanySortCompanyVO>{
    private String name;
    private String oversee;
    private String phone;
    private List<CompanySortKpiVO> kpi;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOversee() {
        return oversee;
    }

    public void setOversee(String oversee) {
        this.oversee = oversee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<CompanySortKpiVO> getKpi() {
        return kpi;
    }

    public void setKpi(List<CompanySortKpiVO> kpi) {
        this.kpi = kpi;
    }

    @Override
    public int compareTo(CompanySortCompanyVO o) {
        return o.kpi.size()-this.kpi.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanySortCompanyVO that = (CompanySortCompanyVO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(oversee, that.oversee) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(kpi, that.kpi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, oversee, phone, kpi);
    }
}
