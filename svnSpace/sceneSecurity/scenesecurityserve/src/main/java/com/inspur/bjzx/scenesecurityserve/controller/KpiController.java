package com.inspur.bjzx.scenesecurityserve.controller;

import com.inspur.bjzx.scenesecurityserve.service.KpiService;
import com.inspur.bjzx.scenesecurityserve.util.DataUtil;
import com.inspur.bjzx.scenesecurityserve.util.RestResult;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class KpiController {
    private static Logger log = org.apache.log4j.Logger.getLogger(KpiController.class);

    @Autowired
    KpiService kpiService;

    @GetMapping(value = "/kpi/searchKpi")
    public RestResult searchKpi(String searchContent, String time) {
        try {
            List<Map<String, Object>> searchKpi = kpiService.searchKpi(searchContent, time);

            return new RestResult<>("true", "查询成功", searchKpi);
        } catch (Exception e) {
            return new RestResult<>("false", "查询失败");
        }

    }

    @GetMapping(value = "/kpi/searchKeyAreas")
    public RestResult searchKeyAreas(String userNameApp) {
        try {
            Map<String, Object> areaIdMap = kpiService.getUserArea(userNameApp);

            List<Map<String, Object>> areaTwoList = kpiService.getAreaTwo(areaIdMap);
            List<Map<String, Object>> DataList = new ArrayList<>();
            for (Map<String, Object> areaTwo : areaTwoList) {

                Map<String, Object> Data = new HashMap<>();
                String areaTwoName = DataUtil.O2S(areaTwo.get("area_two"));
                Data.put("area_two", areaTwoName);
                List<Map<String, Object>> areaThree = kpiService.getAreaThree(areaTwoName, areaIdMap);
                Data.put("areas", areaThree);
                DataList.add(Data);
            }
            return new RestResult<>("true", "查询成功", DataList);
        } catch (Exception e) {
            return new RestResult<>("false", "查询失败");
        }
    }

    @GetMapping(value = "/kpi/searchKeyAreaBrokenStation")
    public RestResult searchKeyAreaBrokenStation(String area_id, String time) {
        try {

            List<Map<String, Object>> brokenStation = kpiService.getBrokenStation(area_id);
            Iterator<Map<String, Object>> iter = brokenStation.iterator();

            while (iter.hasNext()) {
                Map<String, Object> map = iter.next();

                Integer brokenStationTwo = DataUtil.O2I(map.get("broken_station_two"));
                Integer brokenStationFour = DataUtil.O2I(map.get("broken_station_four"));
                Integer brokenStationFive = DataUtil.O2I(map.get("broken_station_five"));
                Integer plus_4g = DataUtil.O2I(map.get("4G_PLUS"));
                String abnormalCell = DataUtil.O2S(map.get("abnormal_cell"));
                String abnormalComputerRoom = DataUtil.O2S(map.get("abnormal_computer_room"));
                brokenStationFour = brokenStationFour+plus_4g;
                map.put("broken_station_four",brokenStationFour);
                if (brokenStationTwo == 0 && brokenStationFour == 0 && brokenStationFive == 0 && StringUtils.isBlank(abnormalCell) && StringUtils.isBlank(abnormalComputerRoom)) {
                    iter.remove();
                }
            }

            return new RestResult<>("true", "查询成功", brokenStation);
        } catch (Exception e) {
            return new RestResult<>("false", "查询失败");
        }
    }

}
