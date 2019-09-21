package com.inspur.plugins.ldst.service.impl;

import com.inspur.plugins.ldst.dao.CommIndicateZhcxDao;
import com.inspur.plugins.ldst.dao.IndicateDayDao;
import com.inspur.plugins.ldst.model.CommIndicateZhcx;
import com.inspur.plugins.ldst.service.IndicateDayService;
import com.inspur.plugins.ldst.utils.DataUtil;
import com.inspur.plugins.ldst.utils.TrimZero;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class IndicateDayServiceImpl implements IndicateDayService {

    @Resource
    private IndicateDayDao indicateDayDao;
    @Resource
    private CommIndicateZhcxDao commIndicateZhcxDao;

    private static final String OUTLINE_2G_NUM = "OUTLINE_2G_NUM";
    private static final String BTS_NUM = "BTS_NUM";
    private static final String OUTLINE_4G_NUM = "OUTLINE_4G_NUM";
    private static final String LTE_NUM = "LTE_NUM";
    private static final String OUTLINE_OLT_NUM = "OUTLINE_OLT_NUM";
    private static final String OLT_NUM = "OLT_NUM";
    private static final String OUTLINE_OLT_RATE = "OUTLINE_OLT_RATE";
    private static final String OUTLINE_5G_NUM = "OUTLINE_5G_NUM";
    private static final String FIVE_NUM = "FIVE_NUM";
    private static final String KPI_ID = "kpiId";
    private static final String KPI_NAME = "kpiName";
    private static final String KPI_VALUE = "kpiValue";
    private static final String OUT_2G = "2G退服";
    private static final String OUT_4G = "4G退服";
    private static final String OUT_OLT = "OLT退服";
    private static final String GD_NUM = "投诉工单";
    private static final String OUT_5G = "5G退服";


    //家客投诉工单，移动投诉工单
    private static final String JK_GD_NUM = "JK_GD_NUM";
    private static final String YD_GD_NUM = "YD_GD_NUM";


    private static final String RESULT = "result";
    private static final String MESSAGE = "message";

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    public JSONObject retireNum(String today, String yesterday) {

        JSONObject result = new JSONObject();
        result.put(RESULT, "false");
        result.put(MESSAGE, "查询数据失败");

        List<String> queryList = new ArrayList<>();
        queryList.add(OUT_2G);
        queryList.add(OUT_4G);
        queryList.add(OUT_OLT);
        queryList.add(GD_NUM);
        queryList.add(OUT_5G);

        JSONArray data = new JSONArray();

        Map<String, String> indicId = new HashMap<>();
        List<CommIndicateZhcx> objList = commIndicateZhcxDao.getByList(queryList);
        for (CommIndicateZhcx cod : objList) {
            indicId.put(cod.getColumnName(), cod.getIndicId().toString());
        }
        putAll(data, indicId, today, yesterday);
        result.put("data", data);
        result.put(RESULT, "true");
        result.put(MESSAGE, "查询数据成功");
        return result;
    }

    private void putAll(JSONArray data, Map<String, String> indicId, String today, String yesterday) {
        Map<String, String> yesterdayNum = retireNumByYesterday(yesterday);
        Map<String, String> todayNum = retireNumByToday(today);
        Integer outLine2gNum = 0;
        Integer btsNum = 0;
        Integer outLine4gNum = 0;
        Integer lteNum = 0;
        Integer outLineOltNum = 0;
        Integer oltNum = 0;
        Double outLineOltRate = 0.0;
        Integer jkGdNum = 0;
        Integer yDGdNum = 0;
        Integer outLine5gNum = 0;
        Integer fiveNum = 0;
        if (yesterdayNum.size() > 0) {

            outLineOltNum = DataUtil.getInteger(yesterdayNum, OUTLINE_OLT_NUM);
            oltNum = DataUtil.getInteger(yesterdayNum, OLT_NUM);
            outLineOltRate = DataUtil.getDouble(yesterdayNum, OUTLINE_OLT_RATE);
            jkGdNum = DataUtil.getInteger(yesterdayNum, JK_GD_NUM);
            yDGdNum = DataUtil.getInteger(yesterdayNum, YD_GD_NUM);

        }
        if (todayNum.size() > 0) {
            outLine2gNum = DataUtil.getInteger(todayNum, OUTLINE_2G_NUM);
            btsNum = DataUtil.getInteger(todayNum, BTS_NUM);
            outLine4gNum = DataUtil.getInteger(todayNum, OUTLINE_4G_NUM);
            lteNum = DataUtil.getInteger(todayNum, LTE_NUM);
            outLine5gNum = DataUtil.getInteger(todayNum, OUTLINE_5G_NUM);
            fiveNum = DataUtil.getInteger(todayNum, FIVE_NUM);
        }
        JSONObject dataSon2G = getDataChildrenJson(OUT_2G, outLine2gNum + "", btsNum + "", indicId.get("outline_2G_num"), indicId.get("bts_num"), "", "");
        JSONObject dataSon4G = getDataChildrenJson(OUT_4G, outLine4gNum + "", lteNum + "", indicId.get("outline_4G_num"), indicId.get("lte_num"), "", "");
        JSONObject dataSonOlt = getDataChildrenJson(OUT_OLT, outLineOltNum + "", oltNum + "", indicId.get("outline_olt_num"), indicId.get("olt_num"), outLineOltRate + "", indicId.get("outline_olt_rate"));
        JSONObject dataSonGD = getDataChildrenJson(GD_NUM, jkGdNum + "", yDGdNum + "", indicId.get("jk_gd_num"), indicId.get("yd_gd_num"), "", "");
        JSONObject dataSon5G = getDataChildrenJson(OUT_5G, outLine5gNum + "", fiveNum + "", indicId.get("outline_5G_num"), indicId.get("five_num"), "", "");
        data.add(dataSon2G);
        data.add(dataSon4G);
        data.add(dataSonOlt);
        data.add(dataSonGD);
        data.add(dataSon5G);
    }

    @Override
    public String getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        return sdf.format(calendar.getTime());
    }

    private Map<String, String> retireNumByYesterday(String yesterday) {
        Map<String, String> values = new HashMap<>();
        Map<String, String> num = indicateDayDao.getByName2("合计", yesterday);
        if (num != null) {
            putValues(values, num,OUTLINE_OLT_NUM);
            putValues(values, num,OLT_NUM);
            values.put(OUTLINE_OLT_RATE, TrimZero.format(DataUtil.getString(num, OUTLINE_OLT_RATE)));
            putValues(values, num,JK_GD_NUM);
            putValues(values, num,YD_GD_NUM);
        }
        return values;
    }

    private void putValues(Map<String, String> values, Map<String, String> num, String str) {
        values.put(str, DataUtil.getString(num, str));
    }

    private Map<String, String> retireNumByToday(String today) {
        Map<String, String> values = new HashMap<>();
        Map<String, String> num = indicateDayDao.getByToday("合计", today);
        if (num != null) {
            putValues(values, num,OUTLINE_2G_NUM);
            putValues(values, num,BTS_NUM);
            putValues(values, num,OUTLINE_4G_NUM);
            putValues(values, num,LTE_NUM);
            putValues(values, num,OUTLINE_5G_NUM);
            putValues(values, num,FIVE_NUM);
        }
        return values;
    }

    private JSONObject getDataChildrenJson(String viewName, String kpiValue1, String kpiValue2, String kpiId1, String kpiId2, String oltRate, String kpiId3) {
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(2);
        JSONObject dataSon = new JSONObject();
        JSONArray viewData = new JSONArray();

        JSONObject jsonObjectOLT1 = new JSONObject();
        jsonObjectOLT1.put(KPI_ID, kpiId1);
        jsonObjectOLT1.put(KPI_NAME, "退服数");
        jsonObjectOLT1.put(KPI_VALUE, kpiValue1);
        JSONObject jsonObjectOLT2 = new JSONObject();
        jsonObjectOLT2.put(KPI_ID, kpiId2);
        jsonObjectOLT2.put(KPI_NAME, "基站数");
        jsonObjectOLT2.put(KPI_VALUE, kpiValue2);
        if (OUT_OLT.equals(viewName)) {
            jsonObjectOLT2.put(KPI_NAME, "OLT总数");
        }
        if ("投诉工单".equals(viewName)) {
            jsonObjectOLT1.put(KPI_NAME, "家客");
            jsonObjectOLT2.put(KPI_NAME, "移动");
        }

        viewData.add(jsonObjectOLT1);
        viewData.add(jsonObjectOLT2);
        if (!"投诉工单".equals(viewName) && !OUT_OLT.equals(viewName)) {
            JSONObject jsonObjectOLT3 = new JSONObject();
            jsonObjectOLT3.put(KPI_ID, "0");
            jsonObjectOLT3.put(KPI_NAME, "占比");
            jsonObjectOLT3.put(KPI_VALUE, format.format(!"0".equals(kpiValue2) ? Double.parseDouble(kpiValue1) / Double.parseDouble(kpiValue2) : 0.0));
            viewData.add(jsonObjectOLT3);
        }
        if (!"".equals(oltRate)) {
            JSONObject jsonObjectOLTrate = new JSONObject();
            jsonObjectOLTrate.put(KPI_ID, kpiId3);
            jsonObjectOLTrate.put(KPI_NAME, "占比");
            jsonObjectOLTrate.put(KPI_VALUE, format.format(Double.parseDouble(oltRate)));
            viewData.add(jsonObjectOLTrate);
        }
        dataSon.put("viewName", viewName);
        dataSon.put("viewData", viewData);

        return dataSon;
    }

}
