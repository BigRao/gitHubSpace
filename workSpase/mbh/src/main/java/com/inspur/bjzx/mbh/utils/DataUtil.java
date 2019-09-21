package com.inspur.bjzx.mbh.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class DataUtil {
    public static Integer O2I(Object object){
        return Integer.parseInt(String.valueOf(object));
    }
    public static String O2S(Object object){
        return String.valueOf(object);
    }
    public static boolean notNull(String str){
        return StringUtils.isNotBlank(str) && !"null".equals(str);
    }
    public static Integer getInteger(Map<String, Object> map, String str){
        return Integer.parseInt(String.valueOf(map.get(str)));
    }
    public static String getString(Map<String, Object> map, String str){
        if (map==null)return "0";
        return map.get(str).toString() != null && map.get(str) != "" ? map.get(str).toString().trim() : "0";
    }
    public static int judge15(int i){
        if(i<15){
            return 0;
        }else if(i<30){
            return 15;
        }else if(i<45){
            return 30;
        }else {
            return 45;
        }
    }
    public static int judge5(int i){
        if(i<5){
            return 0;
        }else if(i<10){
            return 5;
        }else if(i<15){
            return 10;
        }else if(i<20){
            return 15;
        }else if(i<25){
            return 20;
        }else if(i<30){
            return 25;
        }else if(i<35){
            return 20;
        }else if(i<40){
            return 35;
        }else if(i<45){
            return 40;
        }else if(i<50){
            return 45;
        }else if(i<55){
            return 50;
        }else {
            return 55;
        }
    }
}
