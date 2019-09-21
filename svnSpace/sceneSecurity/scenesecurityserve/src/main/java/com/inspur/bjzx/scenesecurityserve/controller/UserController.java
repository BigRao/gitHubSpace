package com.inspur.bjzx.scenesecurityserve.controller;

import com.google.common.collect.ImmutableMap;
import com.inspur.bjzx.scenesecurityserve.service.UserService;
import com.inspur.bjzx.scenesecurityserve.util.RestResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    private static Logger log = Logger.getLogger(UserController.class);

    //附近500m人员
    @RequestMapping(value = "/user/nearLimit", method = RequestMethod.GET)
    public RestResult nearlimit(String longitude, String latitude, String distance) {
        try {
            List<ImmutableMap> list = userService.getLonLatLimit(longitude, latitude, distance);
            if (list != null && list.size() > 0) {
                return new RestResult<>("true", "查询成功", list);
            } else if (list != null) {
                return new RestResult<>("true", "没有签到人员信息");
            } else {
                return new RestResult<>("false", "查询错误，请重新查询");
            }
        } catch (Exception e) {
            return new RestResult<>("false", "查询失败");
        }
    }

    //查看人员信息
    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    public RestResult userInfo(String useraccount, String type, String depart, String longitude, String latitude) {
        try {
            List<ImmutableMap<String, String>> list;
            if (("1").equals(type)) {
                list = userService.getUserInfoTable(depart);
            } else {
                list = userService.getUserInfoMap(useraccount, longitude, latitude);
            }
            if (list != null && list.size() > 0) {
                return new RestResult<>("true", "查询成功", list);
            } else if (list != null) {
                return new RestResult<>("true", "没有签到人员详细信息");
            } else {
                return new RestResult<>("false", "查询出错，请重新查询");
            }
        } catch (Exception e) {
            return new RestResult<>("false", "查询失败");
        }
    }


}
