package com.inspur.bjzx.scenesecurityserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
//@ServletComponentScan(basePackages = {"com.inspur.bjzx.scenesecurityserve.filter"})
public class ScenesecurityserveApplication extends SpringBootServletInitializer implements WebApplicationInitializer {


    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("-1");
        factory.setMaxRequestSize("-1");
        return factory.createMultipartConfig();
    }
    public static void main(String[] args) {
        SpringApplication.run(ScenesecurityserveApplication.class, args);
    }

}

