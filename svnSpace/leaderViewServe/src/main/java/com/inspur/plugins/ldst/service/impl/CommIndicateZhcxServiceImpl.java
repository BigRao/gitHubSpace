package com.inspur.plugins.ldst.service.impl;

import com.inspur.plugins.ldst.dao.CommIndicateZhcxDao;
import com.inspur.plugins.ldst.dao.IndicateDayDao;
import com.inspur.plugins.ldst.dao.IndicateWeekDao;
import com.inspur.plugins.ldst.model.*;
import com.inspur.plugins.ldst.service.CommIndicateZhcxService;
import com.inspur.plugins.ldst.utils.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CommIndicateZhcxServiceImpl implements CommIndicateZhcxService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Resource
    private CommIndicateZhcxDao commIndicateZhcxDao;

    @Resource
    private IndicateDayDao indicateDayDao;

    @Resource
    private IndicateWeekDao indicateWeekDao;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//设置时间格式1
    private final DecimalFormat df2 = new DecimalFormat("0.00%");//设置double格式1
    private final DecimalFormat df0 = new DecimalFormat("0%");//设置double格式2
    private final Calendar cal = Calendar.getInstance();
    private static final Map<String,Double> jzz;//指标基准值
    

    private static final String QW = "合计";
    private static final String FGS = "FGS";
    private static final String YD_GD_NUM = "YD_GD_NUM";
    private static final String JK_GD_NUM = "JK_GD_NUM";
    private static final String OUTLINE_OLT_NUM = "OUTLINE_OLT_NUM";
    private static final String OLT_NUM = "OLT_NUM";
    private static final String OUTLINE_OLT_RATE = "OUTLINE_OLT_RATE";
    private static final String[] ONE = {"城一分公司","城二分公司","城三分公司"};
    private static final String[] TWO = {"大兴分公司","昌平分公司","通州分公司","顺义分公司","房山分公司"};
    private static final String[] THREE = {"密云分公司","怀柔分公司","平谷分公司","延庆分公司"};
    private static final String CENTIMETER = "‰";
    private final TimeDate timeDate = new TimeDate();
    static {
        jzz = new HashMap<>();
        jzz.put("WITHDRAWAL_RATE_2G",4.0);
        jzz.put("WITHDRAWAL_RATE_4G",4.0);
        jzz.put("DAY_AVERAGE",0.15);
        jzz.put("COMPLETE_TIMELY_RATE",95.0);
        jzz.put("FORM_COMPLIANCE_RATE",98.0);
        jzz.put("ONE_PASSING_RATE",95.0);
        jzz.put("UTILIZATION_RATE",90.0);
        jzz.put("ATTENDANCE_RATE",90.0);
        jzz.put("UPPER_STATION_RATE",30.0);
        jzz.put("FRONTLINE_RATE",15.0);
        jzz.put("FAULT_ROOM_RATE",0.01);
        jzz.put("WIRELESS_RATE",0.96);
    }


    @Override
    public List<String> kpiValue(CommIndicateZhcx commIndicateZhcx,String time,String city) throws ParseException {
        String timeInterval = String.valueOf(commIndicateZhcx.getTimeInterval());
        String tableName = commIndicateZhcx.getTableName();
        String columnName = commIndicateZhcx.getColumnName();
        String endTime = endTime(time);
        String startTime = startTime(time,timeInterval);

        return getkpiValue(city,tableName,columnName,startTime,endTime);
    }

    @Override
    public List<CommIndicateZhcx> getCommIndicateZhcxes(String kpiId) {
        return commIndicateZhcxDao.getById(kpiId);
    }

    @Override
    public List<CompanyComplanintDataVO> companyComplaint(String kpiId, String time){
        List <CompanyComplanintDataVO> companyComplanintDataList = new ArrayList<>();
            String names = getColumnName(kpiId);
            List<Map<String, String>> gDList = indicateDayDao.getByCloumnName(names,time);

            for (Map<String, String> tsgd:gDList) {//投诉工单
                CompanyComplanintDataVO companyComplanintData = new CompanyComplanintDataVO();
                try{
                    String company = tsgd.get(FGS);
                    String category = getCategory(company);

                    companyComplanintData.setCompany(company);
                    companyComplanintData.setCategory(category);
                    companyComplanintData.setPlmnComplaint(tsgd.get(YD_GD_NUM));
                    companyComplanintData.setHwnNum(tsgd.get(JK_GD_NUM));

                    companyComplanintDataList.add(companyComplanintData);
                }catch (UnknownCompanyCategoryException e){
                    logger.error("{}:{}",e.getMessage(),e.getValue());
                }
            }

        return companyComplanintDataList;
    }

    @Override
    public List<CompanyOltDataVO> companyOlt(String kpiId, String time){

        List<CompanyOltDataVO> companyOltDataList=new ArrayList<>();

            String names = getColumnName(kpiId);

            List<Map<String, String>> oltDataList = indicateDayDao.getByCloumnName(names,time);

            for (Map<String, String> oltData:oltDataList) {
                CompanyOltDataVO companyOltData = new CompanyOltDataVO();
                String company = oltData.get(FGS);
                try{
                    String category = getCategory(company);
                    companyOltData.setCompany(company);
                    companyOltData.setCategory(category);
                    companyOltData.setOltBack(oltData.get(OUTLINE_OLT_NUM));
                    companyOltData.setOltNum(oltData.get(OLT_NUM));
                    companyOltData.setOltBackProportion(oltData.get(OUTLINE_OLT_RATE));
                    companyOltDataList.add(companyOltData);
                }catch (UnknownCompanyCategoryException e){
                    logger.error("{}:{}",e.getMessage(),e.getValue());
                }
            }

        return companyOltDataList;
    }

    @Override
    public List<CompanySortKpiVO> companyDayWeek(String name, String time)  throws ParseException{
        List<CompanySortKpiVO> day = day(name,time);
        List<CompanySortKpiVO> week = week(name,time);
        day.addAll(week);
        return day;
    }

    @Override
    public CompanyTimeSearchKpiInfoVO getManyTimeKpiInfo(CommIndicateZhcx commIndicateZhcx){
        CompanyTimeSearchKpiInfoVO companyTimeSearchKpiInfoVO = new CompanyTimeSearchKpiInfoVO();

        companyTimeSearchKpiInfoVO.setKpiId(String.valueOf(commIndicateZhcx.getIndicId()));
        companyTimeSearchKpiInfoVO.setKpiName(commIndicateZhcx.getIndicName());
        companyTimeSearchKpiInfoVO.setUnit(commIndicateZhcx.getIndicUnit());
        companyTimeSearchKpiInfoVO.setTendency("");
        return companyTimeSearchKpiInfoVO;
    }
    @Override
    public List<String> timeList(CommIndicateZhcx commIndicateZhcx, String time) throws ParseException {
        String timeInterval = String.valueOf(commIndicateZhcx.getTimeInterval());

        String endTime = endTime(time);
        String startTime = startTime(time,timeInterval);
        return timeDate.getBetweenDate(startTime,endTime);
    }

    private List<CompanySortKpiVO> day(String name,String time)  {
        Map<String, String> qwDay = indicateDayDao.getByName(QW,time);
        Map<String, String> dayTable = indicateDayDao.getByName(name,time);
        List<CompanySortKpiVO> companySortKpiVOList = new ArrayList<>();
        if (dayTable==null)return companySortKpiVOList;
        for(Map.Entry<String,String> dayData:dayTable.entrySet()){//row 为一个表中参数列表,英文 WITHDRAWAL_RATE_2G WITHDRAWAL_RATE_4G

            String key = dayData.getKey().toUpperCase();
            CommIndicateZhcx commIndicateZhcx = commIndicateZhcxDao.getOne(key);//查询id INDIC_ID, 类名 INDIC_NAME,别名 INDIC_NAME_ALIAS

            String kpiId = String.valueOf(commIndicateZhcx.getIndicId());//id
            String kpiCategory = commIndicateZhcx.getIndicNameAlias();//分类名称
            String kpiName = commIndicateZhcx.getIndicName();//别名

            String dayNetValue = TrimZero.format(qwDay.get(dayData.getKey()));//当前指标全网数值
            String currentPercentage = TrimZero.format(dayData.getValue());//当前指标数值
            dayNetValue = deleteSymbols(dayNetValue);
            Double value = 0.0;
            if(!currentPercentage.contains(CENTIMETER)){
                value = Double.parseDouble(dayData.getValue());//当前指标数值
            }
            currentPercentage = deleteSymbols(currentPercentage);
            Double standard = jzz.get(key);//当前指标基准值
            Double value24G = Double.parseDouble(currentPercentage);//2G,4G当前指标数值
            CompanySortKpiVO companySortKpiVO = new CompanySortKpiVO();
            String symbols;
            if("WITHDRAWAL_RATE_2G,WITHDRAWAL_RATE_4G".contains(key)){
                if(value24G>=standard){
                    symbols="<="+standard+CENTIMETER;
                    kpisPut(companySortKpiVO, kpiId, kpiCategory, kpiName, qwDay.get(dayData.getKey()) , dayData.getValue(), symbols);
                    companySortKpiVOList.add(companySortKpiVO);
                }
            }else{
                if(value<=standard){
                    symbols=">="+WildCard.withWildCard(standard);
                    kpisPut(companySortKpiVO, kpiId, kpiCategory, kpiName, WildCard.withWildCard(dayNetValue), WildCard.withWildCard(currentPercentage), symbols);
                    companySortKpiVOList.add(companySortKpiVO);
                }
            }
        }
        return companySortKpiVOList;
    }

    private String deleteSymbols(String str ){
        if (str.contains(CENTIMETER)){
            return str.substring(0,str.indexOf(CENTIMETER));
        }else {
            return str;
        }
    }


    private List<CompanySortKpiVO> week(String name,String time) throws ParseException {
        String monday = timeDate.monday(time);//所在周星期一的日期
        String sunday = timeDate.sunday(time);//所在周星期日的日期
        List<CompanySortKpiVO> companySortKpiVOList = new ArrayList<>();
        Map<String, String> qwWeek = indicateWeekDao.getByName(QW,monday,sunday);
        Map<String, String> weekTable = indicateWeekDao.getByName(name,monday,sunday);
        if (weekTable==null)return companySortKpiVOList;
        for(Map.Entry<String,String> weekData:weekTable.entrySet()){
            String key = weekData.getKey().toUpperCase();
            CommIndicateZhcx commIndicateZhcx = commIndicateZhcxDao.getOne(key);//查询id INDIC_ID, 类名 INDIC_NAME,别名 INDIC_NAME_ALIAS

            String kpiId = String.valueOf(commIndicateZhcx.getIndicId());//id
            String kpiCategory = commIndicateZhcx.getIndicName();//分类名称
            String kpiName = commIndicateZhcx.getIndicNameAlias();//别名

            String qwPercentage = df2.format(Double.parseDouble(qwWeek.get(key.toLowerCase())));//当前指标全网数值百分制

            String currentPercentage = df2.format(Double.parseDouble(weekData.getValue()));//当前指标数值百分制
            String standardPercentage = df0.format(jzz.get(key));//当前指标基准值百分制

            Double value = Double.parseDouble(weekData.getValue());//当前指标数值
            Double standard = jzz.get(key);//当前指标基准值
            CompanySortKpiVO companySortKpiVO = new CompanySortKpiVO();
            String symbols;
            if("FAULT_ROOM_RATE".contains(key)){
                if(value>standard){
                    symbols="<"+standardPercentage;
                    kpisPut(companySortKpiVO,kpiId, kpiCategory, kpiName, qwPercentage, currentPercentage, symbols);
                    companySortKpiVOList.add(companySortKpiVO);
                }
            }else{
                if(value<=standard){
                    symbols=">="+standardPercentage;
                    kpisPut(companySortKpiVO,kpiId, kpiCategory, kpiName, qwPercentage, currentPercentage, symbols);
                    companySortKpiVOList.add(companySortKpiVO);
                }
            }
        }
        return companySortKpiVOList;
    }

    private String endTime(String time){
        String[] times = time.split("/");
        return times[0];
    }
    @Override
    public String startTime(String time,String timeInterval) throws ParseException {
        String[] times = time.split("/");
        Date t;
        Integer day = Integer.valueOf(times[1]);
        t = sdf.parse(times[0]);
        cal.setTime(t);
        //10080 周/hour 小时/1440 天/ months 月
        if ("1440".equals(timeInterval)){
            cal.add(Calendar.DATE,-day);
        }
        if ("10080".equals(timeInterval)){
            cal.add(Calendar.WEEK_OF_MONTH,-day);
        }

        return sdf.format(cal.getTime());
    }

    private List<String> getkpiValue(String city,String tableName,String columnName,String startTime,String endTime) throws ParseException {

        List<Map<String, String>> rows2 ;
        if("pm_gfs_indicate_day".equals(tableName)){
            rows2 = indicateDayDao.getByTime(columnName+" "+columnName.toUpperCase(),city,startTime,endTime);
        }else {
            rows2 = indicateWeekDao.getByTime(columnName+" "+columnName.toUpperCase(),city,startTime,endTime);
        }
        List<String> timeList = timeDate.getBetweenDate(startTime,endTime);

        List<String> kpiValue = Arrays.asList(new String[timeList.size()]);
        Collections.fill(kpiValue, "0");//填充集合
        for (Map<String, String> map2:rows2) {
            String time1 = sdf.format(map2.get("TIME"));
            for (String timeTime:timeList) {
                setKpiValue(columnName, timeList, kpiValue, map2, time1, timeTime);
            }
        }
        return kpiValue;
    }

    private void setKpiValue(String columnName, List<String> timeList, List<String> kpiValue, Map<String, String> map2, String time1, String timeTime) {
        int index = timeList.indexOf(timeTime);
        if (time1.equals(timeTime)){

            String a = deleteSymbols(!StringUtils.isNotBlank(map2.get(columnName.toUpperCase()))?"0":map2.get(columnName.toUpperCase()));
            String value = TrimZero.format(a);
            kpiValue.set(index, value);
        }
    }

    private String getColumnName(String kpiId){
        List<Map<String, String>> rows = commIndicateZhcxDao.getListById(kpiId);
        List<String> list = new ArrayList<>();
        String columnName;
        for (Map<String, String> map:rows) {
            columnName = map.get("COLUMN_NAME");
            list.add(columnName+" "+columnName.toUpperCase());

        }

        return String.join(",",list);
    }

    private String getCategory(String company) throws UnknownCompanyCategoryException {

        if (QW.equals(company)) {
            return QW;
        }else if (Arrays.asList(ONE).contains(company)) {
            return "一类";
        }else if (Arrays.asList(TWO).contains(company)) {
            return "二类";
        }else if (Arrays.asList(THREE).contains(company)) {
            return "三类";
        }
        throw new UnknownCompanyCategoryException("未知的公司分类",company);
    }

    private void kpisPut(CompanySortKpiVO companySortKpiVO, String kpiId, String kpiCategory, String kpiName, String qwPercentage, String currentPercentage, String symbols) {
        companySortKpiVO.setId(kpiId);
        companySortKpiVO.setName(kpiName);
        companySortKpiVO.setValue(currentPercentage);

        companySortKpiVO.setKpiCategory(kpiCategory);
        companySortKpiVO.setStandard(symbols);
        companySortKpiVO.setNetValue(qwPercentage);
    }
    @Override
    public Map<String, Object> getOversee(String name){
        String sql = "select ZH_LABEL \"oversee\",PHONE \"phone\" from PM_GFS_PERSON where MAINTAIN_AREA =?";
        return jdbcTemplate.queryForMap(sql, name);
    }

}