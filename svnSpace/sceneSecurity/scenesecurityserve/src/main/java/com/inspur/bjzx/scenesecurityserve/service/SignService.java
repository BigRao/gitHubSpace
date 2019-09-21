package com.inspur.bjzx.scenesecurityserve.service;

import java.util.List;
import java.util.Map;

public interface SignService {


     int  realTimeSign(Map<String,Object> person,String longitude,String latitude,String location,int isSignBack);

     List<Map<String,Object>> searchSignLine(String userId);

     List<Map<String,Object>> searchSign();

     List<Map<String,Object>> getSecurityPersons(String searchUserName);

     int getSignNum(String userAccount);
}
