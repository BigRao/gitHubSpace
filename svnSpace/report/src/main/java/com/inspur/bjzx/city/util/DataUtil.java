package com.inspur.bjzx.city.util;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtil {
    public static Boolean isEmail(String str) {
        Boolean isEmail = false;
        String expr = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})$";

        if (str.matches(expr)) {
            isEmail = true;
        }
        return isEmail;
    }

    public static Boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    public static String O2S(Object object) {
        return String.valueOf(object);
    }

    public static Integer O2I(Object object) {
        return Integer.parseInt(String.valueOf(object));
    }

    public static String getToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
