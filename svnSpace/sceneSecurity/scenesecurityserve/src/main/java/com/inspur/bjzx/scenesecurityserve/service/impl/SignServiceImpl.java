package com.inspur.bjzx.scenesecurityserve.service.impl;

import com.inspur.bjzx.scenesecurityserve.service.SignService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SignServiceImpl implements SignService{

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    private static final Logger log = Logger.getLogger(SignServiceImpl.class);

    public int  realTimeSign(Map<String,Object> person,String longitude,String latitude,String location,int isSignBack){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String sql = "INSERT INTO pm_sign_info " +
                "(ID,USERID,LONGITUDE,LATITUDE,ADDRESS,SIGN_TIME,SIGN_TYPE,PHONE,USERNAME) VALUES " +
                "(?,?,?,?,?, to_date(?,'YYYY-MM-DD HH24:MI:SS'),?,?,?)";
        log.info(sql);
        int insertResult=jdbcTemplate.update(sql,uuid,person.get("userid"),longitude,latitude,location,df.format(new Date()),isSignBack,person.get("phone"),person.get("name"));
        log.info(insertResult);
        return insertResult;
    }



    public List<Map<String,Object>> searchSignLine(String userId){
        String sql = "select longitude \"longitude\",\n" +
                "decode(sign_type,0,'in',1,'out') \"signType\",\n" +
                "latitude \"latitude\"\n" +
                "from pm_sign_info\n" +
                "where userId= ? \n"+
                "and sign_time>= trunc(sysdate)\n" +
                "and longitude!=0\n" +
                "and latitude!=0\n" +
                "ORDER BY sign_time ";
        log.info(sql);

        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,userId);
        log.info(list);
        return list;
    }


    public List<Map<String,Object>> searchSign(){
        String sql="select nvl(SIGNINUSERID,SIGNBACKUSERID) \"userId\",\n" +
                "nvl(SIGNINUSERNAME,SIGNBACKUSERNAME) \"userName\", \n" +
                "to_char(SIGNINTIME,'YYYY-MM-DD HH24:MI:SS') \"signInTime\",\n" +
                "SIGNINADDRESS \"signInAddress\",\n" +
                "to_char(SIGNBACKTIME,'YYYY-MM-DD HH24:MI:SS') \"signBackTime\",\n" +
                "SIGNBACKADDRESS \"signBackAddress\" from\n" +
                "(select a.userId \"SIGNINUSERID\",\n" +
                "a.username \"SIGNINUSERNAME\",\n" +
                "a.sign_time \"SIGNINTIME\",\n" +
                "a.address \"SIGNINADDRESS\" from pm_sign_info a \n" +
                "where sign_time = (\n" +
                "select min(sign_time) from pm_sign_info \n" +
                "where userid = a.userid\n" +
                "and sign_type=0\n" +
                "and sign_time>= trunc(sysdate)\n" +
                ") and sign_type = 0 )c\n" +
                "full join\n" +
                "(select b.userId \"SIGNBACKUSERID\",\n" +
                "b.username \"SIGNBACKUSERNAME\",\n" +
                "b.sign_time \"SIGNBACKTIME\",\n" +
                "b.address \"SIGNBACKADDRESS\" from pm_sign_info b \n" +
                "where sign_time = (\n" +
                "select max(sign_time) from pm_sign_info \n" +
                "where userid = b.userid\n" +
                "and sign_type=1\n" +
                "and sign_time>= trunc(sysdate)\n" +
                ") and sign_type = 1 )d\n" +
                "on c.signInUserId = d.signBackUserId";
        log.info(sql);
//        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        log.info(list);
        return list;
    }

    public List<Map<String,Object>> getSecurityPersons(String searchUserName){
        String sql = "select s.userid \"userId\",\n" +
                "s.username \"userName\",\n" +
                "s.longitude \"longitude\",\n" +
                "s.latitude \"latitude\",\n" +
                "s.address \"location\",\n" +
                "nvl(t.comm_means1,t.comm_means2) \"userPhone\",\n" +
                "t.meet_id \"videoNum\",\n" +
                "p.file_path  \"file_path\",\n" +
                "p.file_type \"file_type\" from pm_sign_info s \n" +
                "left join eme_staff_info t on s.userid=t.ad_account\n" +
                "left join pm_up_file_info p on s.userid=p.userid and p.create_time>= trunc(sysdate)\n" +
                "where (s.userid,s.sign_time) in (\n" +
                "select userid,max(sign_time) sign_time from pm_sign_info \n" +
                "where sign_time>= trunc(sysdate)";
        if(!searchUserName.equals("")){
            sql+="and userName like '%'||?||'%' \n";
        }
        sql+="group by userid)\n";
        log.info(sql);

        List<Map<String,Object>> list;
        if(!searchUserName.equals("")){
            list = jdbcTemplate.queryForList(sql,searchUserName);
        }else{
            list = jdbcTemplate.queryForList(sql);

        }
        return list;
    }

    @Override
    public int getSignNum(String userAccount) {
        String sql = "select count(distinct USERID) \n" +
                "from pm_sign_info \n" +
                "where \n" +
                "sign_time >= trunc(sysdate)";
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }
}
