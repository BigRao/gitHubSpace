package com.inspur.plugins.ldst.filter;


import com.inspur.plugins.ldst.utils.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<String, String[]> params = new HashMap<>();

    public MyHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.params.putAll(request.getParameterMap());
        logger.info("requestmap====={}",params);
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
            logger.info("wrap{}",ans[0]);
            String decoder = Base64Util.decoder(ans[0]);
            logger.error("出错了{}",decoder);
            return decoder;
        }
    }

    @Override
    public Enumeration<String> getParameterNames() {//重写getParameterNames()
        return new Vector<>(params.keySet()).elements();
    }


    @Override
    public String[] getParameterValues(String name) {//重写getParameterValues()
        String[] results = params.get(name);
        if (results == null || results.length <= 0) {
            return new String[0];
        } else {
            for (int i = 0; i < results.length; i++) {
                if ("".equals(results[i])) {
                    logger.info("解码前：{}",results[i]);
                    results[i] = "";
                    logger.info("解码后：{}",results[i]);
                } else {
                    logger.info("解码前：{}",results[i]);
                    results[i] = Base64Util.decoder(results[i]);
                    logger.info("解码后：{}",results[i]);

                }
            }
            return results;
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() { //重写getParameterMap()
        return this.params;
    }


}
