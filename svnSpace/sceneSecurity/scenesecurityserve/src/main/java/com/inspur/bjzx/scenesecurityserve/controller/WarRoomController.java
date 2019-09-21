package com.inspur.bjzx.scenesecurityserve.controller;

import com.inspur.bjzx.scenesecurityserve.service.KpiService;
import com.inspur.bjzx.scenesecurityserve.service.WarRoomService;
import com.inspur.bjzx.scenesecurityserve.util.DataUtil;
import com.inspur.bjzx.scenesecurityserve.util.RestResult;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class WarRoomController {
    private static Logger log = Logger.getLogger(WarRoomController.class);

    @Autowired
    private WarRoomService warRoomService;

    @GetMapping(value = "/getNotice")
    public RestResult getNotice(String userAccount) {
        try {
            List<Map<String, Object>> list = warRoomService.getWarRoomSceneAllInfo(userAccount);

            return new RestResult<>("true", "查询成功", list);
        } catch (Exception e) {
            return new RestResult<>("false", "查询失败");
        }

    }

    @GetMapping(value = "/markNotice")
    public RestResult markNotice(String userAccount,Integer noticeId) {
        try {
            boolean ANS = warRoomService.changeHadLook(userAccount,noticeId);
            if(ANS) {
                return new RestResult<>("true", "标记成功");
            }else{
                return new RestResult<>("false", "标记失败");
            }
        } catch (Exception e) {
            return new RestResult<>("false", "标记失败");
        }

    }
}
