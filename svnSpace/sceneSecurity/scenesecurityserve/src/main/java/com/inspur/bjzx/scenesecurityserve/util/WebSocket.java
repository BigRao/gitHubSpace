package com.inspur.bjzx.scenesecurityserve.util;


import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value="/websocket/{userAccount}/{company}/{loginType}")
@Component
public class WebSocket {


    private static final Logger log = Logger.getLogger(WebSocket.class);

//    private static int onlineCount = 0;

    //记录每个在线用户对应的sessions
    private static final Map<String, Session> sessions = Collections.synchronizedMap(new HashMap<>());

    //记录App每个公司在线人员
    private static final Map<String,ArrayList<String>> onLineRecordApp = Collections.synchronizedMap(new HashMap<>());

    //记录pc每个公司在线人员
    private static final Map<String,ArrayList<String>> onLineRecordPC = Collections.synchronizedMap(new HashMap<>());

    //记录每个人最近通信时间
    private static final Map<String,String> timeRecord = Collections.synchronizedMap(new HashMap<>());

    //记录在线的人
    private static final ArrayList<String> onLineUser = new ArrayList<>();

    private static final CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    private Session session;

//    private static Boolean time = false;


//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            try{
//                while(true){
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Thread.sleep(20*1000);
//                    Date currentDate = new Date();
//                    String currentTime = sdf.format(currentDate);
//                    log.info(currentTime);
//                    for(String key:timeRecord.keySet()){
//                        Date thisDate = sdf.parse(timeRecord.get(key));
//                        int diff =(int)(currentDate.getTime()-thisDate.getTime());
//                        if(diff>20*1000){
//                            log.info(key +"已经下线了，关闭它的连接");
//                            sessions.get(key).close();
//                            timeRecord.remove(key);
//                        }
//                    }
//                }
//            }catch (Exception e){
//                log.info("runnable error\n"+e.getMessage());
//            }
//        }
//    };
//     Thread thread = new Thread(runnable);

    @OnOpen
    public void onOpen(Session session,@PathParam("userAccount") String userAccount,@PathParam("company") String company,@PathParam("loginType") String loginType) throws IOException{


        try{
            log.info("onOpen-----"+userAccount+"-----"+company+"-----"+loginType);

//            if(!time){
//                time = true;
//                thread.start();
//            }


            //给各个session发送此用户上线消息


            if(sessions.get(userAccount+loginType)!=null){
                //此userAccount已经在一个设备上登录，
                JSONObject responseObj = new JSONObject();
                responseObj.put("type","replace");
                responseObj.put("message","已在另外终端登录，您已下线");
                sessions.get(userAccount+loginType).getBasicRemote().sendText(responseObj.toString());
                sessions.get(userAccount+loginType).close();
            }

            sendMessageToAll(userAccount,company,loginType,"add");

            if(loginType.equals("app")){
                ArrayList<String> arr = (onLineRecordApp.containsKey(company))?onLineRecordApp.get(company):(new ArrayList<>());
                arr.add(userAccount);
                onLineRecordApp.put(company,arr);
            }else{
                ArrayList<String> arr = (onLineRecordPC.containsKey(company))?onLineRecordPC.get(company):(new ArrayList<>());
                arr.add(userAccount);
                onLineRecordPC.put(company,arr);
            }

            onLineUser.add(userAccount+loginType);

            //添加或覆盖key值userAccount+loginType 得新session
            sessions.put(userAccount+loginType,session);


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            timeRecord.put(userAccount+loginType,sdf.format(new Date()));


            log.info(onLineRecordApp);
            log.info(onLineRecordPC);
            log.info(onLineUser);

            this.session = session;
            webSocketSet.add(this);
//        onlineCount++;
//        log.info(onlineCount);

        }catch (Exception e){
            log.info("onOpen error\n"+e.getMessage());
        }

    }

    @OnClose
    public void onClose(Session session,@PathParam("userAccount") String userAccount,@PathParam("company") String company,@PathParam("loginType") String loginType){

        try{


            log.info("onClose-----"+userAccount+"-----"+company+"-----"+loginType);

            sessions.remove(userAccount+loginType);

            if(loginType.equals("app")){
                ArrayList<String> arr = (onLineRecordApp.containsKey(company))?onLineRecordApp.get(company):(new ArrayList<>());
                arr.remove(userAccount);
                onLineRecordApp.put(company,arr);
            }else{
                ArrayList<String> arr = (onLineRecordPC.containsKey(company))?onLineRecordPC.get(company):(new ArrayList<>());
                arr.remove(userAccount);
                onLineRecordPC.put(company,arr);
            }

            onLineUser.remove(userAccount+loginType);

            log.info(onLineRecordApp);
            log.info(onLineRecordPC);
            log.info(onLineUser);

            timeRecord.remove(userAccount+loginType);

            webSocketSet.remove(this);

            //给各个session发送此用户下线消息
            sendMessageToAll(userAccount,company,loginType,"remove");
        }catch (Exception e){
            log.info("onClose error\n"+e.getMessage());
        }



    }

    @OnMessage
    public void onMessage(String message,Session session,@PathParam("userAccount") String userAccount,@PathParam("company") String company,@PathParam("loginType") String loginType) throws IOException{

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            timeRecord.put(userAccount+loginType,sdf.format(new Date()));
            session.getBasicRemote().sendText("em......我还没挂");
        }catch (Exception e){
            log.info("onMessage error\n"+e.getMessage());
        }

    }


    @OnError
    public void onError(Session session,Throwable error){
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }

    public void sendMessageToAll(String userAccount,String company,String loginType,String type){
        try{
            log.info(company);
            JSONObject responseObj = new JSONObject();
            responseObj.put("company",company);
            responseObj.put("userAccount",userAccount);
            responseObj.put("loginType",loginType);
            responseObj.put("type",type);
            for(WebSocket item: webSocketSet){
                try{
                    item.sendMessage(responseObj.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            log.info("sendMessageToAll error\n"+e.getMessage());
        }


    }

    public ArrayList<String> getOnLineUser(){
        return onLineUser;
    }

    public Map<String,ArrayList<String>> getOnLineRecordApp(){
        return onLineRecordApp;
    }

}
