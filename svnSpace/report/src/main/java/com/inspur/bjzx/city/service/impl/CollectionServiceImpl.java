package com.inspur.bjzx.city.service.impl;

import com.inspur.bjzx.city.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by liurui on 2017/8/3.
 */

@Service
public class CollectionServiceImpl implements CollectionService{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public boolean attentionKpi(String userId, String mataKpiId, String opType) {
        String sql;
        if(("ADD").equals(opType)){
            sql = "INSERT INTO PALM_ATTENTION_KPI(ID, MATAKPI_ID, USER_ID, CREATE_TIME) VALUES (attention_id.nextval, ?, ? ,SYSDATE)";
        }else {
            sql = "DELETE FROM PALM_ATTENTION_KPI WHERE MATAKPI_ID = ? AND USER_ID = ?";
        }
        int isAttentionKpi = jdbcTemplate.update(sql, mataKpiId, userId);
        return isAttentionKpi > 0;
    }
}
