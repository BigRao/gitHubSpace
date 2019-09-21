package com.inspur.plugins.ldst.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IndicateWeekDao {
    Map<String, String> getByName(@Param("name") String name, @Param("monday") String monday, @Param("sunday") String sunday);

    List<Map<String, String>> getByTime(@Param("name") String name, @Param("city") String city, @Param("endTime") String endTime, @Param("startTime") String startTime);
}
