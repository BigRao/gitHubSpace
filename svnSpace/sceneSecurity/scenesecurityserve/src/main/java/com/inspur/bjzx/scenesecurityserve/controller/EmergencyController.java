package com.inspur.bjzx.scenesecurityserve.controller;


import com.inspur.bjzx.scenesecurityserve.util.DataUtil;
import com.inspur.bjzx.scenesecurityserve.util.SendResult;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;

@RestController
public class EmergencyController {
    private static final Logger logger = LoggerFactory.getLogger(EmergencyController.class);

    @PostMapping(value = "/emergency/task_state_update/" )
    public Object taskStateUpdate(@RequestBody JSONObject body){
        String url = "http://10.224.128.101:8089/emergency/task_state_update/";
        JSONObject postData = new JSONObject();
        postData.put("TaskID", DataUtil.O2I(body.get("TaskID")));
        postData.put("Type", DataUtil.O2I(body.get("Type")));
        postData.put("ID", DataUtil.O2S(body.get("ID")));
        postData.put("State", DataUtil.O2I(body.get("State")));
        postData.put("Reply", DataUtil.O2S(body.get("Reply")));
        logger.info(postData.toString());
        String string = null;
        try {
            string = SendResult.sendPostRequest(postData,url);
        } catch (IOException e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        logger.info(string);
        return string;
    }

    @PostMapping(value = "/emergency/task_info_get_app/")
    public String taskInfoGetApp(@RequestBody JSONObject body){
        String url = "http://10.224.128.101:8089/emergency/task_info_get_app/";
        JSONObject postData = new JSONObject();
        postData.put("Type", DataUtil.O2I(body.get("Type")));
        postData.put("ResID", DataUtil.O2S(body.get("ResID")));
        postData.put("State", DataUtil.O2S(body.get("State")));
        postData.put("Page", DataUtil.O2I(body.get("Page")));
        postData.put("Count", DataUtil.O2I(body.get("Count")));
        logger.info(postData.toString());
        String string = null;
        try {
            string = SendResult.sendPostRequest(postData,url);
        } catch (IOException e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        logger.info(string);
        return string;
    }

    @PostMapping(value = "/eme_room_get_app/")
    public String eme_room_get_app(@RequestBody JSONObject body){
        String url = "http://10.224.128.101:8089/battlehall/eme_room_get_app/";
        JSONObject postData = new JSONObject();
        postData.put("AdAccount", DataUtil.O2S(body.get("AdAccount")));
        postData.put("OrderBy", DataUtil.O2I(body.get("OrderBy")));
        postData.put("TopN", DataUtil.O2I(body.get("TopN")));
        logger.info(postData.toString());
        String string = null;
        try {
            string = SendResult.sendPostRequest(postData,url);
        } catch (IOException e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        logger.info(string);
        return string;
    }

    @PostMapping(value = "/eme_room_org_app/")
    public String eme_room_org_app(@RequestBody JSONObject body){
        String url = "http://10.224.128.101:8089/battlehall/eme_room_org_app/";
        JSONObject postData = new JSONObject();
        postData.put("AdAccount", DataUtil.O2S(body.get("AdAccount")));
        postData.put("RoomID", DataUtil.O2I(body.get("RoomID")));
        logger.info(postData.toString());
        String string = null;
        try {
            string = SendResult.sendPostRequest(postData,url);
        } catch (IOException e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        logger.info(string);
        return string;
    }

    @PostMapping(value = "/eme_org_staff_set_app/")
    public String eme_org_staff_set_app(@RequestBody String body){
        String url = "http://10.224.128.101:8089/battlehall/eme_org_staff_set_app/";
        String string = null;
        try {
            string = SendResult.sendPostRequest(body,url);
        } catch (IOException e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        logger.info(string);
        return string;
    }

    @PostMapping(value = "/scene_task_info_get/")
    public String scene_task_info_get(@RequestBody JSONObject body){
        String url = "http://10.224.128.101:8089/scene/scene_task_info_get/";
        JSONObject postData = new JSONObject();
        postData.put("SceneID", DataUtil.O2I(body.get("SceneID")));
        postData.put("Type", DataUtil.O2I(body.get("Type")));
        logger.info(postData.toString());
        String string = null;
        try {
            string = SendResult.sendPostRequest(postData,url);
        } catch (IOException e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        logger.info(string);
        return string;
    }
    @PostMapping(value = "/staff_info_get/")
    public String staff_info_get(@RequestBody JSONObject body){
        String url = "http://10.224.128.101:8089/emergency/staff_info_get/";
        String string = null;
        try {
            string = SendResult.sendPostRequest(body,url);
        } catch (IOException e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        logger.info(string);
        return string;
    }
    @PostMapping(value = "/recodeLogs" )
    public Object recodeLogs(@RequestBody JSONObject body){
        String url = "http://10.110.2.42:8804/operationalog/recodeLogs";
        logger.info(String.valueOf(body));
        String string = null;
        try {
            string = SendResult.sendPostRequest(body,url);
        } catch (IOException e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        logger.info(string);
        return string;
    }
}

