package com.inspur.plugins.ldst.utils;

public class UnknownCompanyCategoryException extends Exception{
    private final String value;
    

    public UnknownCompanyCategoryException(String message, String value) {
        super(message);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
