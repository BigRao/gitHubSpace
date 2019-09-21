package com.inspur.bjzx.scenesecurityserve.service;

import com.google.common.collect.ImmutableMap;

import java.util.List;

/**
 * Created by Lenovo on 2017/4/21.
 */
public interface SiteService {
     List<ImmutableMap> getSite(String netype, String site_no);

     List<ImmutableMap> getAccessPointByLaLong(double latitude, double longitude, String radius, String neTypes);
}
