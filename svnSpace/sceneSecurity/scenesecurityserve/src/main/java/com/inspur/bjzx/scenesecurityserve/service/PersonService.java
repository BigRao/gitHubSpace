package com.inspur.bjzx.scenesecurityserve.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PersonService {
    int getPersonNum();
    List<Map<String,Object>> getPerson(String company,String searchText);
    List<Map<String,Object>> getCompany(String searchText);
    List<Map<String,Object>> getPersonByCompany(ArrayList<String>  company);
    String getPermission(String userAccount);
    List<Map<String,Object>> getCompanyByUserAccount(String userAccount);
    List<Map<String,Object>> getPersonByUerAccount(String userAccount);

    List<Map<String,Object>> getBattleRoom(String roomId);

    List<Map<String,Object>> getStaffInfo(Object id, String searchText);

}
