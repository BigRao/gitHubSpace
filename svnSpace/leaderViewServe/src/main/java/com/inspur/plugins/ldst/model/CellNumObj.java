package com.inspur.plugins.ldst.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CellNumObj implements Serializable {
    private String maintainArea;
    private Integer zongJi2g;
    private Integer zongJiLte;

    public String getMaintainArea() {
        return maintainArea;
    }

    public void setMaintainArea(String maintainArea) {
        this.maintainArea = maintainArea;
    }

    public Integer getZongJi2g() {
        return zongJi2g;
    }

    public void setZongJi2g(Integer zongJi2g) {
        this.zongJi2g = zongJi2g;
    }

    public Integer getZongJiLte() {
        return zongJiLte;
    }

    public void setZongJiLte(Integer zongJiLte) {
        this.zongJiLte = zongJiLte;
    }

    @Override
    public String toString() {
        return "{" +
                "maintainArea='" + maintainArea + '\'' +
                ", zongJi2g=" + zongJi2g +
                ", zongJiLte=" + zongJiLte +
                '}';
    }
}
