package com.inspur.plugins.ldst.model;

import com.alibaba.fastjson.annotation.JSONType;
import com.inspur.plugins.ldst.utils.TrimZero;

@JSONType(orders = {"category","company","data","plmnComplaint","hwnNum"})
public class CompanyComplanintDataVO {
    private String category;
    private String company;
    private String plmnComplaint;
    private String hwnNum;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPlmnComplaint() {
        return plmnComplaint;
    }

    public void setPlmnComplaint(String plmnComplaint) {
        this.plmnComplaint = TrimZero.format(plmnComplaint);
    }

    public String getHwnNum() {
        return hwnNum;
    }

    public void setHwnNum(String hwnNum) {
        this.hwnNum = TrimZero.format(hwnNum);
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
