package com.inspur.plugins.kaoshi.util;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class Base64Util {
    public static String encoder(String str) {
        try {
            // BASE64加密
            //BASE64Encoder encoder = new BASE64Encoder();
            //String data = encoder.encode(str.getBytes());

            Encoder encoder = Base64.getEncoder();
            String data = encoder.encodeToString(str.getBytes());
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
            //BASE64Decoder decoder = new BASE64Decoder();
            //byte[] bytes = decoder.decodeBuffer(str);
            //return new String(bytes);

            Decoder decoder = Base64.getDecoder();
            byte[] bytes = decoder.decode(str);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

}
