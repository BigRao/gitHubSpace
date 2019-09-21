package com.inspur.plugins.ldst.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DataUtil {

    DataUtil() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("DataUtilClass");
    }

    public static String o2S(Object object){
        return String.valueOf(object);
    }
    public static Integer getInteger(Map<String, String> num, String str){
        return Integer.parseInt(StringUtils.isNotBlank(num.get(str))?num.get(str):"0");
    }
    public static Double getDouble(Map<String, String> num,String str){
        return Double.parseDouble(StringUtils.isNotBlank(num.get(str))?num.get(str):"0");
    }
    public static String getString(Map<String, String> num, String str){
        return num.get(str)!=null?num.get(str):"0";
    }
}
