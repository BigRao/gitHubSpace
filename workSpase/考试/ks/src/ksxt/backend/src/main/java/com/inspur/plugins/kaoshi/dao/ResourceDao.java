package com.inspur.plugins.kaoshi.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class ResourceDao {
    @Resource
    JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getResourceList(String userId, int number1, int number2) {
        String sql = "SELECT ID AS \"resource_id\",NAME AS \"resource_name\",\n" +
                "       RESOURCE_TYPE AS \"resource_type\",PATH AS \"resource_path\",\n" +
                "       TO_CHAR(CREATE_TIME, 'YYYY-MM-DD HH24:MI:SS') AS \"create_time\",\n" +
                "       nvl(b.IS_PASS,'0') \"is_pass\"\n" +
                "FROM (SELECT ROWNUM AS no, t.*\n" +
                "      FROM KS_RESOURCE t\n" +
                "      WHERE ROWNUM <= ? ) t\n" +
                "full join\n" +
                "    (select KS_LEARN_LOG.RESOURCE_ID,KS_LEARN_LOG.IS_PASS\n" +
                "    from KS_LEARN_LOG\n" +
                "    where USER_ID=?) b\n" +
                "on b.RESOURCE_ID=t.ID\n" +
                "WHERE t.no > ? order by ID";
        return jdbcTemplate.queryForList(sql, number1, userId, number2);
    }

    public Map<String, Object> resourceLook(String resourceId) {
        String sql = "SELECT ID AS \"resource_id\",NAME AS \"resource_name\",\n" +
                "RESOURCE_TYPE AS \"resource_type\",PATH AS \"resource_path\",\n" +
                "TO_CHAR(CREATE_TIME, 'YYYY-MM-DD HH24:MI:SS') AS \"create_time\" \n" +
                "FROM KS_RESOURCE t\n" +
                "      WHERE ID = ?";
        return jdbcTemplate.queryForMap(sql, resourceId);
    }

    public int recordCurrentTime(String resourceId, String userId, String startTime, String endTime, String isPass) {
        String sql = "insert into KS_LEARN_LOG(user_id, username, resource_id, resource_name, type_id, type_name, start_time, end_time, IS_PASS) \n" +
                "select ? user_id,(select KS_USER.NAME from KS_USER where KS_USER.ID = ?) \"username\",\n" +
                "? , KS_RESOURCE.NAME, KS_RESOURCE.TYPE_ID, KS_RESOURCE.RESOURCE_TYPE,\n" +
                " to_date(?,'YYYY-MM-DD HH24:MI:SS'), to_date(?,'YYYY-MM-DD HH24:MI:SS'), \n" +
                "? from KS_RESOURCE where KS_RESOURCE.ID = ?\n";

        return jdbcTemplate.update(sql, userId, userId, resourceId, startTime, endTime, isPass, resourceId);
    }

    public Map<String, Object> getResourceSearch(String resourceId, String userId) {
        String sql = "select NVL(RESOURCE_NAME,'') \"resource_name\",\n" +
                "NVL(TO_CHAR(START_TIME,'YYYY-MM-DD HH24:MI:SS'),'') \"start_time\",\n" +
                "NVL(TO_CHAR(END_TIME,'YYYY-MM-DD HH24:MI:SS'),'') \"end_time\",NVL(IS_PASS,'') \"is_pass\",\n" +
                "(select PATH from KS_RESOURCE where ID=?) \"resource_path\"\n" +
                "from (select t.*, row_number() over (order by t.END_TIME desc) as rnum\n" +
                "      FROM KS_LEARN_LOG t\n" +
                "      where USER_ID=? and RESOURCE_ID=?) where rnum = 1";
        return jdbcTemplate.queryForMap(sql, resourceId, userId, resourceId);
    }

    public Map<String, Object> getResourceCount() {
        String sql = "select count(ID) \"count\"\n" +
                "from KS_RESOURCE";
        return jdbcTemplate.queryForMap(sql);
    }

    public Map<String, Object> resourceLogCount(String resourceId, String userId) {
        String sql = "select count(ID) \"count\"\n" +
                "from KS_LEARN_LOG \n" +
                "where USER_ID=?\n" +
                "and RESOURCE_ID=?\n";
        return jdbcTemplate.queryForMap(sql, userId, resourceId);
    }

    public Map<String, Object> getResourceName(String resourceId) {
        String sql = "select NAME \"resource_name\",PATH \"resource_path\"\n" +
                "from (SELECT ROWNUM , t.*\n" +
                "      FROM KS_RESOURCE t\n" +
                "      WHERE ROWNUM <= '1' and ID=?)";
        return jdbcTemplate.queryForMap(sql, resourceId);
    }
}
