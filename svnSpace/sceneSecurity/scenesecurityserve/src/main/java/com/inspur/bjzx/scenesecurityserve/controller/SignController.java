package com.inspur.bjzx.scenesecurityserve.controller;

import com.inspur.bjzx.scenesecurityserve.service.PersonService;
import com.inspur.bjzx.scenesecurityserve.service.SignService;
import com.inspur.bjzx.scenesecurityserve.util.ReplaceNull;
import com.inspur.bjzx.scenesecurityserve.util.RestResult;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class SignController {

    @Autowired
    SignService signService;

    @Autowired
    PersonService personService;

    private static final Logger log = Logger.getLogger(SignController.class);

    private final ReplaceNull replaceNull = new ReplaceNull();

    //实时打点接口  签到
    @PostMapping(value = "/focus/realTimeSign")
    public RestResult realTimeSign(@RequestBody JSONObject jsonObject) {
        try {
            String userAccount = ((String)jsonObject.get("userAccount")).trim();
            String longitude = ((String)jsonObject.get("longitude")).trim();
            String latitude = ((String)jsonObject.get("latitude")).trim();
            String isSignBack = ((String)jsonObject.get("isSignBack")).trim();
            String location = ((String)jsonObject.get("location")).trim();
            log.info("/focus/realTimeSign\nPARAMS：\n" +
                    "userAccount：" + userAccount + "\n" +
                    "longitude：" + longitude + "\n" +
                    "latitude：" + latitude + "\n" +
                    "isSignBack：" + isSignBack + "\n" +
                    "location：" + location);

            List<Map<String,Object>> list = personService.getPersonByUerAccount(userAccount);
            signService.realTimeSign(list.get(0),longitude,latitude,location,("true".equals(isSignBack))?1:0);

            if ("true".equals(isSignBack)) {
                return new RestResult<>("true", "签退成功", new ArrayList<>());

            } else {
                return new RestResult<>("true", "签到成功", new ArrayList<>());
            }
        } catch (Exception e) {
            log.info("/focus/realTimeSign error\n"+e.getMessage());
            return new RestResult<>("false", "操作失败", new ArrayList<>());
        }finally {
            log.info("\n\n\n\n");
        }
    }


    //签到查询接口
    @GetMapping(value = "/focus/searchSign")
    public RestResult searchSign(String userAccount) {
        try {
            userAccount = userAccount.trim();
            log.info("/focus/searchSign\nPARAMS：\n" +
                    "userAccount：" + userAccount);
            List<Map<String, Object>> list = signService.searchSign();
            replaceNull.replaceNull(list);
            return new RestResult<>("true", "查询成功", list);
        } catch (Exception e) {
            log.info("/focus/searchSign error\n"+e.getMessage());
            return new RestResult<>("false", "查询失败");
        }finally {
            log.info("\n\n\n\n");
        }
    }

    @GetMapping(value = "/focus/searchSignLine")
    public RestResult searchSignLine(String userAccount, String signUserId) {
        try {
            userAccount = userAccount.trim();
            signUserId = signUserId.trim();
            log.info("/focus/searchSignLine\nPARAMS：\n" +
                    "userAccount：" + userAccount + "\n" +
                    "signUserId：" + signUserId + "\n");
            List<Map<String, Object>> list = signService.searchSignLine(signUserId);
            return new RestResult<>("true", "查询成功", list);
        } catch (Exception e) {
            log.info("/focus/searchSignLine error\n"+e.getMessage());
            return new RestResult<>("false", "查询失败");
        }finally {
            log.info("\n\n\n\n");
        }
    }


    @GetMapping(value = "/special/getSecurityPersons")
    public RestResult getSecurityPersons(String userAccount, String searchUserName) {
        try {
            userAccount = userAccount.trim();
            searchUserName = searchUserName.trim();
            log.info("/special/getSecurityPersons\nPARAMS：\n" +
                    "userAccount：" + userAccount + "\n" +
                    "searchUserName：" + searchUserName + "\n");

            List<Map<String, Object>> personList = signService.getSecurityPersons(searchUserName);
            replaceNull.replaceNull(personList);
            List<Map<String, Object>> list = new ArrayList<>();
            String previousUserId;
            String currentUserId = "";
            for (Map<String, Object> objectMap : personList) {
                previousUserId = currentUserId;
                currentUserId = String.valueOf(objectMap.get("userId"));
                if (!currentUserId.equals(previousUserId)) {
                    list.add(objectMap);
                    list.get(list.size() - 1).put("video", new ArrayList<>());
                    list.get(list.size() - 1).put("picture", new ArrayList<>());
                }
                if ("video".equals(objectMap.get("file_type"))) {
                    String path = String.valueOf(objectMap.get("file_path"));
                    ArrayList<String> videoArr = (ArrayList) list.get(list.size() - 1).get("video");
                    videoArr.add(path);
                    list.get(list.size() - 1).put("video", videoArr);
                } else if ("picture".equals(objectMap.get("file_type"))) {
                    String path = String.valueOf(objectMap.get("file_path"));
                    ArrayList<String> videoArr = (ArrayList) list.get(list.size() - 1).get("picture");
                    videoArr.add(path);
                    list.get(list.size() - 1).put("picture", videoArr);
                }
            }
            for (Map<String, Object> stringObjectMap : list) {
                stringObjectMap.remove("file_type");
                stringObjectMap.remove("file_path");
            }
            return new RestResult<>("true", "查询成功", list);
        } catch (Exception e) {
            log.info("/special/getSecurityPersons error\n"+e.getMessage());
            return new RestResult<>("false", "查询失败");
        }finally {
            log.info("\n\n\n\n");
        }
    }

}
