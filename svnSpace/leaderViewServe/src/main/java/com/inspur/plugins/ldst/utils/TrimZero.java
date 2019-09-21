package com.inspur.plugins.ldst.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrimZero {
    TrimZero() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("TrimZeroClass");
    }
    private static final String REGEX_1 = "0+?$";
    private static final String REGEX_2 = "[.]$";
    public static String format(String s) {
        if (s.contains(".")) {
            // 去掉多余的0
            s = s.replaceAll(REGEX_1, "");
            // 如最后一位是.则去掉
            s = s.replaceAll(REGEX_2, "");
        }
        return s;
    }
}
