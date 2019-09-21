package com.inspur.bjzx.mbh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;

@EnableTransactionManagement()
@MapperScan("com.inspur.bjzx.mbh.dao")
@SpringBootApplication
@ServletComponentScan(basePackages = {"com.inspur.bjzx.mbh.filter"})
public class MbhApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MbhApplication.class, args);
    }

}
