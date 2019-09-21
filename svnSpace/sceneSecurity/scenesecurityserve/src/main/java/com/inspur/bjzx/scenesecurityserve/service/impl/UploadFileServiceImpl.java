package com.inspur.bjzx.scenesecurityserve.service.impl;

import com.inspur.bjzx.scenesecurityserve.service.UploadFileService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UploadFileServiceImpl implements UploadFileService{

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    private static final Logger log = Logger.getLogger(UploadFileServiceImpl.class);
    /**
     * 保存上传文件信息
     */
    @Override
    public boolean uploadFile(JSONObject jsonObject, final String filePath, final String fileType) {
        int UpdateResultDetemine;
        String id = UUID.randomUUID().toString();
        String useraccount = (String)jsonObject.get("useraccount");
        String longitude = jsonObject.get("longitude").toString();
        String latitude = jsonObject.get("latitude").toString();
        try {
            String InsertSqlOfUploadPho = "INSERT INTO PM_UP_FILE_INFO(ID,USERID,FILE_PATH,CREATE_TIME,LONGITUDE,LATITUDE,FILE_TYPE) values(?,?,?,sysdate,TRUNC(?,6),TRUNC(?,6),?)";
            UpdateResultDetemine = jdbcTemplate.update(InsertSqlOfUploadPho, id, useraccount, filePath, longitude, latitude, fileType);
            if (UpdateResultDetemine >= 1) {
                return true;
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return false;
    }

}
