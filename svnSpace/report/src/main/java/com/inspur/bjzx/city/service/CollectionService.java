package com.inspur.bjzx.city.service;

/**
 * Created by liurui on 2017/8/3.
 */
public interface CollectionService {
    //收藏/取消收藏
    boolean attentionKpi(String userId, String mataKpiId, String opType);
}
