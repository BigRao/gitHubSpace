package com.inspur.bjzx.scenesecurityserve.filter;

import com.inspur.bjzx.scenesecurityserve.util.Base64Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private Map<String, String[]> params = new HashMap<String, String[]>();

    public MyHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.params.putAll(request.getParameterMap());
        System.out.println("requestmap=====" + params);
    }


    @Override
    public String getParameter(String name) {//重写getParameter()
        String[] ans = params.get(name);
        if (ans == null || ans.length == 0) {
            return null;
        } else {
            if ("".equals(ans[0])) {
                return "";
            }
            System.out.println("wrap" + ans[0]);
            //System.err.println(Base64Util.decoder(ans[0]));
            //return Base64Util.decoder(ans[0]);
            return ans[0];
        }
    }

    public void modifyParameterValues() {//将parameter的值去除空格后重写回去


    }

    @Override
    public Enumeration<String> getParameterNames() {//重写getParameterNames()
        return new Vector<String>(params.keySet()).elements();
    }


    @Override
    public String[] getParameterValues(String name) {//重写getParameterValues()
        String[] results = params.get(name);
        if (results == null || results.length <= 0) {
            return null;
        } else {
            for (int i = 0; i < results.length; i++) {
                //if ("".equals(results[i])) {
                //    System.out.println("解码前：" + results[i]);
                    //results[i] = "";
                    System.out.println("解码后：" + results[i]);
                //} else {
                //    System.out.println("解码前：" + results[i]);
                //    results[i] = Base64Util.decoder(results[i]);
                //    System.out.println("解码后：" + results[i]);
                //}
            }
            return results;
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() { //重写getParameterMap()
        return this.params;
    }


}
