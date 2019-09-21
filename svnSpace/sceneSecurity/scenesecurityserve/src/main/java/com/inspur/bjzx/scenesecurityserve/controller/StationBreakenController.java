package com.inspur.bjzx.scenesecurityserve.controller;


import com.inspur.bjzx.scenesecurityserve.service.PersonService;
import com.inspur.bjzx.scenesecurityserve.service.SignService;
import com.inspur.bjzx.scenesecurityserve.service.StationBreakenService;
import com.inspur.bjzx.scenesecurityserve.service.StationFaultService;
import com.inspur.bjzx.scenesecurityserve.util.RestResult;
import com.inspur.bjzx.scenesecurityserve.util.ReplaceNull;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class StationBreakenController {

    @Autowired
    StationBreakenService stationBreakenService;

    @Autowired
    StationFaultService stationFaultService;

    @Autowired
    PersonService personService;

    @Autowired
    SignService signService;

    private final ReplaceNull replaceNull = new ReplaceNull();
    private static final Logger log = org.apache.log4j.Logger.getLogger(StationBreakenController.class);

    @GetMapping(value = "/focus/getNum")
    @ResponseBody
    public RestResult getNum(String userAccount) {
        try {
            userAccount = userAccount.trim();
            log.info("/focus/getNum\nPARAMS：\n" +
                    "userAccount：" + userAccount);
            //String type = personService.getPermission(userAccount);
            //log.info("type:"+type);
            int stationBreakenNum, stationFaultNum, networkOverviewNum, faultAreaNum;
            //if (type.equals("all")) {
                stationBreakenNum = stationBreakenService.getStationBreakenNumAll();
                stationFaultNum = stationFaultService.getStationFaultNumAll();
            //} else {
            //    stationBreakenNum = stationBreakenService.getStationBreakenNum(userAccount);
            //    stationFaultNum = stationFaultService.getStationFaultNum(userAccount);
            //}
            networkOverviewNum = stationBreakenService.getStationBreakenNumAll();
            faultAreaNum = stationBreakenService.getFaultAreaNum();
            log.info(stationBreakenNum);
            log.info(stationFaultNum);
            int personNum = personService.getPersonNum();
            int signNum = signService.getSignNum(userAccount);
            JSONObject allNum = new JSONObject();
            allNum.put("faultNum", stationBreakenNum);
            allNum.put("performanceNum", stationFaultNum);
            allNum.put("personNum", personNum);
            allNum.put("networkOverviewNum", networkOverviewNum);
            allNum.put("faultAreaNum", faultAreaNum);
            allNum.put("signNum", signNum);
            return new RestResult<>("true", "查询成功", "", allNum);
        } catch (Exception e) {
            log.info("/focus/getNum error\n"+e.getMessage());
            return new RestResult<>("false", "查询失败");
        }finally {
            log.info("\n\n\n\n");
        }

    }


    @GetMapping(value = "/focus/searchFaultWorks")
    @ResponseBody
    public RestResult getDetail(String userAccount, String associatedId) {
        try {
            userAccount = userAccount.trim();
            String id = associatedId.trim();
            log.info("/focus/searchFaultWorks\nPARAMS：\n" +
                    "userAccount：" + userAccount + "\n" +
                    "id：" + id);
            //String type = personService.getPermission(userAccount);
            //log.info(type);
            List<Map<String, Object>> list;
            if (!id.equals("")) {
                list = stationBreakenService.getDetail(id);
            } else {
                //if (type.equals("all")) {
                    list = stationBreakenService.getDetail();
                //} else {
                //    list = stationBreakenService.getDetailByUserAccount(userAccount);
                //}
            }

            replaceNull.replaceNull(list);
            return new RestResult<>("true", "查询成功", "", list);
        } catch (Exception e) {
            log.info("/focus/searchFaultWorks error\n"+e.getMessage());
            return new RestResult<>("false", "查询失败");
        }finally {
            log.info("\n\n\n\n");
        }
    }

    @GetMapping(value = "/focus/searchPerformanceWorks")
    @ResponseBody
    public RestResult searchFaultWorks(String userAccount, String associatedId) {
        try {
            userAccount = userAccount.trim();
            String id = associatedId.trim();
            log.info("/focus/searchPerformanceWorks\nPARAMS：\n" +
                    "userAccount：" + userAccount + "\n" +
                    "id：" + id + "\n");
            //String type = personService.getPermission(userAccount);
            List<Map<String, Object>> list;
            if (!id.equals("")) {
                list = stationFaultService.getDetail(id);
            } else {
                //if (type.equals("all")) {
                    list = stationFaultService.getDetail();
                //} else {
                //    list = stationFaultService.getDetailByUserAccount(userAccount);
                //}
            }
            replaceNull.replaceNull(list);
            return new RestResult<>("true", "查询成功", list);
        } catch (Exception e) {
            log.info("/focus/searchPerformanceWorks error\n"+e.getMessage());
            return new RestResult<>("false", "查询失败");
        }finally {
            log.info("\n\n\n\n");
        }
    }

}
