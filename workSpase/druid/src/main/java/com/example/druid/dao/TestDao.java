package com.example.druid.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class TestDao {
    @Resource
    JdbcTemplate jdbcTemplate;

    public Map<String, Object> getTopicNum() {
        String sql = "select SINGLE_NUM \"single_num\",\n" +
                "MULTIPLE_NUM \"multiple_num\",\n" +
                "JUDGE_NUM \"judge_num\"\n" +
                "from KS_TEST";
        return jdbcTemplate.queryForMap(sql);
    }

    public Map<String, Object> selectMust(String type) {
        String sql = "select count(*) \"count\"\n" +
                "from KS_TOPIC \n" +
                "where \n" +
                "      must_in = '1' \n" +
                "  and CHOICE_TYPE = ?\n" +
                "  and ID in (select TOPIC_ID from KS_TOPIC_TEST where TEST_ID = '1')";
        return jdbcTemplate.queryForMap(sql, type);
    }

    public List<Map<String, Object>> getMust(String type) {
        String sql = "select id \"id\", name \"name\",CHOICE_TYPE \"type\",SCORE \"score\"\n" +
                "from KS_TOPIC t\n" +
                "where\n" +
                "      must_in = '1'\n" +
                "  and CHOICE_TYPE = ?\n" +
                "  and is_effective = '1'\n" +
                "  and ID in (select TOPIC_ID from KS_TOPIC_TEST where TEST_ID = '1')";
        return jdbcTemplate.queryForList(sql, type);
    }

    public List<Map<String, Object>> getTopic(Integer num, String type) {
        String sql = "select id \"id\", name \"name\", CHOICE_TYPE \"type\",SCORE \"score\"\n" +
                "from (SELECT t.*\n" +
                "      FROM KS_TOPIC t\n" +
                "      WHERE CHOICE_TYPE = ?\n" +
                "        and ID in (select TOPIC_ID from KS_TOPIC_TEST where TEST_ID = '1')\n" +
                "        and is_effective = '1'\n" +
                "        and must_in is null\n" +
                "      order by dbms_random.value) t\n" +
                "where\n" +
                "        ROWNUM <= ?";
        return jdbcTemplate.queryForList(sql, type, num);
    }

    public List<Map<String, Object>> getAnswer(Integer id) {
        String sql = "select ANSWER \"answer\",IS_RIGHT \"right\"\n" +
                "from KS_TOPIC_ANSWER\n" +
                "where TOPIC_ID = ?\n" +
                "order by dbms_random.value()";
        return jdbcTemplate.queryForList(sql, id);
    }

    public int count(String a,String b){
        String sql = "select count(*) " +
                "from KS_TOPIC_ANSWER " +
                "where TOPIC_ID = "+a;
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }
}
