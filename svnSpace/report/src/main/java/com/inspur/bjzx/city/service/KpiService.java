package com.inspur.bjzx.city.service;

import com.google.common.collect.ImmutableMap;
import net.sf.json.JSONArray;

import java.util.List;

/**
 * Created by liurui on 2017/8/3.
 */
public interface KpiService {
    //获取指标分组信息
    List<ImmutableMap<String, String>> getKpiGrpInfo(String userId);

    //获取指标单页签
    JSONArray getKpiLists(String userId, String kpiGrpId, String time, String regionId);
}
