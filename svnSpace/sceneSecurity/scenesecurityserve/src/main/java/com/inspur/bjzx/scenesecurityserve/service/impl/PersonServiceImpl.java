package com.inspur.bjzx.scenesecurityserve.service.impl;

import com.inspur.bjzx.scenesecurityserve.service.PersonService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PersonServiceImpl implements PersonService{


    private static final Logger log = Logger.getLogger(PersonServiceImpl.class);

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;


    //查询值班表中人员数量
    public int getPersonNum() {
        log.info("查询值班表全部人员数量");
        String sql = "SELECT COUNT(*) AS \"count\" FROM EME_STAFF_INFO";
        log.info(sql);
        Integer integer = jdbcTemplate.queryForObject(sql, Integer.class);
        log.info(integer);
        return integer;
    }


    //查询人员信息——根据userAccount
    public List<Map<String,Object>> getPersonByUerAccount(String userAccount){
        log.info("根据userAccount("+userAccount+")查询人员信息");
        String sql ="SELECT " +
                "AD_ACCOUNT \"userid\", " +
                "CHINA_NAME  \"name\"," +
                "NVL(COMM_MEANS1,COMM_MEANS2) \"phone\" " +
                "FROM EME_STAFF_INFO " +
//                "WHERE AD_ACCOUNT = '" + userAccount +"'";
                "WHERE AD_ACCOUNT = ?";
        log.info(sql);

//        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,userAccount);
        log.info(list);
        return list;
    }

    @Override
    public List<Map<String, Object>> getBattleRoom(String roomId) {

        log.info("根据roomId("+roomId+")查询作战室信息");
        String sql = "select ID \"id\",NAME \"name\" from eme_room_organize \n" +
                "where room_id = ? \n" +
                "and pid<>0 \n" +
                "order by show_order";
        log.info(sql);
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,roomId);
        for (Map<String,Object> map:list){
            map.put("type","company");
            String sql2 = "select count(*) from eme_room_staff where id = ?";
            String companyNumber = jdbcTemplate.queryForObject(sql2, String.class, map.get("ID"));
            map.put("companyNumber",companyNumber);
            map.put("isCheck","false");
            map.put("isOpened","false");
        }
        log.info(list);
        return list;
    }

    @Override
    public List<Map<String, Object>> getStaffInfo(Object id, String searchText) {
        log.info("根据id("+id+")查询作战室信息");
        String sql = "select b.sequence_id \"hierarchyId\",a.china_name \"name\", \n" +
                "b.comm_means1 \"phone\",b.meet_id \"receiveVideoNum\" ,b.AD_ACCOUNT \"account\"\n" +
                "from eme_room_staff a left join eme_staff_info b \n" +
                "on a.ad_account=b.ad_account \n" +
                "where a.id = ? ";
        if (!"".equals(searchText)){
            sql+="and a.china_name like '%"+searchText+"%'";
        }
        log.info(sql);
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,id);
        if (list.size()==0)return list;
        for (Map<String,Object> map:list){
            map.put("type","person");
            map.put("isCheck","false");
        }
        log.info(list);
        return list;
    }


    //查询人员信息——
    //company不为空 查询company下的所有人，并把其分公司名称查询出来
    //searchText不为空 模糊匹配所有人,并把其分公司名称查询出来
    public List<Map<String,Object>> getPerson(String company,String searchText){
        if(!searchText.equals("")){
            log.info("查询人员信息——name模糊匹配"+searchText);
        }else{
            log.info("查询人员信息——查询"+company+"底下所有人");
        }
        String sql ="SELECT 'person'  \"type\",\n" +
                "AD_ACCOUNT \"hierarchyId\",\n" +
                "CHINA_NAME  \"name\",\n" +
                "meet_id \"videoNum\",\n"+
                "NVL(COMM_MEANS1,COMM_MEANS2) \"phone\",\n" +
                "'false' \"isCheck\",\n" +
                "LR_DEPARTMENT \"send_depart\" "+
                "FROM EME_STAFF_INFO\n";

        if(!searchText.equals("")){
//            sql+="WHERE CHINA_NAME  LIKE '%" ;
//            sql+=searchText;
//            sql+="%'";
            sql+="WHERE CHINA_NAME  LIKE '%'||?||'%'" ;
        }else{
//            sql+="WHERE LR_DEPARTMENT = '";
//            sql+=company;
//            sql+="'";
            sql+="WHERE LR_DEPARTMENT = ?";
        }
        log.info(sql);
//        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);

        List<Map<String,Object>> list;
        if(!searchText.equals("")){
            list = jdbcTemplate.queryForList(sql,searchText);
        }else{
            list = jdbcTemplate.queryForList(sql,company);
        }
        return list;
    }



    //查询公司信息——
    //searchText为空 查询所有
    //searchText不为空  模糊匹配
    public List<Map<String,Object>> getCompany(String searchText){
        if(searchText.equals("")){
            log.info("查询所有公司信息");
        }else{
            log.info("公司名称模糊匹配"+searchText+"查询公司信息");
        }
        String sql ="SELECT DISTINCT LR_DEPARTMENT \"name\",\n" +
                "'' \"hierarchyId\",\n" +
                "'company' \"type\",\n" +
                "'false' \"isCheck\",\n" +
                "'false' \"isOpened\"\n" +
                "FROM EME_STAFF_INFO\n" +
                "WHERE LR_DEPARTMENT IS NOT NULL\n";
        if(!searchText.equals("")){
//            sql+="AND LR_DEPARTMENT LIKE '%" +searchText +"%'";
            sql+="AND LR_DEPARTMENT LIKE '%'||?||%'";
        }
        log.info(sql);

//        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);

        List<Map<String,Object>> list;
        if(!searchText.equals("")){
            list = jdbcTemplate.queryForList(sql,searchText);
        }else{
            list = jdbcTemplate.queryForList(sql);
        }
        return list;
    }


    //根据多个company查人员信息
    public List<Map<String,Object>> getPersonByCompany(ArrayList<String>  company){
        log.info("查询人员信息——"+company+"底下的人");
        String sql ="select b.sequence_id \"hierarchyId\",a.china_name \"name\",\n" +
                "b.comm_means1 \"phone\" ,b.meet_id \"receiveVideoNum\"\n" +
                "from eme_room_staff a left join eme_staff_info b \n" +
                "on a.ad_account=b.ad_account \n";
        for(int i = 0;i<company.size();i++){
            if(i ==0){
                sql+="WHERE a.id = '"
                +company.get(i)
                +"' ";
            }else{
                sql+="OR a.id = '"
                +company.get(i)
                +"' ";
            }
        }
//
//        Object[] args={};
//        for(int i = 0;i<company.size();i++){
//            if(i ==0){
//                sql+="WHERE LR_DEPARTMENT = ? ";
//            }else{
//                sql+="OR LR_DEPARTMENT =? ";
//            }
//            args[args.length]=company.get(i);
//        }
        log.info(sql);
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
//        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,args);
        log.info(list);
        return list;
    }



    public String getPermission(String userAccount){
        log.info("查询"+userAccount+"的权限");
        String sql = "SELECT PROTECT_AREA \"address\" " +
                "FROM EME_STAFF_INFO " +
//                "WHERE AD_ACCOUNT = '" + userAccount+"'";
                "WHERE AD_ACCOUNT = ?";
        log.info(sql);

//        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,userAccount);
        if(list.size()>0){
            return ("全部".equals(list.get(0).get("address"))?"all":"notall");
        }else{
            return "";
        }
    }



    public  List<Map<String,Object>> getCompanyByUserAccount(String userAccount){
        String sql = "SELECT LR_DEPARTMENT \"company\",\n" +
                "meet_id \"videoNum\"\n"+
                "FROM EME_STAFF_INFO\n" +
//                "WHERE AD_ACCOUNT = '" + userAccount +"'";
                "WHERE AD_ACCOUNT = ?";
        log.info(sql);

//        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);

        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,userAccount);

        if(list.size()>0){
            return list;
        }else{
            return new ArrayList<>();
        }
    }


}


