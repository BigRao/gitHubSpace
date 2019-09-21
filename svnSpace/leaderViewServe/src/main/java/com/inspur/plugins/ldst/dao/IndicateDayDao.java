package com.inspur.plugins.ldst.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IndicateDayDao {

    Map<String, String> getByName(@Param("name") String name, @Param("time") String time);

    Map<String, String> getByName2(@Param("name") String name, @Param("time") String time);

    Map<String, String> getByToday(@Param("name") String name, @Param("time") String time);

    List<Map<String, String>> getByTime(@Param("name") String name, @Param("city") String city, @Param("endTime") String endTime, @Param("startTime") String startTime);

    List<Map<String, String>> getByCloumnName(@Param("name") String name,@Param("time") String time);

    Map<String, String> getByColumnTable(@Param("columnName") String columnName,@Param("tableName") String tableName,@Param("city") String city,@Param("time") String time);

}
