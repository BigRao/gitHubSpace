package com.inspur.plugins.kaoshi.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class TestDao {
    @Resource
    JdbcTemplate jdbcTemplate;

    public Map<String, Object> getSearchTest(String userId) {
        String sql = "select t.NAME \"test_name\",t.TIME_LIMIT \"test_limit\",t.FULL_SCORE \"test_total_score\",\n" +
                "       (select count(ID) from KS_TEST_LOG where USER_ID=?) \"test_num\",\n" +
                "       nvl(c.SCORE,'0') \"test_result\", nvl(c.IS_PASS,'0') \"is_pass\",\n" +
                "       TO_CHAR(t.BEGIN_TIME, 'YYYY-MM-DD HH24:MI:SS') \"start_time\",\n" +
                "       TO_CHAR(t.END_TIME, 'YYYY-MM-DD HH24:MI:SS') \"end_time\",\n" +
                "       t.SINGLE_NUM \"single_num\", t.MULTIPLE_NUM \"multiple_num\",t.JUDGE_NUM \"judge_num\"\n" +
                "from (select * from KS_TEST) t\n" +
                "full join\n" +
                "    (select * from(select TEST_ID,SCORE,IS_PASS\n" +
                "    from KS_TEST_LOG where USER_ID=?\n" +
                "order by END_TIME desc)where ROWNUM<=1) c\n" +
                "on t.ID=c.TEST_ID";
        return jdbcTemplate.queryForMap(sql, userId, userId);
    }

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

    public int submitScore(String userId, String testId, String startTime, String endTime, Integer score, Integer isPass, String validity) {
        String sql = "insert into KS_TEST_LOG(user_id, username, test_id, test_name, type_id, type_name, score, start_time, end_time, test_duration, is_pass,VALIDITY)\n" +
                "select ?,(select NAME from KS_USER where KS_USER.ID = ? ) \"username\",\n" +
                "       ID \"test_id\",NAME \"test_name\",TYPE_ID,\n" +
                "       (select KS_TYPE.NAME\n" +
                "        from KS_TYPE\n" +
                "        where KS_TYPE.ID = t.TYPE_ID) \"type_name\",\n" +
                "       ?,to_date ( ? , 'YYYY-MM-DD HH24:MI:SS'),\n" +
                "       to_date ( ? , 'YYYY-MM-DD HH24:MI:SS' ),\n" +
                "       PASS_SCORE \"test_duration\",?," +
                "       to_date ( ? , 'YYYY-MM-DD HH24:MI:SS' ) \"VALIDITY\"\n" +
                "from KS_TEST t\n" +
                "where ID = ?";
        return jdbcTemplate.update(sql, userId, userId, score, startTime, endTime, isPass, validity, testId);
    }

    public Map<String, Object> getPassScore(String testId) {
        String sql = "select PASS_SCORE \"pass_score\"\n" +
                "from KS_TEST \n" +
                "where ID = ?";
        return jdbcTemplate.queryForMap(sql, testId);
    }

    public int submitChoice(String userId, String topicId, String topicChoice, String topicIsRight) {
        String sql = "insert into KS_TEST_LOG_INFO(test_log_id, topic_id, choice, is_right)\n" +
                "values (?,?,?,?)";
        return jdbcTemplate.update(sql, userId, topicId, topicChoice, topicIsRight);
    }

    public Map<String, Object> getTestId(String userId) {
        String sql = "SELECT max(ID) \"testId\"\n" +
                "       FROM KS_TEST_LOG t\n" +
                "       WHERE USER_ID = ?";
        return jdbcTemplate.queryForMap(sql, userId);
    }

    public List<Map<String, Object>> searchTestScore(String userId) {
        String sql = "select ID \"test_log_id\",TO_CHAR(START_TIME, 'YYYY-MM-DD HH24:MI:SS') \"test_start_time\",\n" +
                "       TO_CHAR(END_TIME, 'YYYY-MM-DD HH24:MI:SS') \"test_end_time\",SCORE \"test_score\",\n" +
                "       IS_PASS \"test_is_pass\"\n" +
                "from KS_TEST_LOG\n" +
                "where USER_ID=?" +
                "and TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS') <= TO_CHAR(VALIDITY,'YYYY-MM-DD HH24:MI:SS')\n" +
                "order by ID DESC";
        return jdbcTemplate.queryForList(sql, userId);
    }

    public List<Map<String, Object>> searchTestLog(String testLogId) {
        String sql = "select TOPIC_ID \"topic_id\",IS_RIGHT \"topic_is_right\"\n" +
                "from KS_TEST_LOG_INFO\n" +
                "where TEST_LOG_ID = ?";
        return jdbcTemplate.queryForList(sql, testLogId);
    }

    public Map<String, Object> getLogNum(String testLogId) {
        String sql = "select count(ID) \"count\"\n" +
                "from KS_TEST_LOG\n" +
                "where ID=?\n" +
                "and TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS') < TO_CHAR(VALIDITY,'YYYY-MM-DD HH24:MI:SS')";
        return jdbcTemplate.queryForMap(sql, testLogId);
    }
}
