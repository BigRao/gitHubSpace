package com.inspur.bjzx.scenesecurityserve.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;


/** 
 * @author kouki 
 * @version 2014年4月15日 下午2:00:07 
 * 
 */

public class TimeTransformer {
	private final static Logger logger = LoggerFactory.getLogger(TimeTransformer.class);
	/**
	 * formate date to yyyy-MM-dd
	 **/
	public static String getFormate1(Date date){
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

    /**
     * formate date to yyyy-MM-dd HH:mm:ss
     **/
    public static String formate1(Date date){
		logger.info("测试-----------"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date)+"------------测试");
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
    }
    
	/**
	 * formate date to yyyy-MM-dd HH:mm:ss
	 **/
	public static String getFormate(Date date){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
}
