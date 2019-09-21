package com.inspur.plugins.ldst.service.impl;

import com.inspur.plugins.ldst.dao.PersonDao;
import com.inspur.plugins.ldst.model.Person;
import com.inspur.plugins.ldst.service.PersonService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PersonServiceImpl implements PersonService {


    @Resource
    private PersonDao personDao;


    @Override
    public String sms(String body) {

        JSONObject json = JSONObject.fromObject(body);
        String message;

        String name = json.getString("name");
        message = json.getString("message");
        Person person = new Person();
        person.setZhLabel(name);

        String phone = personDao.getPhoneByName(name).getMaintainArea();
        String peopleName = person.getZhLabel();
        return message + phone +peopleName;
    }
}
