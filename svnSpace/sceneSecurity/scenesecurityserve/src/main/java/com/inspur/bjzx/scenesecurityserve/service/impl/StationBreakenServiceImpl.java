package com.inspur.bjzx.scenesecurityserve.service.impl;

import com.inspur.bjzx.scenesecurityserve.service.StationBreakenService;
import com.inspur.bjzx.scenesecurityserve.util.DataUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StationBreakenServiceImpl implements StationBreakenService{



    @Autowired
    @Qualifier("primaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate secondaryJdbcTemplate;

    private  static final Logger log = Logger.getLogger(StationBreakenServiceImpl.class);


    //查询故障工单数量——全部
    public int getStationBreakenNumAll(){
        String sql = "select count(distinct SHEET_ID)from T_STATION_BREAKEN_THREE\n";
        log.info(sql);
        return secondaryJdbcTemplate.queryForObject(sql,Integer.class);

    }


    //查询故障工单数量——根据userAccount关联 区域表和故障工单表，来查关联出来的
    public int getStationBreakenNum(String userAccount){
        String sql = "SELECT count(DISTINCT (T_STATION_BREAKEN.STATION_ID)) AS \"total\" FROM T_STATION_BREAKEN@zsyw4db,SCE_REGION_CELL,SCE_BZ_SCENE_TOTAL,EME_STAFF_INFO\n" +
                "WHERE SCE_REGION_CELL.SITE_NO = T_STATION_BREAKEN.STATION_ID\n" +
                "AND SCE_REGION_CELL.CJ_NAME = SCE_BZ_SCENE_TOTAL.BZ_NAME\n" +
                "AND EME_STAFF_INFO.LR_BZ_ID = SCE_BZ_SCENE_TOTAL.BZ_ID\n" +
                "AND EME_STAFF_INFO.LR_REGION_ID = SCE_BZ_SCENE_TOTAL.REGION_ID\n" +
                "AND EME_STAFF_INFO.LR_REGION_ID = SCE_REGION_CELL.REGION_ID\n" +
//                "AND EME_STAFF_INFO.AD_ACCOUNT='"+userAccount+"'\n"+
                "AND EME_STAFF_INFO.AD_ACCOUNT=? \n"+/*
                "AND LONGITUDE IS NOT NULL\n" +
                "AND LATITUDE IS NOT NULL\n" +*/
                "AND HONEYCOMB_TYPE IS NOT NULL\n" +
                "AND STATION_TYPE IS NOT NULL\n" +
                "AND SHEET_ID IS NOT NULL\n" +
                "AND STATION_NAME IS NOT NULL\n" +
                "AND STATION_ID IS NOT NULL\n"+
                "AND SCE_REGION_CELL.REGION_NAME IS NOT NULL";
        log.info(sql);
//        int num = jdbcTemplate.queryForObject(sql,Integer.class);
        return jdbcTemplate.queryForObject(sql,Integer.class,userAccount);
    }


    //查询故障工单区域数量
    public int getFaultAreaNum(){
        String sql2 = "select OUTLINE_2G_NUM,OUTLINE_4G_NUM,OUTLINE_4G_PLUS_NUM,OUTLINE_5G_NUM from T_STATION_BREAKEN_THREE_C t where time = trunc(sysdate) and t.nename = '合计'\n";
        log.info(sql2);
        Map<String, Object> map = secondaryJdbcTemplate.queryForMap(sql2);
        return
                DataUtil.getInteger(map,"OUTLINE_2G_NUM")+
                DataUtil.getInteger(map,"OUTLINE_4G_NUM")+
                DataUtil.getInteger(map,"OUTLINE_4G_PLUS_NUM")+
                DataUtil.getInteger(map,"OUTLINE_5G_NUM");
    }


    //故障工单列表查询——查全部
    public List<Map<String,Object>> getDetail(){
        String sql = "SELECT DISTINCT T_STATION_BREAKEN.SHEET_ID AS \"sheet_id\", " +
                "T_STATION_BREAKEN.STATION_NAME AS \"station_name\"," +
                "T_STATION_BREAKEN.STATION_ID AS \"station_id\"," +
                "T_STATION_BREAKEN.STATION_TYPE AS \"station_type\"," +
                "T_STATION_BREAKEN.SHEET_CURRENT_DEPARTMENT AS \"sheet_current_department\"," +
                "T_STATION_BREAKEN.HONEYCOMB_TYPE AS \"honeycomb_type\"," +
                "T_STATION_BREAKEN.STATION_MAINTENANCE_UNIT AS \"station_maintenance_unit\"," +
                "T_STATION_BREAKEN.FGS AS \"fgs\"," +
                "T_STATION_BREAKEN.LONGITUDE AS \"longitude\"," +
                "T_STATION_BREAKEN.LATITUDE AS \"latitude\", " +
                "SCE_REGION_CELL.REGION_NAME AS \"region_name\",\n"+
                "PM_REGION_FAULTWO_PB_HOUR.WORKORDER_NO AS \"isAssociated\" "+
                "FROM T_STATION_BREAKEN LEFT JOIN PM_REGION_FAULTWO_PB_HOUR ON T_STATION_BREAKEN.STATION_ID=PM_REGION_FAULTWO_PB_HOUR.SITE_NO LEFT JOIN SCE_REGION_CELL\n"+
                "ON SCE_REGION_CELL.SITE_NO = T_STATION_BREAKEN.STATION_ID\n" +
                "WHERE T_STATION_BREAKEN.LONGITUDE IS NOT NULL\n" +
                "AND T_STATION_BREAKEN.LATITUDE IS NOT NULL\n" +
                "AND T_STATION_BREAKEN.HONEYCOMB_TYPE IS NOT NULL\n" +
                "AND T_STATION_BREAKEN.STATION_TYPE IS NOT NULL\n" +
                "AND T_STATION_BREAKEN.SHEET_ID IS NOT NULL\n" +
                "AND T_STATION_BREAKEN.STATION_NAME IS NOT NULL\n" +
                "AND T_STATION_BREAKEN.STATION_ID IS NOT NULL\n" +
                "AND SCE_REGION_CELL.REGION_NAME IS NOT NULL";
        log.info(sql);
        List<Map<String,Object>> list = secondaryJdbcTemplate.queryForList(sql);
        log.info(list);
        return list;

    }


    //故障工单列表查询——根据工单Id单表查询
    public List<Map<String,Object>> getDetail(String id){
        String sql = "SELECT DISTINCT T_STATION_BREAKEN.SHEET_ID AS \"sheet_id\", " +
                "T_STATION_BREAKEN.STATION_NAME AS \"station_name\"," +
                "T_STATION_BREAKEN.STATION_ID AS \"station_id\"," +
                "T_STATION_BREAKEN.STATION_TYPE AS \"station_type\"," +
                "T_STATION_BREAKEN.SHEET_CURRENT_DEPARTMENT AS \"sheet_current_department\"," +
                "T_STATION_BREAKEN.HONEYCOMB_TYPE AS \"honeycomb_type\"," +
                "T_STATION_BREAKEN.STATION_MAINTENANCE_UNIT AS \"station_maintenance_unit\"," +
                "T_STATION_BREAKEN.FGS AS \"fgs\"," +
                "T_STATION_BREAKEN.LONGITUDE AS \"longitude\"," +
                "T_STATION_BREAKEN.LATITUDE AS \"latitude\", " +
                "SCE_REGION_CELL.REGION_NAME AS \"region_name\",\n" +
                "PM_REGION_FAULTWO_PB_HOUR.WORKORDER_NO AS \"isAssociated\" "+
                "FROM T_STATION_BREAKEN  LEFT JOIN PM_REGION_FAULTWO_PB_HOUR ON T_STATION_BREAKEN.station_id=PM_REGION_FAULTWO_PB_HOUR.SITE_NO LEFT JOIN SCE_REGION_CELL\n "+
                "ON SCE_REGION_CELL.SITE_NO = T_STATION_BREAKEN.STATION_ID\n" +
//                "WHERE T_STATION_BREAKEN.SHEET_ID= '" +id +"'\n"+
                "WHERE T_STATION_BREAKEN.SHEET_ID= ?\n"+
                "AND T_STATION_BREAKEN.LONGITUDE IS NOT NULL\n" +
                "AND T_STATION_BREAKEN.LATITUDE IS NOT NULL\n" +
                "AND T_STATION_BREAKEN.HONEYCOMB_TYPE IS NOT NULL\n" +
                "AND T_STATION_BREAKEN.STATION_TYPE IS NOT NULL\n" +
                "AND T_STATION_BREAKEN.SHEET_ID IS NOT NULL\n" +
                "AND T_STATION_BREAKEN.STATION_NAME IS NOT NULL\n" +
                "AND T_STATION_BREAKEN.STATION_ID IS NOT NULL\n" +
                "AND SCE_REGION_CELL.REGION_NAME IS NOT NULL";
        log.info(sql);
//        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,id);
        log.info(list);
        return list;
    }

    //故障工单列表查询——根据userAccount查询
    public List<Map<String,Object>> getDetailByUserAccount(String userAccount){
        String sql = "SELECT DISTINCT T_STATION_BREAKEN.SHEET_ID AS \"sheet_id\", " +
                "T_STATION_BREAKEN.STATION_NAME AS \"station_name\"," +
                "T_STATION_BREAKEN.STATION_ID AS \"station_id\"," +
                "T_STATION_BREAKEN.STATION_TYPE AS \"station_type\"," +
                "T_STATION_BREAKEN.SHEET_CURRENT_DEPARTMENT AS \"sheet_current_department\"," +
                "T_STATION_BREAKEN.HONEYCOMB_TYPE AS \"honeycomb_type\"," +
                "T_STATION_BREAKEN.STATION_MAINTENANCE_UNIT AS \"station_maintenance_unit\"," +
                "T_STATION_BREAKEN.FGS AS \"fgs\"," +
                "T_STATION_BREAKEN.LONGITUDE AS \"longitude\"," +
                "T_STATION_BREAKEN.LATITUDE AS \"latitude\", " +
                "SCE_REGION_CELL.REGION_NAME AS \"region_name\",\n" +
                "PM_REGION_FAULTWO_PB_HOUR.WORKORDER_NO AS \"isAssociated\" "+
                "FROM T_STATION_BREAKEN LEFT JOIN PM_REGION_FAULTWO_PB_HOUR ON T_STATION_BREAKEN.STATION_ID=PM_REGION_FAULTWO_PB_HOUR.SITE_NO,SCE_REGION_CELL,SCE_BZ_SCENE_TOTAL,EME_STAFF_INFO\n" +
                "WHERE SCE_REGION_CELL.SITE_NO = T_STATION_BREAKEN.STATION_ID\n" +
                "AND SCE_REGION_CELL.CJ_NAME = SCE_BZ_SCENE_TOTAL.BZ_NAME\n" +
                "AND EME_STAFF_INFO.LR_BZ_ID = SCE_BZ_SCENE_TOTAL.BZ_ID\n" +
                "AND EME_STAFF_INFO.LR_REGION_ID = SCE_BZ_SCENE_TOTAL.REGION_ID\n" +
                "AND EME_STAFF_INFO.LR_REGION_ID = SCE_REGION_CELL.REGION_ID\n" +
                "AND EME_STAFF_INFO.AD_ACCOUNT=?\n"+
//                "AND EME_STAFF_INFO.AD_ACCOUNT='"+userAccount+"'\n"+
                "AND T_STATION_BREAKEN.LONGITUDE IS NOT NULL\n" +
                "AND T_STATION_BREAKEN.LATITUDE IS NOT NULL\n" +
                "AND T_STATION_BREAKEN.HONEYCOMB_TYPE IS NOT NULL\n" +
                "AND T_STATION_BREAKEN.STATION_TYPE IS NOT NULL\n" +
                "AND T_STATION_BREAKEN.SHEET_ID IS NOT NULL\n" +
                "AND T_STATION_BREAKEN.STATION_NAME IS NOT NULL\n" +
                "AND T_STATION_BREAKEN.STATION_ID IS NOT NULL\n" +
                "AND SCE_REGION_CELL.REGION_NAME IS NOT NULL";
        log.info(sql);
//        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,userAccount);
        log.info(list);
        return list;
    }

}
