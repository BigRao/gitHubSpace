package com.inspur.plugins.ldst.dao;

import com.inspur.plugins.ldst.model.CommIndicateZhcx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommIndicateZhcxDao {

    List<CommIndicateZhcx> getById(@Param("id") String id);

    List<Map<String,String>> getListById(@Param("id") String id);

    List<CommIndicateZhcx> getByList(List<String> list);

    CommIndicateZhcx getOne(@Param("columnName") String columnName);
}
