package com.inspur.bjzx.scenesecurityserve.service.impl;

import com.google.common.collect.ImmutableMap;
import com.inspur.bjzx.scenesecurityserve.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Service
public class UserServiceImpl implements UserService{

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Override
    public List<ImmutableMap> getLonLatLimit(String longitude, String latitude, String distance) {
        float longitude1 = Float.parseFloat(longitude);
        float latitude1 = Float.parseFloat(latitude);
        double distance1 = Double.parseDouble(distance);
        //先计算查询点的经纬度范围
        double r = 6371;//地球半径千米
        double dis = distance1 / 1000;//0.5千米距离
        double dlng =  2 * Math.asin(Math.sin(dis/(2*r))/Math.cos(latitude1 * Math.PI/180));
        dlng = dlng * 180/Math.PI;//角度转为弧度
        double dlat = dis/r;
        dlat = dlat * 180/Math.PI;
        double minlat =latitude1 - dlat;
        double maxlat = latitude1 + dlat;
        double minlng = longitude1 - dlng;
        double maxlng = longitude1 + dlng;

        String sql = "SELECT c.LONGITUDE,c.LATITUDE,c.USERID,c.USERNAME,c.PHONE FROM (SELECT t.*, row_number() OVER(PARTITION BY t.USERID ORDER BY t.SIGN_TIME DESC) rn FROM PM_SIGN_INFO t) c WHERE rn = 1 AND trunc(sysdate) = trunc(SIGN_TIME) AND c.LONGITUDE >= ? AND c.LONGITUDE <= ? AND c.LATITUDE >= ? AND c.LATITUDE <= ?";
        return jdbcTemplate.query(sql, new Object[]{minlng,maxlng,minlat,maxlat}, new RowMapper<ImmutableMap>() {
            @Override
            public ImmutableMap mapRow(ResultSet resultSet, int i) throws SQLException {
                return ImmutableMap.<String,String>builder()
                        .put("longitude", StringUtils.isEmpty(resultSet.getObject("LONGITUDE")) ? "" : resultSet.getString("LONGITUDE"))
                        .put("latitude", StringUtils.isEmpty(resultSet.getObject("LATITUDE")) ? "" : resultSet.getString("LATITUDE"))
                        .put("userid", StringUtils.isEmpty(resultSet.getObject("USERID")) ? "" : resultSet.getString("USERID"))
                        .put("username", StringUtils.isEmpty(resultSet.getObject("USERNAME")) ? "" : resultSet.getString("USERNAME"))
                        .put("phone", StringUtils.isEmpty(resultSet.getObject("PHONE")) ? "" : resultSet.getString("PHONE"))
                        .build();
            }
        });
    }


    @Override
    public List<ImmutableMap<String,String>> getUserInfoTable(String depart) {
        String sql = "SELECT NAME,SEND_DEPART,TEAMS,RESPONSIBILITY,MOBILE1 FROM SCE_ZHIBAN_INFO WHERE SEND_DEPART = ? AND sysdate BETWEEN to_date(to_char(START_TIME, 'yyyy-MM-dd') || to_char(to_date(STIME, 'hh24:mi:ss'), 'hh24:mi:ss'), 'yyyy-MM-dd hh24:mi:ss') AND to_date(to_char(END_TIME, 'yyyy-MM-dd') || to_char(to_date(ETIME, 'hh24:mi:ss'), 'hh24:mi:ss'), 'yyyy-MM-dd hh24:mi:ss')";
        return jdbcTemplate.query(sql, new Object[]{depart}, new RowMapper<ImmutableMap<String,String>>() {
            @Override
            public ImmutableMap<String,String> mapRow(ResultSet resultSet, int i) throws SQLException {
                return ImmutableMap.<String,String>builder()
                        .put("username", StringUtils.isEmpty(resultSet.getObject("NAME")) ? "" : resultSet.getString("NAME"))
                        .put("depart", StringUtils.isEmpty(resultSet.getObject("SEND_DEPART")) ? "" : resultSet.getString("SEND_DEPART"))
                        .put("teams", StringUtils.isEmpty(resultSet.getObject("TEAMS")) ? "" : resultSet.getString("TEAMS"))
                        .put("responsibility", StringUtils.isEmpty(resultSet.getObject("RESPONSIBILITY")) ? "" : resultSet.getString("RESPONSIBILITY"))
                        .put("phone", StringUtils.isEmpty(resultSet.getObject("MOBILE1")) ? "" : resultSet.getString("MOBILE1"))
                        .build();
            }
        });
    }


