package com.inspur.bjzx.scenesecurityserve.util;


import org.apache.log4j.Logger;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class MyControllerAdvice {

    private static Logger logger = Logger.getLogger(MyControllerAdvice.class);

    @ResponseBody
    @ExceptionHandler(value=CannotGetJdbcConnectionException.class)
    public RestResult CannotGetJdbcConnectionExceptionHandler(CannotGetJdbcConnectionException e){
        e.printStackTrace();
        return new RestResult("false","数据库连接异常");
    }
}
