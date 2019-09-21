package com.inspur.plugins.kaoshi.model;

import java.util.Date;

public class User {
    String id ;
    String name;
    String phone;
    Date registration_Time;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getRegistration_Time() {
        return registration_Time;
    }

    public void setRegistration_Time(Date registration_Time) {
        this.registration_Time = registration_Time;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", registration_Time=" + registration_Time +
                '}';
    }
}
