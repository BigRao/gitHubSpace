package com.inspur.plugins.ldst.dao;

import com.inspur.plugins.ldst.model.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PersonDao {
    Person getPhoneByName(@Param("name") String name);
}
