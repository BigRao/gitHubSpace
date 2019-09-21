package com.inspur.plugins.ldst.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WildCard {
    WildCard() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("WildCardClass");
    }

    public static String withWildCard(Object a) {
        return a+"%";
    }

}
