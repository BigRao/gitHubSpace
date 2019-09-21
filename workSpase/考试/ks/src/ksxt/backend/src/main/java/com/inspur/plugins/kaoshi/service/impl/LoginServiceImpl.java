package com.inspur.plugins.kaoshi.service.impl;

import com.inspur.plugins.kaoshi.dao.LoginDao;
import com.inspur.plugins.kaoshi.model.User;
import com.inspur.plugins.kaoshi.service.LoginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginServiceImpl implements LoginService {
    private static Logger log = LogManager.getLogger(LoginServiceImpl.class);
    @Resource
    LoginDao loginDao;

    @Override
    public Integer findUserById(String id) {

        return loginDao.selectUserById(id);
    }
    @Override
    public Integer findUserByPhone(String phone) {

        return loginDao.selectUserByPhone(phone);
    }
    @Override
    public Integer insertUser(String userId,String userName,String userPhone) {
        return loginDao.insertUser(userId,userName,userPhone);
    }
    @Override
    public User getUserById(String id) {

        return loginDao.getUserById(id);
    }

}
