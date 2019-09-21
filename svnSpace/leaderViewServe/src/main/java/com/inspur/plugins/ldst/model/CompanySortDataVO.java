package com.inspur.plugins.ldst.model;

import com.alibaba.fastjson.annotation.JSONType;

import java.util.List;
import java.util.Objects;

@JSONType(orders = {"companyCategory","notStandardNum","company"})
public class CompanySortDataVO implements Comparable<CompanySortDataVO> {
    private String companyCategory;
    private Integer notStandardNum;
    private List<CompanySortCompanyVO> company;





    public String getCompanyCategory() {
        return companyCategory;
    }

    public void setCompanyCategory(String companyCategory) {
        this.companyCategory = companyCategory;
    }

    public Integer getNotStandardNum() {
        return notStandardNum;
    }

    public void setNotStandardNum(Integer notStandardNum) {
        this.notStandardNum = notStandardNum;
    }

    public List<CompanySortCompanyVO> getCompany() {
        return company;
    }

    public void setCompany(List<CompanySortCompanyVO> company) {
        this.company = company;
    }


    @Override
    public int compareTo(CompanySortDataVO o) {
        return o.getNotStandardNum()-this.notStandardNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanySortDataVO that = (CompanySortDataVO) o;
        return Objects.equals(companyCategory, that.companyCategory) &&
                Objects.equals(notStandardNum, that.notStandardNum) &&
                Objects.equals(company, that.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyCategory, notStandardNum, company);
    }
}