    @Override
    public List<ImmutableMap<String, String>> getUserInfoMap(String useraccount,String longitude,String latitude) {
        String sql = "SELECT\n" +
                "    b.NAME,\n" +
                "    b.SEND_DEPART,\n" +
                "    b.TEAMS,\n" +
                "    b.RESPONSIBILITY,\n" +
                "    b.MOBILE1,\n" +
                "    c.PHONE,\n" +
                "    to_char(c.SIGN_TIME,'yyyy-MM-dd HH24:mi:ss') AS SIGN_TIME,\n" +
                "    b.USERACCOUNT\n" +
                "FROM\n" +
                "    (\n" +
                "        SELECT\n" +
                "            t.*,\n" +
                "            row_number() OVER(PARTITION BY t.USERID ORDER BY t.SIGN_TIME DESC) rn\n" +
                "        FROM\n" +
                "            PM_SIGN_INFO t) c\n" +
                "INNER JOIN\n" +
                "    (\n" +
                "        SELECT\n" +
                "            NAME,\n" +
                "            SEND_DEPART,\n" +
                "            TEAMS,\n" +
                "            RESPONSIBILITY,\n" +
                "            MOBILE1,\n" +
                "            USERACCOUNT\n" +
                "        FROM\n" +
                "            SCE_ZHIBAN_INFO\n" +
                "        GROUP BY\n" +
                "            SCE_ZHIBAN_INFO.USERACCOUNT,\n" +
                "            SCE_ZHIBAN_INFO.NAME,\n" +
                "            SCE_ZHIBAN_INFO.SEND_DEPART,\n" +
                "            SCE_ZHIBAN_INFO.TEAMS,\n" +
                "            SCE_ZHIBAN_INFO.RESPONSIBILITY,\n" +
                "            SCE_ZHIBAN_INFO.MOBILE1) b\n" +
                "ON\n" +
                "    (\n" +
                "        c.USERID = b.USERACCOUNT\n" +
                "    AND TRUNC(c.LONGITUDE,4) = ?\n" +
                "    AND TRUNC(c.LATITUDE,4) = ?\n" +
                "    AND c.rn = 1)";
        List<ImmutableMap<String, String>> list =  jdbcTemplate.query(sql, new Object[]{longitude,latitude}, new RowMapper<ImmutableMap<String,String>>() {
            @Override
            public ImmutableMap<String, String> mapRow(ResultSet resultSet, int i) throws SQLException {
                if(StringUtils.isEmpty(resultSet.getObject("MOBILE1"))){
                    return ImmutableMap.<String,String>builder()
                            .put("useraccount", StringUtils.isEmpty(resultSet.getObject("USERACCOUNT")) ? "" : resultSet.getString("USERACCOUNT"))
                            .put("username", StringUtils.isEmpty(resultSet.getObject("NAME")) ? "" : resultSet.getString("NAME"))
                            .put("depart", StringUtils.isEmpty(resultSet.getObject("SEND_DEPART")) ? "" : resultSet.getString("SEND_DEPART"))
                            .put("teams", StringUtils.isEmpty(resultSet.getObject("TEAMS")) ? "" : resultSet.getString("TEAMS"))
                            .put("responsibility", StringUtils.isEmpty(resultSet.getObject("RESPONSIBILITY")) ? "" : resultSet.getString("RESPONSIBILITY"))
                            .put("sign_time", StringUtils.isEmpty(resultSet.getObject("SIGN_TIME")) ? "" : resultSet.getString("SIGN_TIME"))
                            .put("phone", StringUtils.isEmpty(resultSet.getObject("PHONE")) ? "" : resultSet.getString("PHONE"))
                            .build();
                }else{
                    return ImmutableMap.<String,String>builder()
                            .put("useraccount", StringUtils.isEmpty(resultSet.getObject("USERACCOUNT")) ? "" : resultSet.getString("USERACCOUNT"))
                            .put("username", StringUtils.isEmpty(resultSet.getObject("NAME")) ? "" : resultSet.getString("NAME"))
                            .put("depart", StringUtils.isEmpty(resultSet.getObject("SEND_DEPART")) ? "" : resultSet.getString("SEND_DEPART"))
                            .put("teams", StringUtils.isEmpty(resultSet.getObject("TEAMS")) ? "" : resultSet.getString("TEAMS"))
                            .put("responsibility", StringUtils.isEmpty(resultSet.getObject("RESPONSIBILITY")) ? "" : resultSet.getString("RESPONSIBILITY"))
                            .put("sign_time", StringUtils.isEmpty(resultSet.getObject("SIGN_TIME")) ? "" : resultSet.getString("SIGN_TIME"))
                            .put("phone", StringUtils.isEmpty(resultSet.getObject("MOBILE1")) ? "" : resultSet.getString("MOBILE1"))
                            .build();
                }
            }
        });

        //sql = "SELECT to_char(SIGN_TIME,'yyyy-MM-dd HH24:mi:ss') AS SIGN_TIME,USERNAME,PHONE FROM PM_SIGN_INFO WHERE TRUNC(LONGITUDE,4) = ? AND TRUNC(LATITUDE,4) = ? AND rn = 1 ORDER BY SIGN_TIME";
        sql = "SELECT to_char(c.SIGN_TIME,'yyyy-MM-dd HH24:mi:ss') AS SIGN_TIME,c.USERNAME,c.PHONE,c.USERID FROM (SELECT t.*, row_number() OVER(PARTITION BY t.USERID ORDER BY t.SIGN_TIME DESC) rn FROM PM_SIGN_INFO t) c WHERE rn = 1 AND TRUNC(LONGITUDE,4) = ? AND TRUNC(LATITUDE,4) = ?";
        List<ImmutableMap<String, String>> list1 = jdbcTemplate.query(sql, new Object[]{longitude,latitude}, new RowMapper<ImmutableMap<String,String>>() {
            @Override
            public ImmutableMap<String, String> mapRow(ResultSet resultSet, int i) throws SQLException {
                return ImmutableMap.<String, String>builder()
                        .put("useraccount", StringUtils.isEmpty(resultSet.getObject("USERID")) ? "" : resultSet.getString("USERID"))
                        .put("username", StringUtils.isEmpty(resultSet.getObject("USERNAME")) ? "" : resultSet.getString("USERNAME"))
                        .put("depart", "")
                        .put("teams", "")
                        .put("responsibility", "")
                        .put("sign_time", StringUtils.isEmpty(resultSet.getObject("SIGN_TIME")) ? "" : resultSet.getString("SIGN_TIME"))
                        .put("phone", StringUtils.isEmpty(resultSet.getObject("PHONE")) ? "" : resultSet.getString("PHONE"))
                        .build();
            }
        });
        if(list.size() > 0){
            for(int i = 0;i<list1.size();i++){
                ImmutableMap<String, String> map = list1.get(i);
                for(int j = 0;j<list.size();j++){
                    ImmutableMap<String, String> map1 = list.get(j);
                    if(map.get("useraccount").equals(map1.get("useraccount"))){
                        list1.remove(i);
                        break;
                    }
                    if(j == list.size()-1){
                        list.add(map);
                    }
                }
            }
            return list;
        }else{
            return list1;
        }
    }

}
