package com.inspur.plugins.kaoshi.service;

import com.inspur.plugins.kaoshi.model.User;

public interface LoginService {
    Integer findUserById(String id);

    Integer findUserByPhone(String phone);

    Integer insertUser(String userId, String userName, String userPhone);

    User getUserById(String id);
}
