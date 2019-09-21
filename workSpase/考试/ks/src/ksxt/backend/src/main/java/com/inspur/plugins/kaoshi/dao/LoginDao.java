package com.inspur.plugins.kaoshi.dao;

import com.inspur.plugins.kaoshi.model.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;


@Repository
public class LoginDao {
    @Resource
    JdbcTemplate jdbcTemplate;

    public int insertUser(String userId, String userName, String userPhone) {
        String sql = "insert into KS_USER(ID, NAME, PHONE, REGISTRATION_TIME) values (?,?,?,?)";
        return jdbcTemplate.update(sql, userId, userName, userPhone, new Date());
    }

    public int selectUserById(String id) {
        String sql = "select count(*) from KS_USER where ID=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
    }

    public int selectUserByPhone(String phone) {
        String sql = "select count(*) from KS_USER where PHONE=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{phone}, Integer.class);
    }

    public User getUserById(String id) {
        String sql = "select * from KS_USER where ID=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(User.class));
    }
}
