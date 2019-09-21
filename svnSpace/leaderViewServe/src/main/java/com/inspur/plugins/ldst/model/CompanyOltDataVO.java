package com.inspur.plugins.ldst.model;

import com.alibaba.fastjson.annotation.JSONType;
import com.inspur.plugins.ldst.utils.TrimZero;
import org.apache.commons.lang.StringUtils;

@JSONType(orders = {"category","company","oltBack","oltBackProportion","oltNum"})
public class CompanyOltDataVO {

    private String category;
    private String company;
    private String oltBack;
    private String oltBackProportion;
    private String oltNum;



    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOltBack() {
        return oltBack;
    }

    public void setOltBack(String oltBack) {
        this.oltBack = !StringUtils.isNotBlank(oltBack)?"0": TrimZero.format(oltBack);
    }

    public String getOltNum() {
        return oltNum;
    }

    public void setOltNum(String oltNum) {
        this.oltNum = !StringUtils.isNotBlank(oltNum)?"0": TrimZero.format(oltNum);
    }

    public String getOltBackProportion() {
        return oltBackProportion;
    }

    public void setOltBackProportion(String oltBackProportion) {
        this.oltBackProportion = !StringUtils.isNotBlank(oltBackProportion)?"0": TrimZero.format(oltBackProportion);
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }


}
