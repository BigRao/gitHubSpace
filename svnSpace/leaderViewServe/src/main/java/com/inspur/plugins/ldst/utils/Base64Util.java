package com.inspur.plugins.ldst.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Base64;
public class Base64Util {
    private static Logger logger = LoggerFactory.getLogger(Base64Util.class);
    Base64Util() {
        logger.info("Base64UtilClass");
    }


    public static String decoder(String str) {
        try {
            // BASE64解密
            byte[] bytes = Base64.getDecoder().decode(str);
            return new String(bytes);
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            return "";
        }

    }

}
