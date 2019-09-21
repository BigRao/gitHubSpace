package com.inspur.bjzx.scenesecurityserve.controller;

import com.google.common.collect.ImmutableMap;
import com.inspur.bjzx.scenesecurityserve.service.SiteService;
import com.inspur.bjzx.scenesecurityserve.util.RestResult;
import com.inspur.util.Base64;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by Lenovo on 2017/4/21.
 */
@RestController
public class SiteController {
    @Autowired
    SiteService siteService;

    //查看基站信息
    @GetMapping(value = "/site/info")
    public RestResult siteInfo(String netype, String site_no) {
        List<ImmutableMap> list;
        list = siteService.getSite(netype, site_no);
        if (list != null && list.size() > 0) {
            return new RestResult<>("true", "查询成功", list);
        } else if(list != null){
            return new RestResult<>("true", "没有基站详细信息");
        } else {
            return new RestResult<>("false", "查询出错，请重新查询");
        }
    }


    @GetMapping(value = "/site/getAccessPointByLaLong",produces = "application/json; charset=utf-8")
    public RestResult getAccessPointByLaLong(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String latitude_encode;
        String longitude_encode;
        String radius_encode;
        String latitude =  request.getParameter("latitude");
        System.out.println(latitude);
        String longitude =  request.getParameter("longitude");
        System.out.println(longitude);
        String radius =  request.getParameter("radius");
        latitude = latitude.replace(" ","+");
        longitude = longitude.replace(" ","+");
        radius = radius.replace(" ","+");

        if("undefined".equals(request.getHeader("timestamp")) || "".equals(request.getHeader("timestamp")) ||request.getHeader("timestamp") == null){
            latitude_encode = Base64.decode(latitude, Long.parseLong(request.getParameter("mytime")));
            longitude_encode = Base64.decode(longitude, Long.parseLong(request.getParameter("mytime")));
            radius_encode = Base64.decode(radius, Long.parseLong(request.getParameter("mytime")));
        }else{
            latitude_encode = Base64.decode(latitude, Long.parseLong(request.getHeader("timestamp")));
            longitude_encode = Base64.decode(longitude, Long.parseLong(request.getHeader("timestamp")));
            radius_encode = Base64.decode(radius, Long.parseLong(request.getHeader("timestamp")));
        }
        String neTypes =  request.getParameter("neTypes");
        long mytimestamp = new Date().getTime();
        response.addHeader("timestamp", String.valueOf(mytimestamp));
        String responseBody = "";
        try{
            List<ImmutableMap> list = siteService.getAccessPointByLaLong(Double.parseDouble(latitude_encode),Double.parseDouble(longitude_encode), radius_encode, neTypes);
            if(list != null && list.size() > 0){
                JSONArray jsonArray = JSONArray.fromObject(list);
                responseBody = jsonArray.toString();
                responseBody = Base64.encode(responseBody,mytimestamp);
            }
            return new RestResult<>("true","查询成功",responseBody);
        } catch (Exception e){
            //response.getWriter().write(e.toString());
            e.printStackTrace();
            return new RestResult<>("false","查询失败");
        }
        //return sb.toString();
    }
}
