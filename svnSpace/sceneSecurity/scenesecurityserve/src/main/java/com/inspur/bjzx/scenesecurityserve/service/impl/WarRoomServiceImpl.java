package com.inspur.bjzx.scenesecurityserve.service.impl;

import com.inspur.bjzx.scenesecurityserve.service.WarRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Service
public class WarRoomServiceImpl implements WarRoomService {
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    @Override
    public List<Map<String, Object>> getWarRoomSceneAllInfo(String userAccount) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String today=simpleDateFormat.format(new Date());
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE,1);
        String todayend=simpleDateFormat.format(calendar.getTime());
        //today="2019-08-26";
        String sql1 ="select INT_ID from TASK_BOBAO_INFOHadLook where USERACCOUNT=? and WATCH_TIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss')";
        System.out.println("sq1==="+sql1+userAccount);
        List<Map<String,Object>> intIds =jdbcTemplate.queryForList(sql1,new Object[]{userAccount,today});

        String qSql="select  to_char(a.START_TIME,'yyyy-mm-dd hh24:mi:ss') \"release_time\", b.ROOM_NAME \"roomname\", a.INT_ID \"id\" ,a.BOBAO_INFO \"content\" from task_bobao_info a,eme_battle_room b WHERE b.ROOM_ID=a.scene_id and a.START_TIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and a.START_TIME<to_date(?,'yyyy-mm-dd hh24:mi:ss')  %s  order by a.START_TIME desc";
        String condition="";
        System.out.println("intids==="+intIds);
        if(null!=intIds&&intIds.size()!=0){
            condition=" and int_id not in(";
            for(Map<String,Object> id:intIds){
                condition+= id.get("INT_ID")+",";
            }
            condition=condition.substring(0,condition.lastIndexOf(','));
            condition=condition+")";
        }

        qSql=String.format(qSql,condition);
        System.out.println("sql===="+qSql+"  time=="+today+" endtoday=="+todayend+"  condi=="+condition);
        List<Map<String,Object>> ansList=jdbcTemplate.queryForList(qSql,new Object[]{today,todayend});
        return ansList;
    }

    @Override
    public boolean changeHadLook(String userAccount,Integer noticeId){
        if(userAccount!=null&&noticeId!=null) {
            String sql = "insert into TASK_BOBAO_INFOHadLook values (S_INFOHadLook_log.nextval,?,?,sysdate)";
            System.out.println("useraccount==="+userAccount+"  noticeid"+noticeId);
            int ans=jdbcTemplate.update(sql,new  Object[]{noticeId,userAccount});
            return ans>=1?true:false;
        }else{
            return false;
        }
    }
}
