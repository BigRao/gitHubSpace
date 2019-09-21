package com.inspur.bjzx.scenesecurityserve.service;

import net.sf.json.JSONObject;


public interface UploadFileService {
    boolean uploadFile(JSONObject jsonObject, String filePath, String fileType);
}
