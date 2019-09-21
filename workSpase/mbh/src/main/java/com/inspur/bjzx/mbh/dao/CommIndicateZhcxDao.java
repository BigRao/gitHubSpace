package com.inspur.bjzx.mbh.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommIndicateZhcxDao {

    List<Map<String, Object>> getListById(List<String> list);

    Map<String, Object> getByKpiId(@Param("column") String column_name,@Param("table") String table_name,
                                   @Param("time") String firstTime);

    List<Map<String, Object>> getRankByKpiId(@Param("column") String column_name,@Param("table") String table_name,
                                             @Param("time") String firstTime);

    Map<String, Object> getMaxTime(@Param("table") String tableName);

    List<Map<String, Object>> manyTimeSearch(@Param("table") String table_name, @Param("column") String column_name,
                                             @Param("neName") String neName,
                                             @Param("timeType") String timeType, @Param("firstTime") String firstTime,
                                             @Param("lastTime") String lastTime);
}
