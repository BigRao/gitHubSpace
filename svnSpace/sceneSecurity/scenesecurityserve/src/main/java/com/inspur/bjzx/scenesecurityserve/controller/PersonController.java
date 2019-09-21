package com.inspur.bjzx.scenesecurityserve.controller;

import com.inspur.bjzx.scenesecurityserve.service.PersonService;
import com.inspur.bjzx.scenesecurityserve.util.ReplaceNull;
import com.inspur.bjzx.scenesecurityserve.util.RestResult;
import com.inspur.bjzx.scenesecurityserve.util.WebSocket;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class PersonController {

    @Autowired
    PersonService personService;

    final WebSocket webSocket = new WebSocket();

    final ReplaceNull replaceNull = new ReplaceNull();

    private static final Logger log = Logger.getLogger(PersonController.class);

    @GetMapping(value = "/focus/getGuaranteeTeam")
    public RestResult getGuaranteeTeam(String userAccount, String roomId, String searchText) {
        try {
            userAccount = userAccount.trim();
            searchText = searchText.trim();
            roomId = roomId.trim();
            log.info("/focus/getCompanyOrPerson\nPARAMS：\n" +
                    "userAccount：" + userAccount + "\n" +
                    "roomId：" + roomId + "\n" +
                    "searchText：" + searchText + "\n");

            List<Map<String, Object>> battleRoom = personService.getBattleRoom(roomId);
            Iterator<Map<String, Object>> it = battleRoom.iterator();
            while(it.hasNext()){
                Map<String, Object> map = it.next();
                List<Map<String, Object>> staffInfo = personService.getStaffInfo(map.get("ID"), searchText);
                if (staffInfo.size() == 0) {
                    it.remove();
                    continue;
                }
                map.put("persons", staffInfo);
            }
            return new RestResult<>("true", "查询成功", battleRoom);

        } catch (Exception e) {
            log.info("/focus/getCompanyOrPerson error\n" + e.getMessage());
            return new RestResult<>("false", "查询失败");
        } finally {
            log.info("\n\n\n\n");
        }
    }

    @GetMapping(value = "/focus/getPerson")
    public RestResult getPerson(String userAccount, String company) {
        try {
            userAccount = userAccount.trim();
            company = company.trim();
            log.info("/focus/getPerson\nPARAMS：\n" +
                    "userAccount：" + userAccount + "\n" +
                    "company：" + company);
            List<Map<String, Object>> list = personService.getPerson(company, "");
            setOnLineValue(list);
            log.info(userAccount);
            log.info(company);
            return new RestResult<>("true", "查询成功", list);
        } catch (Exception e) {
            log.info("/focus/getPerson error\n" + e.getMessage());
            return new RestResult<>("false", "查询失败");
        } finally {
            log.info("\n\n\n\n");
        }
    }


    @GetMapping(value = "/focus/getCompany")
    public RestResult test(String userAccount) {
        try {
            userAccount = userAccount.trim();
            log.info("/focus/getCompany\nPARAMS：\n" +
                    "userAccount：" + userAccount + "\n"
            );
            List<Map<String, Object>> list = personService.getCompanyByUserAccount(userAccount);
            String company = "";
            String videoNum = "";
            if (list.size() > 0) {
                company = (String) (list.get(0).get("company") == null ? "" : list.get(0).get("company"));
                videoNum = (String) (list.get(0).get("videoNum") == null ? "" : list.get(0).get("videoNum"));
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("company", company);
            jsonObject.put("videoNum", videoNum);
            return new RestResult<>("true", "查询成功", jsonObject);
        } catch (Exception e) {
            log.info("/focus/getCompany error\n" + e.getMessage());
            return new RestResult<>("false", "查询失败");
        } finally {
            log.info("\n\n\n\n");
        }

    }


    public void setOnLineValue(List<Map<String, Object>> list) {
        ArrayList<String> onLineUser = webSocket.getOnLineUser();
        for (Map<String, Object> stringObjectMap : list) {
            if (onLineUser.contains(stringObjectMap.get("hierarchyId") + "app")) {
                stringObjectMap.put("onLine", "true");
            } else {
                stringObjectMap.put("onLine", "false");
            }
        }
    }
}
