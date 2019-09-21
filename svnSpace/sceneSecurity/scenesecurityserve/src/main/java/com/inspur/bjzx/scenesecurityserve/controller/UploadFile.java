package com.inspur.bjzx.scenesecurityserve.controller;

import com.inspur.bjzx.scenesecurityserve.service.UploadFileService;
import com.inspur.bjzx.scenesecurityserve.util.RestResult;
import com.inspur.bjzx.scenesecurityserve.util.FileUpload;
import com.inspur.bjzx.scenesecurityserve.util.TimeTransformer;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Base64.Decoder;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;


@Controller
public class UploadFile {
    @Value("${image_path}")
    String image_path;

    @Autowired
    UploadFileService uploadFileService;


    private static final Logger log = Logger.getLogger(UploadFile.class);

    //附件上传接口
    @RequestMapping(value = "/user/upload",method = RequestMethod.POST)
    @ResponseBody
    private RestResult UploadFile(@RequestBody JSONObject jsonObject){
        log.info("文件开始上传！");
        String filePath;
        String newFileName;
        boolean upLoadFlag;
        boolean T;
        String useraccount = (String)jsonObject.get("useraccount");
        String longitude = jsonObject.get("longitude").toString();
        String latitude = jsonObject.get("latitude").toString();

        /*String longitude = "116.598432";
        String latitude = "39.686757";
        String useraccount = "support";*/
        String imgtype = (String)jsonObject.get("imgType");
        String imgfile = (String)jsonObject.get("imgfile");

        log.info("/user/upload\nPARAMS：\n" +
                "useraccount："+useraccount+"\n" +
                "longitude："+longitude+"\n" +
                "latitude："+latitude+"\n" +
                "imgtype："+imgtype+"\n");
        if(StringUtils.isEmpty(imgfile)){
            log.info("done---false");
            return new RestResult<>("false", "请至少选择一张照片");
        }
        String img = imgfile.substring(imgfile.indexOf(",")+1,imgfile.length());
        if (StringUtils.isNotBlank(longitude)&&StringUtils.isNotBlank(latitude)&&StringUtils.isNotBlank(useraccount)&&StringUtils.isNotBlank(imgtype)){
            filePath = image_path;
            filePath += File.separator + useraccount + File.separator + TimeTransformer.getFormate1(new Date());
            newFileName = TimeTransformer.formate1(new Date())+"."+imgtype;
            Decoder decoder = Base64.getDecoder();
            try {
                //Base64解码
                byte[] b = decoder.decode(img);
                for (int i = 0; i < b.length; ++i) {
                    if (b[i] < 0) {//调整异常数据
                        b[i] += 256;
                    }
                }
                upLoadFlag = FileUpload.UploadFile(filePath,b,newFileName);
                if (upLoadFlag){
                    T = uploadFileService.uploadFile(jsonObject,filePath+File.separator+newFileName,"picture");
                    if (T){
                        /*HttpPost httpPost = new HttpPost("http://10.224.133.53:8899/pmdata/SET_TOUCH_SCREEN_CMD/");
                        CloseableHttpClient client = HttpClients.createDefault();
                        String respContent = "";

                        String json = "{\"CMD\":\"{\\\"data\\\":{\\\"event\\\":\\\"ImgUpdataEvent\\\",\\\"target\\\":\\\" \\\",\\\"value\\\":\\\" \\\",\\\"name\\\":\\\" \\\",\\\"ip\\\":\\\"10.4.85.143\\\"}}\"}";

                        StringEntity entity = new StringEntity(json,"utf-8");//解决中文乱码问题
                        entity.setContentEncoding("UTF-8");
                        entity.setContentType("application/json");
                        httpPost.setEntity(entity);
                        System.out.println();

                        HttpResponse resp = client.execute(httpPost);
                        if(resp.getStatusLine().getStatusCode() == 200) {
                            HttpEntity he = resp.getEntity();
                            respContent = EntityUtils.toString(he,"UTF-8");
                        }*/

                        log.info("done---true");
                        return new RestResult<>("true", "照片上传成功");
                    }else{
                        log.info("done---false");
                        return new RestResult<>("false", "网络出现问题，请重新提交");
                    }
                }else{
                    log.info("done---false");
                    return new RestResult<>("false", "网络出现问题，请重新提交");
                }
            } catch (Exception e) {
                log.info("done---false");
                return new RestResult<>("false", "网络出现问题，请重新提交");
            }
        }else{
            log.info("done---false");
            return new RestResult<>("false", "参数不完整，请重新提交");
        }
    }

    //附件上传接口
    @RequestMapping(value = "/user/uploadVideo",method = RequestMethod.POST)
    @ResponseBody
    private RestResult UploadVideo(HttpServletRequest httpServletRequest,@RequestParam("file") MultipartFile file){
        log.info("文件开始上传！");
        String filePath;
        String newFileName;
        boolean upLoadFlag;
        boolean T;
        String imgType = httpServletRequest.getParameter("imgType");
        String useraccount = httpServletRequest.getParameter("useraccount");
        String longitude = httpServletRequest.getParameter("longitude");
        String latitude = httpServletRequest.getParameter("latitude");
        if (StringUtils.isNotBlank(imgType)&&StringUtils.isNotBlank(useraccount)&&StringUtils.isNotBlank(longitude)&&StringUtils.isNotBlank(latitude)){
            if (file.isEmpty()){
                log.info("done---false");
                return new RestResult<>("false", "请至少选择一个视频");
            }else{
                filePath = image_path;
                filePath += File.separator + useraccount + File.separator + TimeTransformer.getFormate1(new Date());
                newFileName = TimeTransformer.formate1(new Date())+"."+imgType;
                byte [] bytes = new byte[0];
                try {
                    bytes = file.getBytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                upLoadFlag = FileUpload.UploadFile(filePath,bytes,newFileName);
                if (upLoadFlag){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("longitude",longitude);
                    jsonObject.put("latitude",latitude);
                    jsonObject.put("useraccount",useraccount);
                    T = uploadFileService.uploadFile(jsonObject,filePath+File.separator+newFileName,"video");
                    if (T){
                        /*HttpPost httpPost = new HttpPost("http://10.224.133.53:8899/pmdata/SET_TOUCH_SCREEN_CMD/");
                        CloseableHttpClient client = HttpClients.createDefault();
                        String respContent = "";

                        String json = "{\"CMD\":\"{\\\"data\\\":{\\\"event\\\":\\\"VideoUpdataEvent\\\",\\\"target\\\":\\\" \\\",\\\"value\\\":\\\" \\\",\\\"name\\\":\\\" \\\",\\\"ip\\\":\\\"10.4.85.143\\\"}}\"}";

                        StringEntity entity = new StringEntity(json,"utf-8");//解决中文乱码问题
                        entity.setContentEncoding("UTF-8");
                        entity.setContentType("application/json");
                        httpPost.setEntity(entity);
                        System.out.println();

                        HttpResponse resp = null;
                        try {
                            resp = client.execute(httpPost);
                            if(resp.getStatusLine().getStatusCode() == 200) {
                                HttpEntity he = resp.getEntity();
                                respContent = EntityUtils.toString(he,"UTF-8");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        log.info("done---true");
                        return new RestResult<>("true", "视频上传成功");
                    }else{
                        log.info("done---false");
                        return new RestResult<>("false", "网络出现问题，请重新提交");
                    }
                }else{
                    log.info("done---false");
                    return new RestResult<>("false", "网络出现问题，请重新提交");
                }
            }
        }else{
            log.info("done---false");
            return new RestResult<>("false", "参数不完整，请重新提交");
        }
    }

}
