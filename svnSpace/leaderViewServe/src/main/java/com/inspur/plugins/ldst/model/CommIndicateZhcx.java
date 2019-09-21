package com.inspur.plugins.ldst.model;


public class CommIndicateZhcx {
    private Long indicId;
    private String indicName;
    private String indicNameAlias;
    private String vendorIds;
    private String spaceDesc;
    private Long objectClass;
    private String tableName;
    private String columnName;
    private String condition;
    private Long indicType;
    private String indicUnit;
    private Long timeInterval;
    private String keyField;
    private String startTime;
    private Long isLv;
    private String cityField;
    private Long isFloat;
    private String columnGroup;
    private String fieldName;
    private String columnRc;
    private String columnJsj;
    private String columnJz;
    private String columnD;
    private String columnW;
    private String columnM;
    private String columnY;
    private String neIndicId;
    private String vendorCol;
    private String colSource;
    private String note;
    private String influxDbname;

    public Long getIndicId() {
        return indicId;
    }

    public void setIndicId(Long indicId) {
        this.indicId = indicId;
    }

    public String getIndicName() {
        return indicName;
    }

    public void setIndicName(String indicName) {
        this.indicName = indicName;
    }

    public String getIndicNameAlias() {
        return indicNameAlias;
    }

    public void setIndicNameAlias(String indicNameAlias) {
        this.indicNameAlias = indicNameAlias;
    }

    public void setVendorIds(String vendorIds) {
        this.vendorIds = vendorIds;
    }

    public void setSpaceDesc(String spaceDesc) {
        this.spaceDesc = spaceDesc;
    }

    public void setObjectClass(Long objectClass) {
        this.objectClass = objectClass;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setIndicType(Long indicType) {
        this.indicType = indicType;
    }

    public String getIndicUnit() {
        return indicUnit;
    }

    public void setIndicUnit(String indicUnit) {
        this.indicUnit = indicUnit;
    }

    public Long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(Long timeInterval) {
        this.timeInterval = timeInterval;
    }

    public void setKeyField(String keyField) {
        this.keyField = keyField;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setIsLv(Long isLv) {
        this.isLv = isLv;
    }

    public void setCityField(String cityField) {
        this.cityField = cityField;
    }

    public void setIsFloat(Long isFloat) {
        this.isFloat = isFloat;
    }

    public void setColumnGroup(String columnGroup) {
        this.columnGroup = columnGroup;
    }


    @Override
    public String toString() {
        return "{" +
                "indicId=" + indicId +
                ", indicName='" + indicName + '\'' +
                ", indicNameAlias='" + indicNameAlias + '\'' +
                ", vendorIds='" + vendorIds + '\'' +
                ", spaceDesc='" + spaceDesc + '\'' +
                ", objectClass=" + objectClass +
                ", tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", condition='" + condition + '\'' +
                ", indicType=" + indicType +
                ", indicUnit='" + indicUnit + '\'' +
                ", timeInterval=" + timeInterval +
                ", keyField='" + keyField + '\'' +
                ", startTime='" + startTime + '\'' +
                ", isLv=" + isLv +
                ", cityField='" + cityField + '\'' +
                ", isFloat=" + isFloat +
                ", columnGroup='" + columnGroup + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", columnRc='" + columnRc + '\'' +
                ", columnJsj='" + columnJsj + '\'' +
                ", columnJz='" + columnJz + '\'' +
                ", columnD='" + columnD + '\'' +
                ", columnW='" + columnW + '\'' +
                ", columnM='" + columnM + '\'' +
                ", columnY='" + columnY + '\'' +
                ", neIndicId='" + neIndicId + '\'' +
                ", vendorCol='" + vendorCol + '\'' +
                ", colSource='" + colSource + '\'' +
                ", note='" + note + '\'' +
                ", influxDbname='" + influxDbname + '\'' +
                '}';
    }
}
