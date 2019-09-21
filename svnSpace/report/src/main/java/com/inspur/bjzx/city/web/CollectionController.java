package com.inspur.bjzx.city.web;

import com.inspur.bjzx.city.service.CollectionService;
import com.inspur.bjzx.city.service.KpiService;
import com.inspur.bjzx.city.util.RestResult;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liurui on 2017/8/3.
 */
@RestController
public class CollectionController {
    private static Log log = LogFactory.getLog(CollectionController.class);

    @Autowired
    KpiService kpiService;

    @Autowired
    CollectionService collectionService;

    //我的收藏显示
    @RequestMapping(value = "/rest/attentionKpiList", method = {RequestMethod.GET})
    public RestResult attentionKpiList(String userId, String time, String regionId) {
        log.info("/rest/attentionKpiList接口输入参数：{userId = "+userId+",time = "+time+",regionId = "+regionId);
        try {
            JSONArray AttentionKpiLists = kpiService.getKpiLists(userId, "", time, regionId);
            if (AttentionKpiLists != null) {
                if (AttentionKpiLists.size() > 0) {
                    return new RestResult<>("success", "查询成功", AttentionKpiLists);
                } else {
                    return new RestResult<>("success", "没有指标分组信息");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RestResult<>("fault", "查询出错，请重新查询");
    }

    //收藏/取消收藏
    @RequestMapping(value = "/rest/attentionKpi", method = {RequestMethod.GET})
    public RestResult attentionKpi(String userId, String mataKpiId, String opType) {
        log.info("/rest/attentionKpi接口输入参数：{userId = "+userId+",mataKpiId = "+mataKpiId+",opType = "+opType);
        try {
            boolean AttentionKpiLists = collectionService.attentionKpi(userId, mataKpiId, opType);
            if (AttentionKpiLists) {
                if (("ADD").equals(opType)) {
                    return new RestResult<>("success", "收藏成功");
                } else {
                    return new RestResult<>("success", "取消收藏成功");
                }
            } else {
                if (("DELETE").equals(opType)) {
                    return new RestResult<>("success", "收藏失败，请重新提交");
                } else {
                    return new RestResult<>("success", "取消收藏失败，请重新提交");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RestResult<>("fault", "查询出错，请重新查询");
    }
}
