package com.inspur.bjzx.scenesecurityserve.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by 刘洋 on 2016/11/2 0002.
 */
public class FileUpload {
    private final static Logger logger = LoggerFactory.getLogger(FileUpload.class);

    public static boolean UploadFile(String filePath, byte [] bytes, String fileName){
        boolean flag = false;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String newFileName = filePath + File.separator + fileName;
            OutputStream out = new FileOutputStream(newFileName);
            out.write(bytes);
            out.flush();
            out.close();
            //FileCopyUtils.copy(bytes, new File(newFileName));
            flag = true;
        } catch (IOException e){
            logger.info(e.getMessage());
        }
        return flag;
    }
}
