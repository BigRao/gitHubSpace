package com.inspur.bjzx.scenesecurityserve.service.impl;

import com.google.common.collect.ImmutableMap;
import com.inspur.bjzx.scenesecurityserve.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Lenovo on 2017/4/21.
 */

@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate1;


    @Override
    public List<ImmutableMap> getSite(final String netype, final String site_no) {
        List<ImmutableMap<String,String>> list = new ArrayList<>();
        String table;
        if(netype.equals("BTS")){
            table = "PM_AR_BTS_HOUR";
        }else{
            table = "PM_AR_ENODEB_HOUR";
        }
        String sql = "SELECT * FROM "+table+" WHERE SITENO = ?";
        return jdbcTemplate.query(sql, new Object[]{site_no}, new RowMapper<ImmutableMap>() {
            @Override
            public ImmutableMap mapRow(ResultSet resultSet, int i) throws SQLException {
                if(netype.equals("BTS")){
                    return ImmutableMap.<String,String>builder()
                            .put("sitename", site_no)                                                                //StringUtils.isEmpty(resultSet.getObject("SITENAME")) ? "" : resultSet.getString("SITENAME")
                            .put("traffic", StringUtils.isEmpty(resultSet.getObject("TRAFFIC")) ? "" : resultSet.getString("TRAFFIC"))
                            .put("flow", StringUtils.isEmpty(resultSet.getObject("FLOW")) ? "" : resultSet.getString("FLOW"))
                            .put("cr_rate", StringUtils.isEmpty(resultSet.getObject("CR_RATE")) ? "" : resultSet.getString("CR_RATE"))
                            .put("dr_rate", StringUtils.isEmpty(resultSet.getObject("DR_RATE")) ? "" : resultSet.getString("DR_RATE"))
                            .build();
                }else{
                    return ImmutableMap.<String,String>builder()
                            .put("sitename", site_no)                                                                       //StringUtils.isEmpty(resultSet.getObject("SITENAME")) ? "" : resultSet.getString("SITENAME")
                            .put("flow", StringUtils.isEmpty(resultSet.getObject("FLOW")) ? "" : resultSet.getString("FLOW"))
                            .put("lte_cr", StringUtils.isEmpty(resultSet.getObject("LTE_CR")) ? "" : resultSet.getString("LTE_CR"))
                            .put("lte_dr", StringUtils.isEmpty(resultSet.getObject("LTE_DR")) ? "" : resultSet.getString("LTE_DR"))
                            .put("volte_traffic", StringUtils.isEmpty(resultSet.getObject("VOLTE_TRAFFIC")) ? "" : resultSet.getString("VOLTE_TRAFFIC"))
                            .put("volte_cr", StringUtils.isEmpty(resultSet.getObject("VOLTE_CR")) ? "" : resultSet.getString("VOLTE_CR"))
                            .put("volte_dr", StringUtils.isEmpty(resultSet.getObject("VOLTE_DR")) ? "" : resultSet.getString("VOLTE_DR"))
                            .build();
                }
            }
        });
    }


    @Override
    public List<ImmutableMap> getAccessPointByLaLong(double latitude, double longitude, String radius, String neTypes) {
        Map<String,Double> map = this.getRange(latitude, longitude, radius);

        double minLng=0;
        double maxLng=0;
        double minLat=0;
        double maxLat=0;
        if(map.size() > 0){
            minLng = map.get("minLng");//经度
            maxLng = map.get("maxLng");

            minLat = map.get("minLat");//纬度
            maxLat = map.get("maxLat");
        }
        String [] type = neTypes.split(",");
        String sql = "";
        List<ImmutableMap> list = new ArrayList<>();
        String minLngString = String.valueOf(minLng);
        String maxLngString = String.valueOf(maxLng);
        String minLatString = String.valueOf(minLat);
        String maxLatString = String.valueOf(maxLat);
        System.out.println("minLng:"+minLngString);
        System.out.println("maxLng:"+maxLngString);
        System.out.println("minLat:"+minLatString);
        System.out.println("maxLat:"+maxLatString);
        for(String str : type){
            if("BTS".equals(str)){
                sql="SELECT 'BTS' TYEP,b.BEEHIVE_TYPE,b.LATITUDE,b.LONGITUDE,b.SITE_NAME,b.BTS_NO FROM pm_ar_bts_hour b WHERE b.longitude IS NOT NULL AND b.latitude IS NOT NULL AND b.longitude<=? AND b.longitude>=? AND b.latitude<? AND b.latitude>=? ";
            }else if("NODEB".equals(str)){
                sql="SELECT 'NODEB' TYEP,b.BEEHIVE_TYPE,b.LATITUDE,b.LONGITUDE,b.SITE_NAME,b.SITE_NO FROM RMS_NODEB b WHERE b.longitude IS NOT NULL AND b.latitude IS NOT NULL AND b.longitude<=? AND b.longitude>=? AND b.latitude<? AND b.latitude>=?";
            }else if("LTE_E_NODEB".equals(str)){
                sql="SELECT 'LTE_E_NODEB' TYEP,b.BEEHIVE_TYPE,b.LATITUDE ,b.LONGITUDE ,b.SITE_NAME,b.SITE_NO FROM pm_ar_enodeb_hour b WHERE b.LONGITUDE IS NOT NULL AND b.LATITUDE IS NOT NULL AND b.LONGITUDE<=? AND b.LONGITUDE>=? AND b.LATITUDE<? AND b.LATITUDE>=? ";
            }
            sql += " AND b.BEEHIVE_TYPE IS NOT NULL\n";
            List<ImmutableMap> tempList = (List<ImmutableMap>) jdbcTemplate.query(sql, new Object[]{maxLngString, minLngString, maxLatString, minLatString}, new ResultSetExtractor() {
                public Object extractData(ResultSet rs) throws SQLException,
                        DataAccessException {
                    List<ImmutableMap> list = new ArrayList<>();
                    while (rs.next()) {
                        int i = 1;
                        list.add(ImmutableMap.<String,String>builder()
                                .put("types", rs.getString(i++))
                                .put("beehive_type", rs.getString(i++))
                                .put("latitude", rs.getString(i++))
                                .put("longitude", rs.getString(i++))
                                .put("site_name", rs.getString(i++))
                                .put("site_no", rs.getString(i++))
                                .build());
                    }
                    return list;
                }
            });
            list.addAll(Objects.requireNonNull(tempList));
        }
        return list;
    }

    //接口三获取经纬度范围
    private Map<String,Double> getRange(double latitude,double longitude,String radiusS){
        int radius=Integer.parseInt(radiusS);

        double degree = (24901*1609)/360.0;
        double dpmLat = 1/degree;
        double radiusLat = dpmLat*radius;
        double minLat = latitude - radiusLat;//纬度
        double maxLat = latitude + radiusLat;

        double mpdLng = degree*Math.cos(latitude*(3.14159265/180));
        double dpmLng = 1 / mpdLng;
        double radiusLng = dpmLng*radius;
        double minLng = longitude - radiusLng;//经度
        double maxLng = longitude + radiusLng;

        Map<String,Double> map= new HashMap<>();
        map.put("minLng",minLng);
        map.put("maxLng",maxLng);
        map.put("minLat",minLat);
        map.put("maxLat",maxLat);
        return map;
    }
}
