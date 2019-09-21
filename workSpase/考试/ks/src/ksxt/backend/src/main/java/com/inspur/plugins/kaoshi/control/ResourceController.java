package com.inspur.plugins.kaoshi.control;

import com.alibaba.fastjson.JSONObject;
import com.inspur.plugins.kaoshi.service.ResourceService;
import com.inspur.plugins.kaoshi.util.AESUtils;
import com.inspur.plugins.kaoshi.util.DataUtil;
import com.inspur.plugins.kaoshi.util.RestResult;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class ResourceController {
    private static Logger log = LogManager.getLogger(ResourceController.class);
    @Resource
    ResourceService resourceService;

    private static final String TRUE = "true";
    private static final String FALSE = "false";

    @GetMapping(value = "/resource/resourceList")
    public RestResult resourceList(String userId, String pageStart, String pageSize) {
        try {
            Map<String, Object> resourceCount = resourceService.resourceCount();
            String count = String.valueOf(resourceCount.get("count"));
            List<Map<String, Object>> resourceList = resourceService.getResourceList(AESUtils.decoder(userId), pageStart, pageSize);
            return new RestResult<>(TRUE, "查询成功", count, resourceList);
        } catch (Exception e) {
            log.info("/resource/resourceList error\n" + e.getMessage());
            return new RestResult(FALSE, "查询失败");
        } finally {
            log.info("\n\n");
        }
    }

    @GetMapping(value = "/resource/resourceSearch")
    public RestResult resourceSearch(String resourceId, String userId) {
        try {
            Map<String, Object> resourceLogCount = resourceService.resourceLogCount(resourceId, AESUtils.decoder(userId));
            if (DataUtil.O2I(resourceLogCount.get("count")) > 0) {
                Map<String, Object> resourceSearch = resourceService.getResourceSearch(resourceId, AESUtils.decoder(userId));
                return new RestResult<>(TRUE, "查询成功", resourceSearch);
            } else {
                Map<String, Object> resourceName = resourceService.getResourceName(resourceId);
                resourceName.put("is_pass", "0");
                return new RestResult<>(TRUE, "查询成功", resourceName);
            }
        } catch (Exception e) {
            log.info("/resource/resourceList error\n" + e.getMessage());
            return new RestResult(FALSE, "查询失败");
        } finally {
            log.info("\n\n");
        }
    }

    @PostMapping(value = "/resource/recordCurrentTime")
    public RestResult recordCurrentTime(@RequestBody JSONObject body) {
        try {
            String resourceId = body.getString("resourceId");
            String userId = AESUtils.decoder(body.getString("userId"));
            String startTime = body.getString("startTime");
            String endTime = body.getString("endTime");
            String isPass = body.getString("is_pass");
            if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
                return new RestResult<>(TRUE, "没有学习，请先学习");
            }
            int resource = resourceService.recordCurrentTime(resourceId, userId, startTime, endTime, isPass);
            if (resource == 1) {
                return new RestResult<>(TRUE, "提交成功");
            } else {
                return new RestResult(FALSE, "提交失败");
            }
        } catch (Exception e) {
            log.info("/resource/resourceComplete error\n" + e.getMessage());
            return new RestResult(FALSE, "提交失败");
        } finally {
            log.info("\n\n");
        }
    }
/*    @GetMapping(value = "/resource/resourceLook/{resourceId}")
    public RestResult resourceLook(@PathVariable("resourceId") String resourceId ){
        try{
            Map<String, Object> resourceLook = resourceService.resourceLook(resourceId);
            log.info(resourceLook);
            return new RestResult<>(TRUE,"查询成功",resourceLook);
        }catch (Exception e){
            log.info("/resource/resourceLook error\n"+e.getMessage());
            return new RestResult(FALSE, "查询失败");
        }finally {
            log.info("\n\n");
        }
    }*/

}
