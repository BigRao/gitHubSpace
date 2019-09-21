package com.inspur.bjzx.scenesecurityserve.util;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by Lenovo on 2017/4/24.
 */
@Configuration
public class DataSourceConfig {
    @Bean(name = "primaryDataSource")
    @Qualifier("primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "secondaryDataSource")
    @Qualifier("secondaryDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("primaryDataSource")DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "primaryNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate primaryNamedParameterJdbcTemplate(@Qualifier("primaryDataSource")DataSource dataSource){
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean(name = "secondaryJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(@Qualifier("secondaryDataSource")DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "secondaryNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate secondaryNamedParameterJdbcTemplate(@Qualifier("secondaryDataSource")DataSource dataSource){
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
