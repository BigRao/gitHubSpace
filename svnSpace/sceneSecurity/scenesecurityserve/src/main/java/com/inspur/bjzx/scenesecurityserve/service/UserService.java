package com.inspur.bjzx.scenesecurityserve.service;

import com.google.common.collect.ImmutableMap;

import java.util.List;

public interface UserService {

    List<ImmutableMap> getLonLatLimit(String longitude, String latitude, String distance);

    List<ImmutableMap<String, String>> getUserInfoTable(String depart);

    List<ImmutableMap<String, String>> getUserInfoMap(String useraccount,String longitude,String latitude);

}
