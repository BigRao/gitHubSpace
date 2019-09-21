package com.inspur.bjzx.city.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Lenovo on 2016/5/31.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestResult<T> {
    private String result;
    private String message;
    private Integer size;
    private T data;

    public RestResult(String result, String message, Integer size, T data) {
        this.result = result;
        this.message = message;
        this.size = size;
        this.data = data;
    }

    public RestResult(String result, String message, T data) {
        this.message = message;
        this.result = result;
        this.data = data;
    }

    public RestResult(String result, String message) {
        this.message = message;
        this.result = result;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RestResult{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
