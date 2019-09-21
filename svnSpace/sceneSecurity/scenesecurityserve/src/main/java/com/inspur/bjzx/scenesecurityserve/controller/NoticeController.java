package com.inspur.bjzx.scenesecurityserve.controller;

import com.inspur.bjzx.scenesecurityserve.service.NoticeService;
import com.inspur.bjzx.scenesecurityserve.util.RestResult;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    private static final Logger log = Logger.getLogger(NoticeController.class);

    @GetMapping(value = "/getNotice1")
    public RestResult test(String userAccount) {
        try {


            //userAccount=userAccount.trim();
            log.info("/getNotice\nPARAMS：\n" +
                    "userAccount：" + userAccount);
            List<Map<String, Object>> list = noticeService.getNotice();
            if (list.size() > 0) {
                return new RestResult<>("true", "查询成功", list.get(0));
            } else {
                return new RestResult<>("true", "查询成功", new HashMap<String, Object>());
            }
        } catch (Exception e) {
            return new RestResult<>("false", "查询失败");
        }

    }


    @PostMapping(value = "/addNotice")
    public RestResult addNotice(@RequestBody JSONObject jsonParam){
        try {
            String userAccount = ((String) jsonParam.get("userAccount")).trim();
            String title = ((String) jsonParam.get("title")).trim();
            String noticeText = ((String) jsonParam.get("noticeText")).trim();
            log.info("/addNotice\nPARAMS：\n" +
                    "userAccount：" + userAccount + "\n" +
                    "title：" + title + "\n" +
                    "noticeText：" + noticeText);
            int insertResult = noticeService.addNotice(userAccount, title, noticeText);
            return new RestResult<>("true", "添加成功", insertResult);
        } catch (Exception e) {
            return new RestResult<>("false", "查询失败");
        }
    }
}
