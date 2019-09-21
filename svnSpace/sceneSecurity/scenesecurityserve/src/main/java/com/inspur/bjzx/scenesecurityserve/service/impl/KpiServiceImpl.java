package com.inspur.bjzx.scenesecurityserve.service.impl;

import com.inspur.bjzx.scenesecurityserve.service.KpiService;
import com.inspur.bjzx.scenesecurityserve.util.DataUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class KpiServiceImpl implements KpiService {
    private static final Logger log = Logger.getLogger(KpiServiceImpl.class);
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("secondaryNamedParameterJdbcTemplate")
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private DateTimeFormatter minuteFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public List<Map<String, Object>> searchKpi(String searchContent, String time) {
        List<Map<String,Object>> kpiList = new ArrayList<>();
        String sql = "select INDIC_UNIT \"unit\",INDIC_NAME \"kpiName\",INDIC_ID \"kpiId\"\n" +
                "from FMS_COMM_INDICATE_ZHCX\n" +
                "where INDIC_NAME like '%"+searchContent+"%'";

        List<Map<String,Object>> zbList = jdbcTemplate.queryForList(sql);
        for (Map<String,Object> zb:zbList){
            Map<String,Object> kpi = new HashMap<>();
            kpi.put("kpiInfo",zb);
            String kpiId = DataUtil.O2S(zb.get("kpiId"));
            String sql2 = "select TABLE_NAME \"tableName\",CONDITION \"condition\",COLUMN_NAME \"columnName\",\n" +
                    "       COLUMN_W \"weeksForm\",COLUMN_M \"monthsForm\",COLUMN_Y \"yearsForm\",\n" +
                    "       COLUMN_RC \"dailyCompare\"\n" +
                    "from FMS_COMM_INDICATE_ZHCX\n" +
                    "where INDIC_ID=?";
            Map<String, Object> columnInfo = jdbcTemplate.queryForMap(sql2,kpiId);
            String tableName = DataUtil.O2S(columnInfo.get("tableName"));
            if(DataUtil.O2I(judgeTableExists(tableName).get("count"))==0||"PM_WIRELESS_CITY_HOUR".equals(tableName))continue;
            String condition = DataUtil.O2S(columnInfo.get("condition"));
            String columnName = DataUtil.O2S(columnInfo.get("columnName"));
            String weeksForm = DataUtil.O2S(columnInfo.get("weeksForm"));
            String monthsForm = DataUtil.O2S(columnInfo.get("monthsForm"));
            String yearsForm = DataUtil.O2S(columnInfo.get("yearsForm"));
            String dailyCompare = DataUtil.O2S(columnInfo.get("dailyCompare"));

            StringBuilder sql3=new StringBuilder();
            sql3.append("select ").append(columnName).append(" \"kpiValue\"").append(", ");
            setSql(sql3,dailyCompare);
            sql3.append(" \"dailyCompare\"").append(", ");
            setSql(sql3,weeksForm);
            sql3.append(" \"weeksForm\"").append(", ");
            setSql(sql3,monthsForm);
            sql3.append(" \"monthsForm\"").append(", ");
            setSql(sql3,yearsForm);
            sql3.append(" \"yearsForm\"");
            sql3.append(" from ").append(tableName);
            sql3.append(" where to_char(time,'YYYY-MM-DD') = '").append(time).append("'");
            if(DataUtil.notNull(condition)){
                sql3.append(" and ").append(condition);
            }

            List<Map<String, Object>> kpiInfo = jdbcTemplate.queryForList(sql3.toString());
            kpiList.add(kpi);
            kpiList.add(kpiInfo.get(0));
        }
        return kpiList;
    }

    private Map<String, Object> judgeTableExists(String tableName){
        String sql = "select count(*) \"count\" from user_objects where  object_name =  '"+tableName+"'";

        return jdbcTemplate.queryForMap(sql);
    }

    @Override
    public List<Map<String, Object>> getAreaTwo(Map<String, Object> areaIdMap) {

        String sql = "select t.send_zl \"area_two\" from sce_region t \n" +
                "where t.region_id in (:areaIds)\n" +
                "and T.SCENE_NAME LIKE '%XW保障%' \n" +
                "union all \n" +
                "select distinct t.send_zl \"area_two\" from sce_region t\n" +
                "WHERE T.SCENE_NAME LIKE '%XW保障%' \n" +
                "and t.region_id not in (:areaIds)";
        return namedParameterJdbcTemplate.queryForList(sql, areaIdMap);
    }
    @Override
    public List<Map<String, Object>> getAreaThree(String areaTwo,Map<String, Object> areaIdMap) {
        String sql = "select t.region_id \"area_three_id\" , t.region_name \"area_three\" from sce_region t\n" +
                "WHERE T.SCENE_NAME LIKE '%XW保障%'\n" +
                "and t.send_zl='"+areaTwo+"'\n" +
                "and t.region_id in (:areaIds)\n" +
                "union all \n" +
                "select t.region_id \"area_three_id\" , t.region_name \"area_three\" from sce_region t\n" +
                "WHERE T.SCENE_NAME LIKE '%XW保障%'\n" +
                "and t.send_zl='"+areaTwo+"'\n" +
                "and t.region_id not in (:areaIds)" ;
        return namedParameterJdbcTemplate.queryForList(sql,areaIdMap);
    }

    @Override
    public List<Map<String, Object>> getBrokenStation(String area_id) {
        StringBuilder sql=new StringBuilder();
        sql.append("select * from (SELECT nename \"area\",ne_key \"area_id\",outline_2g_num \"broken_station_two\",outline_4g_num \"broken_station_four\",OUTLINE_4G_PLUS_NUM \"4G_PLUS\",outline_5g_num \"broken_station_five\" ,' ' \"abnormal_cell\",' ' \"abnormal_computer_room\"");
        sql.append("FROM T_STATION_BREAKEN_THREE_C t where nename <> '合计'");
        if("".equals(area_id)){
            sql.append("and time = (select max(time) from T_STATION_BREAKEN_THREE_C))");
            return jdbcTemplate.queryForList(sql.toString());
        }else {
            sql.append("and ne_key = ?");
            sql.append("order by t.TIME desc) where rownum = 1 ");
            return jdbcTemplate.queryForList(sql.toString(),area_id);
        }

    }

    @Override
    public Map<String, Object> getUserArea(String userNameApp) {
        String sql = "select t.scene_id from sce_person t where t.user_name_app = ?";
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql, userNameApp);
        List<Object> areaIds = new ArrayList<>();
        if(mapList.size()==0){
            areaIds.add("4660");
        }
        for (Map<String, Object> map:mapList){
            areaIds.add(map.get("scene_id"));
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("areaIds", areaIds);
        return parameters;
    }

    @Override
    public int getBrokenStationNum(String type,String regionId) {
        log.info("----station_type=="+type+",region_id=="+regionId+"----");
        String sql = "select count(*) count from t_station_breaken_three where station_type =? and region_id = ? ";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql,type,regionId);

        return DataUtil.O2I(map.get("count"));
    }

    private void setSql(StringBuilder sql,String str){
        if (DataUtil.notNull(str)){
            sql.append(str);
        }else {
            sql.append("'0'");
        }
    }
}
