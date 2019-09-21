package com.inspur.bjzx.city.service.impl;

import com.inspur.bjzx.city.service.ShortMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ShortMessageServiceImpl implements ShortMessageService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> getPhoneCount(String phone) {
        String sql = "select count(*) as \"count\" from PALM_USER where PHONE=?";
        return jdbcTemplate.queryForMap(sql, new Object[]{phone});
    }
}
