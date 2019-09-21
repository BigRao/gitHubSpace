package com.inspur.bjzx.city.service.impl;

import com.google.common.collect.ImmutableMap;
import com.inspur.bjzx.city.service.CommonService;
import com.inspur.bjzx.city.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liurui on 2017/8/3.
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<ImmutableMap<String, String>> getLoginInfo(String userAccount, String userPwd) {
        String sql;
        if (DataUtil.isEmail(userAccount)) {
            sql = "SELECT ID,PASSWORD,USERNAME,PHONE,EMAIL FROM PALM_USER WHERE EMAIL = ? AND PASSWORD = ?";
        } else if (DataUtil.isPhone(userAccount)){
            sql = "SELECT ID,PASSWORD,USERNAME,PHONE,EMAIL FROM PALM_USER WHERE PHONE = ? AND PASSWORD = ?";
        }else {
            sql = "SELECT ID,PASSWORD,USERNAME,PHONE,EMAIL FROM PALM_USER WHERE USERACCOUNT = ? AND PASSWORD = ?";
        }

        return jdbcTemplate.query(sql, new Object[]{userAccount, userPwd}, (resultSet, i) -> ImmutableMap.<String, String>builder()
                .put("userAccount", getDbValue(resultSet, "EMAIL"))
                .put("userId", getDbValue(resultSet, "ID"))
                .put("userName", getDbValue(resultSet, "USERNAME"))
                .put("userPhone", getDbValue(resultSet, "PHONE"))
                .put("token", DataUtil.getToken())
                .build());
    }

    @Override
    public List<ImmutableMap<String, String>> getRegionInfo(String type) {
        String sql = "SELECT ACRONYM,ID FROM PALM_REGION_CONF WHERE REGION_TYPE = ?";
        return jdbcTemplate.query(sql, new Object[]{type}, (resultSet, i) -> ImmutableMap.<String, String>builder()
                .put("id", getDbValue(resultSet, "ID"))
                .put("name", getDbValue(resultSet, "ACRONYM"))
                .build());
    }

    @Override
    public List<ImmutableMap<String, Object>> getIntelligenceInfo(String maxNumber, String start, String type) {
        int limit = DataUtil.O2I(start) * DataUtil.O2I(maxNumber);
        int j = limit-DataUtil.O2I(maxNumber)+1;
        String sql = "select *\n" +
                "   from (select    b.ID,\n" +
                "                        b.NEWS_HEADLINES,\n" +
                "                        b.NEWS_SOURCE,\n" +
                "                        TO_DATE(b.RELEASE_TIME,'yyyy-MM-dd hh24:mi:ss') RELEASE_TIME,\n" +
                "                        b.ATTENTION_NUMBER,\n" +
                "                        b.NEWS_LINK,\n" +
                "                        b.THUMB_LINK,\n" +
                "                        b.NEWS_TYPE, \n" +
                "                        rownum rn\n" +
                "           from (SELECT ID, \n" +
                "              NEWS_HEADLINES,\n" +
                "                        NEWS_SOURCE,\n" +
                "                        RELEASE_TIME,\n" +
                "                        ATTENTION_NUMBER,\n" +
                "                        NEWS_LINK,\n" +
                "                        THUMB_LINK,\n" +
                "                        NEWS_TYPE\n" +
                "                   FROM PALM_INTELLIGENCE_INFORMATION\n" +
                "                   WHERE NEWS_TYPE = ?  ORDER BY TO_DATE(RELEASE_TIME, 'yyyy-mm-dd hh24:mi:ss') DESC) b)\n" +
                "  where rn between ? and ?";
        return jdbcTemplate.query(sql, new Object[]{type, j, limit}, (resultSet, i) -> ImmutableMap.<String, Object>builder()
                .put("id", resultSet.getInt("ID"))
                .put("title", getDbValue(resultSet, "NEWS_HEADLINES"))
                .put("source", getDbValue(resultSet, "NEWS_SOURCE"))
                .put("releaseTime", resultSet.getString("RELEASE_TIME").substring(5, 16))
                .put("attentionNumber", resultSet.getInt("ATTENTION_NUMBER"))
                .put("link", getDbValue(resultSet, "NEWS_LINK"))
                .put("type", resultSet.getInt("NEWS_TYPE"))
                .put("thumbLink", getDbValue(resultSet, "THUMB_LINK"))
                .build());
    }

    @Override
    public List<ImmutableMap<String, String>> checkVersion() {
        String sql = "SELECT ID,VERSION_NAME,VERSION_CODE,APP_NAME,RELEASE_TIME,REMARKS FROM PALM_APP_VERSION ORDER BY  RELEASE_TIME DESC";
        return jdbcTemplate.query(sql, (resultSet, i) -> ImmutableMap.<String, String>builder()
                .put("id", getDbValue(resultSet, "ID"))
                .put("versionName", getDbValue(resultSet, "VERSION_NAME"))
                .put("versionCode", getDbValue(resultSet, "VERSION_CODE"))
                .put("appName", getDbValue(resultSet, "APP_NAME"))
                .put("releaseTime", getDbValue(resultSet, "RELEASE_TIME"))
                .put("remarks", getDbValue(resultSet, "REMARKS"))
                .build());
    }

    @Override
    public boolean register(String userName, String userPwd, String phone, String email) {
        String sql = "insert into PALM_USER (ID,PASSWORD, USERNAME, PHONE, EMAIL,USERACCOUNT) values (PALM_USER_ID_SEQ.nextval,?,?,?,?,?)";
        Integer integer = jdbcTemplate.update(sql, new Object[]{userPwd, userName, phone, email,phone});
        return integer == 1;
    }

    @Override
    public Integer getPhoneCount(String phone) {
        String sql = "select count(*) as \"count\" from PALM_USER where PHONE=?";
        Map<String, Object> phoneCount = jdbcTemplate.queryForMap(sql, new Object[]{phone});
        return DataUtil.O2I(phoneCount.get("count"));
    }

    @Override
    public boolean hasEmail(String email) {
        String sql = "select count(*) as \"count\" from PALM_USER where EMAIL=?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[]{email});
        return !(DataUtil.O2I(map.get("count"))==0);
    }

    @Override
    public boolean hasPhone(String phone) {
        String sql = "select count(*) as \"count\" from PALM_USER where PHONE=?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[]{phone});
        return !(DataUtil.O2I(map.get("count"))==0);
    }

    @Override
    public String getUserId(String phone) {
        String sql = "select ID from PALM_USER where PHONE=?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[]{phone});

        return DataUtil.O2S(map.get("ID"));
    }

    @Override
    public boolean resetPwd(String phone, String userPwd) {
        String sql = "update PALM_USER set PASSWORD = ? where PHONE = ? ";
        Integer integer = jdbcTemplate.update(sql, new Object[]{userPwd, phone,});
        return integer == 1;
    }

    @Override
    public boolean setOpLog(String userId, String token, String typeName, String objectName, String message, String result) {
        String sql = "insert into PALM_APP_OPERATIONLOG (id, token, userid, typename, objectname, message, optime, result) " +
                "values (PALM_APP_OPERATIONLOG_ID_SEQ.nextval,?,?,?,?,?,?,?)";
        Integer integer = jdbcTemplate.update(sql, new Object[]{token, userId, typeName, objectName, message, new Date(), result});
        return integer == 1;
    }


    @Override
    public void setLoginLog(String token, String userAccount, String result) {
        String type;
        if (DataUtil.isEmail(userAccount)) {
            type = "email";
        } else {
            type = "phone";
        }
        String sql = "insert into PALM_APP_USERLOG (LOGINID, TOKEN, LOGINTIME,TYPE, CNNAME, RESULT) " +
                "values (PALM_APP_USERLOG_LOGINID_SEQ.nextval,?,?,?,?,?)";
        jdbcTemplate.update(sql, new Object[]{token, new Date(), type, userAccount, result});
    }

    @Override
    public boolean setLogoutLog(String token) {
        String sql = "update PALM_APP_USERLOG set LOGOUTTIME = ? where TOKEN = ? ";
        Integer integer = jdbcTemplate.update(sql, new Object[]{new Date(), token});
        return integer == 1;
    }

    @Override
    public Integer getIntelligenceSize(String type) {
        String sql = "select count(*) as \"count\" from PALM_INTELLIGENCE_INFORMATION where NEWS_TYPE=?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[]{type});
        return DataUtil.O2I(map.get("count"));
    }

    private String getDbValue(ResultSet resultSet, String chart_type) throws SQLException {
        return StringUtils.isEmpty(resultSet.getObject(chart_type)) ? "" : resultSet.getString(chart_type);
    }
}
