package com.inspur.plugins.ldst;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Map;

@EnableTransactionManagement()
@MapperScan("com.inspur.plugins.ldst.dao")
@SpringBootApplication
@ServletComponentScan(basePackages = {"com.inspur.plugins.ldst.filter"})
public class LdstApplication extends SpringBootServletInitializer implements WebApplicationInitializer, CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    JdbcTemplate jdbcTemplate;
    @Resource
    DataSourceProperties dataSourceProperties;

    public static void main(String[] args) {
        SpringApplication.run(LdstApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("URL = {}" , dataSourceProperties.getUrl());
        String sql = "select count(*) \"count\" from PM_GFS_PERSON ";
        Map<String, Object> people = jdbcTemplate.queryForMap(sql);
        log.info("sql:{}",sql);
        log.info("result:{}",people);

    }
}
