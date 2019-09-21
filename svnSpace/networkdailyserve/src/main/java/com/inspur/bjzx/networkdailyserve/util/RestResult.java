package com.inspur.bjzx.networkdailyserve.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Lenovo on 2016/5/31.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestResult<T> {
    private String result;
    private String message;
    private String pageCount;
    private T data;

    public RestResult(String result, String message, String pageCount, T data) {
        this.message = message;
        this.pageCount = pageCount;
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

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }
}
