package com.inspur.bjzx.scenesecurityserve.controller;


import com.alibaba.fastjson.JSON;
import com.inspur.bjzx.scenesecurityserve.Interface.PublicInterface;
import com.inspur.bjzx.scenesecurityserve.Interface.PublicInterfaceProxy;
import com.inspur.bjzx.scenesecurityserve.service.PersonService;
import com.inspur.bjzx.scenesecurityserve.util.RestResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.*;

@RestController
public class VideoCallController {

    @Autowired
    PersonService personService;

    private static final Logger log = Logger.getLogger(VideoCallController.class);

    @PostMapping(value="/focus/sendMessageForVideo")
    public RestResult sendMessageForVideo(@RequestBody JSONObject jsonObject){
        try{
            String userAccount = ((String)jsonObject.get("userAccount")).trim();
            String userName = ((String)jsonObject.get("userName")).trim();
            String roomNum = (jsonObject.get("meetRoom").toString()).trim();
            JSONArray receiveMessageGroups = (JSONArray)jsonObject.get("receiveMessageGroups");
            JSONArray receiveMessagePersons = (JSONArray)jsonObject.get("receiveMessagePersons");
            log.info("/focus/sendMessageForVideo\nPARAMS：\n" +
                    "userAccount："+userAccount+"\n" +
                    "userName："+userName+"\n" +
                    "receiveMessageGroups："+receiveMessageGroups+"\n" +
                    "receiveMessagePersons："+receiveMessagePersons);
            List<Map<String,Object>> list =new ArrayList<>();
            ArrayList<String> receiveMessageGroupsList = (ArrayList<String>) JSON.parseArray(receiveMessageGroups.toString(),String.class);
            log.info(receiveMessageGroupsList);
            if(receiveMessageGroupsList.size()>0){
                list.addAll(personService.getPersonByCompany(receiveMessageGroupsList));
            }
            list.addAll(receiveMessagePersons);
            log.info(list);
            PublicInterface t = new PublicInterfaceProxy();
            try {
                for (Map<String, Object> map : list) {
//                    String message = t.sendSMS("<sms username=\"sts\" password=\"Sts.123\"><head system=\"SEQ\" service=\"alarm_sms\"  priority=\"2\" seqno=\"\"/>"
////                            + "<mobile>"+list.get(i).get("receivePhone")+"</mobile><message>" + userName +"的会议室http://221.179.142.120:9999/apps/file/sms/sms.html?meetRoom="+roomNum+"&videoNum="+list.get(i).get("receiveVideoNum")+/*"&useraccount="+ list.get(i).get("receiveId")+*/"</message></sms>");
////                    log.info(message);
                    String message = t.sendSMS("<sms username=\"sts\" password=\"Sts.123\"><head system=\"SEQ\" service=\"alarm_sms\"  priority=\"2\" seqno=\"\"/>"
                            + "<mobile>" + map.get("receivePhone") + "</mobile><message>当前已启动“" + userName + "”作战室，指挥长请您加入，加入链接如下：http://221.179.142.120:9999/apps/file/sms/sms.html?meetRoom=" + roomNum + "@videoNum=" + map.get("receiveVideoNum") + "@useraccount=" + map.get("receiveId") + "</message></sms>");
                    log.info(message);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return new RestResult<>("true","发送成功",list);

        }catch (Exception e){
            log.info("/focus/sendMessageForVideo error\n"+e.getMessage());
            return new RestResult("false","发送失败");
        }finally {
            log.info("\n\n\n\n");
        }
    }

    @PostMapping(value="/focus/sendSms")
    public RestResult sendSms(@RequestBody JSONObject jsonObject){
        try{
            String battleId = ((String)jsonObject.get("battleId")).trim();
            String battleName = ((String)jsonObject.get("battleName")).trim();
            String taskName = (jsonObject.get("taskName").toString()).trim();
            JSONArray receiveMessagePersons = (JSONArray)jsonObject.get("receiveMessagePersonArray");
            List<Map<String,Object>> list =new ArrayList<>();
            list.addAll(receiveMessagePersons);
            PublicInterface t = new PublicInterfaceProxy();
            try {
                for (Map<String, Object> map : list) {
                    String message = t.sendSMS("<sms username=\"sts\" password=\"Sts.123\"><head system=\"SEQ\" service=\"alarm_sms\"  priority=\"2\" seqno=\"\"/>"
                            + "<mobile>" + map.get("receivePhone") + "</mobile><message>"+battleName+"http://221.179.142.120:9999/apps/file/sms/sms.html?battleId=" + battleId + "@battleName="+ URLEncoder.encode(battleName,"utf-8") + "@useraccount=" + map.get("receiveId") +"</message></sms>");
                    log.info(message);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return new RestResult<>("true","发送成功");
        }catch (Exception e){
            log.info("/focus/sendSms error\n"+e.getMessage());
            return new RestResult("false","发送失败");
        }finally {
            log.info("\n\n\n\n");
        }
    }

    @PostMapping(value="/focus/sendSms2")
    public RestResult sendSms2(@RequestBody List<JSONObject> jsonObject){
        try{
            log.info(jsonObject);
            PublicInterface t = new PublicInterfaceProxy();
            try {
                for (JSONObject map : jsonObject) {
                    String message = t.sendSMS("<sms username=\"sts\" password=\"Sts.123\"><head system=\"SEQ\" service=\"alarm_sms\"  priority=\"2\" seqno=\"\"/>"
                            + "<mobile>" + map.get("phone") + "</mobile><message>"+map.get("message")+"</message></sms>");
                    log.info(message);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return new RestResult<>("true","发送成功");
        }catch (Exception e){
            log.info("/focus/sendSms error\n"+e.getMessage());
            return new RestResult("false","发送失败");
        }finally {
            log.info("\n\n\n\n");
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println( URLEncoder.encode("作战室","utf-8"));
    }
}
