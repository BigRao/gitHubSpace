package com.inspur.bjzx.scenesecurityserve.util;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReplaceNull {
    private static Logger log =  Logger.getLogger(ReplaceNull.class);

    public void replaceNull(List<Map<String,Object>> list){
        try{
            for(int i = 0;i<list.size();i++) {
                Iterator iterator = list.get(i).keySet().iterator();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    if (list.get(i).get(key) == null) {
                        list.get(i).put(key, "");
                    }
                    if(list.get(i).get(key) instanceof Integer){
                        list.get(i).put(key,list.get(i).get(key).toString());
                    }
                    if(list.get(i).get(key) instanceof List){
                        replaceNull((List<Map<String,Object>>)list.get(i).get(key));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
