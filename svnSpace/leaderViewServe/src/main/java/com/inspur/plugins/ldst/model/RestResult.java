package com.inspur.plugins.ldst.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestResult<T> {
    private final String result;
    private final String message;
    private T data;

    public RestResult(String result, String message, T data) {
        this.message = message;
        this.result = result;
        this.data = data;
    }

    public RestResult(String result, String message) {
        this.message = message;
        this.result = result;
    }

    @Override
    public String toString() {
        return "{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
