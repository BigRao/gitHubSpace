package com.inspur.bjzx.scenesecurityserve.service.impl;

import com.inspur.bjzx.scenesecurityserve.service.NoticeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class NoticeServiceImpl implements NoticeService {


    @Autowired
    @Qualifier("primaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;


    private static final Logger log = Logger.getLogger(NoticeServiceImpl.class);

    @Override
    public List<Map<String,Object>> getNotice(){
        String sql = "SELECT * from(select ID as \"id\",TITLE as \"title\"," +
                "CONTENT as \"content\",RELEASE_PERSON as \"release_person\"," +
                "to_char(RELEASE_TIME,'YYYY-MM-DD HH24:MI:SS') as \"release_time\" from  scene_notice ORDER by RELEASE_TIME desc)A where rownum = 1";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        log.info("NoticeDao");
        return list;
    }


    public int addNotice(String userAccount,String title,String noticeText){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "INSERT INTO scene_notice (id,title,content,release_person,release_time) values " +
//                "(1,'" +
//                title +"','"+noticeText+"','"+userAccount+"', to_date('"+df.format(new Date())+"','YYYY-MM-DD HH24:MI:SS'))";
                "(1,?,?,?,TO_DATE(?,'YYYY-MM-DD HH24:MI:SS'))" ;
//        int result = jdbcTemplate.update(sql);
        int result = jdbcTemplate.update(sql,title,noticeText,userAccount,df.format(new Date()));

        log.info(result);
        return result;
    }
}
