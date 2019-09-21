package com.inspur.bjzx.scenesecurityserve.service.impl;

import com.inspur.bjzx.scenesecurityserve.service.StationFaultService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class StationFaultServiceImpl implements StationFaultService {




    @Autowired
    @Qualifier("primaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate secondaryJdbcTemplate;

    private static final Logger log = Logger.getLogger(StationFaultServiceImpl.class);


    //查询性能工单数量——根据userAccount查询
    public int getStationFaultNum(String userAccount){
        String sql = "select count(distinct(pm_region_faultwo_pb_hour.workorder_no)) as \"count\" from pm_region_faultwo_pb_hour,sce_region_cell,sce_bz_scene_total,EME_STAFF_INFO\n" +
                "where sce_region_cell.site_no = pm_region_faultwo_pb_hour.site_no\n" +
                "and sce_region_cell.cj_name = sce_bz_scene_total.bz_name\n" +
                "and EME_STAFF_INFO.LR_BZ_ID = sce_bz_scene_total.bz_id\n" +
                "and EME_STAFF_INFO.LR_REGION_ID = sce_bz_scene_total.region_id\n" +
                "and EME_STAFF_INFO.LR_REGION_ID = sce_region_cell.region_id " +
//                "and EME_STAFF_INFO.AD_ACCOUNT='"+userAccount+"' "+
                "and EME_STAFF_INFO.AD_ACCOUNT=? "+
                "and pm_region_faultwo_pb_hour.is_ok = 1\n" +
                "AND SCE_REGION_CELL.LONGITUDE IS NOT NULL\n" +
                "AND SCE_REGION_CELL.LATITUDE IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.SITE_TYPE IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.NE_TYPE IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.WORKORDER_NO IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.SITE_NAME IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.SITE_NO IS NOT NULL\n"+
                "AND SCE_REGION_CELL.REGION_NAME IS NOT NULL";
        log.info(sql);
//        int num  = jdbcTemplate.queryForObject(sql,Integer.class);

        return jdbcTemplate.queryForObject(sql,Integer.class,userAccount);
    }


    //查询性能工单数量——全部
    public int getStationFaultNumAll(){
        String sql = "select count(distinct SHEET_ID)from T_STATION_BREAKEN_THREE\n ";
        log.info(sql);
        //return secondaryJdbcTemplate.queryForObject(sql,Integer.class);
        return 0;
    }


    //查询性能工单列表——全部
    public List<Map<String,Object>> getDetail(){
        String sql="SELECT distinct pm_region_faultwo_pb_hour.WORKORDER_NO AS \"sheet_id\", " +
                "pm_region_faultwo_pb_hour.SITE_NAME AS \"station_name\"," +
                "pm_region_faultwo_pb_hour.SITE_NO AS \"station_id\"," +
                "pm_region_faultwo_pb_hour.NE_TYPE AS \"station_type\"," +
                "pm_region_faultwo_pb_hour.CURRENT_GROUP AS \"sheet_current_department\"," +
                "pm_region_faultwo_pb_hour.SITE_TYPE AS \"honeycomb_type\"," +
                "pm_region_faultwo_pb_hour.OWNER_DEPARTMENT AS \"fgs\"," +
                "sce_region_cell.LONGITUDE AS \"longitude\"," +
                "sce_region_cell.LATITUDE AS \"latitude\", " +
                "SCE_REGION_CELL.REGION_NAME AS \"region_name\",\n" +
                "T_STATION_BREAKEN.SHEET_ID AS \"isAssociated\" "+
                "FROM (pm_region_faultwo_pb_hour \n" +
                "left join T_STATION_BREAKEN@zsyw4db \n" +
                "on t_station_breaken.station_id=pm_region_faultwo_pb_hour.site_no)\n" +
                "left join sce_region_cell on sce_region_cell.site_no = pm_region_faultwo_pb_hour.site_no " +
                "where is_ok=1\n"+
                "AND SCE_REGION_CELL.LONGITUDE IS NOT NULL\n" +
                "AND SCE_REGION_CELL.LATITUDE IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.SITE_TYPE IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.NE_TYPE IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.WORKORDER_NO IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.SITE_NAME IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.SITE_NO IS NOT NULL\n"+
                "AND SCE_REGION_CELL.REGION_NAME IS NOT NULL";
        log.info(sql);
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        log.info(list);
        return list;
    }

    //查询性能工单列表——根据Id查询
    public List<Map<String,Object>> getDetail(String id){
        String sql="SELECT distinct pm_region_faultwo_pb_hour.WORKORDER_NO AS \"sheet_id\", " +
                "pm_region_faultwo_pb_hour.SITE_NAME AS \"station_name\"," +
                "pm_region_faultwo_pb_hour.SITE_NO AS \"station_id\"," +
                "pm_region_faultwo_pb_hour.NE_TYPE AS \"station_type\"," +
                "pm_region_faultwo_pb_hour.CURRENT_GROUP AS \"sheet_current_department\"," +
                "pm_region_faultwo_pb_hour.SITE_TYPE AS \"honeycomb_type\"," +
                "pm_region_faultwo_pb_hour.OWNER_DEPARTMENT AS \"fgs\"," +
                "sce_region_cell.LONGITUDE AS \"longitude\"," +
                "sce_region_cell.LATITUDE AS \"latitude\", " +
                "SCE_REGION_CELL.REGION_NAME AS \"region_name\",\n" +
                "T_STATION_BREAKEN.SHEET_ID AS \"isAssociated\" "+
                "FROM (pm_region_faultwo_pb_hour \n" +
                "left join T_STATION_BREAKEN@zsyw4db \n" +
                "on t_station_breaken.station_id=pm_region_faultwo_pb_hour.site_no)\n" +
                "left join sce_region_cell on sce_region_cell.site_no = pm_region_faultwo_pb_hour.site_no " +
                "where is_ok=1 " +
//                "and WORKORDER_NO='"+id +"'\n"+
                "and WORKORDER_NO=?\n"+
                "AND SCE_REGION_CELL.LONGITUDE IS NOT NULL\n" +
                "AND SCE_REGION_CELL.LATITUDE IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.SITE_TYPE IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.NE_TYPE IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.WORKORDER_NO IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.SITE_NAME IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.SITE_NO IS NOT NULL\n" +
                "AND SCE_REGION_CELL.REGION_NAME IS NOT NULL\n";
        log.info(sql);
//        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,id);
        log.info(list);
        return list;
    }


    //查询性能工单列表——根据UserAccount查询
    public List<Map<String,Object>> getDetailByUserAccount(String userAccount){
        String sql="SELECT distinct pm_region_faultwo_pb_hour.WORKORDER_NO AS \"sheet_id\", " +
                "pm_region_faultwo_pb_hour.SITE_NAME AS \"station_name\"," +
                "pm_region_faultwo_pb_hour.SITE_NO AS \"station_id\"," +
                "pm_region_faultwo_pb_hour.NE_TYPE AS \"station_type\"," +
                "pm_region_faultwo_pb_hour.CURRENT_GROUP AS \"sheet_current_department\"," +
                "pm_region_faultwo_pb_hour.SITE_TYPE AS \"honeycomb_type\"," +
                "pm_region_faultwo_pb_hour.OWNER_DEPARTMENT AS \"fgs\"," +
                "sce_region_cell.LONGITUDE AS \"longitude\"," +
                "sce_region_cell.LATITUDE AS \"latitude\", " +
                "SCE_REGION_CELL.REGION_NAME AS \"region_name\",\n" +
                "T_STATION_BREAKEN.SHEET_ID AS \"isAssociated\" "+
                "from pm_region_faultwo_pb_hour left join T_STATION_BREAKEN@zsyw4db on t_station_breaken.station_id=pm_region_faultwo_pb_hour.site_no,sce_region_cell,sce_bz_scene_total,EME_STAFF_INFO\n" +
                "where sce_region_cell.site_no = pm_region_faultwo_pb_hour.site_no\n" +
                "and sce_region_cell.cj_name = sce_bz_scene_total.bz_name\n" +
                "and EME_STAFF_INFO.LR_BZ_ID = sce_bz_scene_total.bz_id\n" +
                "and EME_STAFF_INFO.LR_REGION_ID = sce_bz_scene_total.region_id\n" +
                "and EME_STAFF_INFO.LR_REGION_ID = sce_region_cell.region_id " +
//                "and EME_STAFF_INFO.AD_ACCOUNT='"+userAccount+"' "+
                "and EME_STAFF_INFO.AD_ACCOUNT=? \n"+
                "and pm_region_faultwo_pb_hour.is_ok = 1\n"+
                "AND SCE_REGION_CELL.LONGITUDE IS NOT NULL\n" +
                "AND SCE_REGION_CELL.LATITUDE IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.SITE_TYPE IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.NE_TYPE IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.WORKORDER_NO IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.SITE_NAME IS NOT NULL\n" +
                "AND PM_REGION_FAULTWO_PB_HOUR.SITE_NO IS NOT NULL\n" +
                "AND SCE_REGION_CELL.REGION_NAME IS NOT NULL\n";
        log.info(sql);
//        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,userAccount);
        log.info(list);
        return list;
    }

}
