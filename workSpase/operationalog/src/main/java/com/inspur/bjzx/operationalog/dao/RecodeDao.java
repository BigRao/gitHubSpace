package com.inspur.bjzx.operationalog.dao;


import com.inspur.bjzx.operationalog.model.Recode;
import org.apache.ibatis.annotations.Mapper;

/**
 * DAO 接口类
 *
 * Created by bysocket on 07/02/2017.
 */
@Mapper
public interface RecodeDao {

    int setRecodeLogs(Recode recode);
}
