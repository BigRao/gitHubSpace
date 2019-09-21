package com.inspur.bjzx.scenesecurityserve.util;

import java.util.Base64;

public class Base64Util {
    public static String encoder(String str) {
        try {
            // BASE64加密
            String data = Base64.getEncoder().encodeToString(str.getBytes());
            System.out.println("BASE64加密：" + data);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String decoder(String str) {
        try {
            // BASE64解密
            byte[] bytes = Base64.getDecoder().decode(str);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

}
