package com.inspur.bjzx.scenesecurityserve.service;

import java.util.List;
import java.util.Map;

public interface WarRoomService {
    List<Map<String, Object>> getWarRoomSceneAllInfo(String userAccount);
    boolean changeHadLook(String userAccount,Integer noticeId);
}
