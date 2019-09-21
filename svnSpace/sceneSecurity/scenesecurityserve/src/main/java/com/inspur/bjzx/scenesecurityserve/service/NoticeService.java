package com.inspur.bjzx.scenesecurityserve.service;

import java.util.List;
import java.util.Map;


public interface NoticeService {
     List<Map<String,Object>> getNotice();
     int addNotice(String userAccount,String title,String noticeText);
}
