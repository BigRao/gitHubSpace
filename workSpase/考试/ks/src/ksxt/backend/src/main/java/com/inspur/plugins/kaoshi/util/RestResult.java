package com.inspur.plugins.kaoshi.util;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestResult<T> {
    private String result;
    private String message;
    private String totalSize;
    private T data;

    public RestResult(String result, String message, String totalSize, T data) {
        this.message = message;
        this.totalSize = totalSize;
        this.data = data;
        this.result = result;
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

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    @Override
    public String toString() {
        return "{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", totalSize='" + totalSize + '\'' +
                ", data=" + data +
                '}';
    }
}
