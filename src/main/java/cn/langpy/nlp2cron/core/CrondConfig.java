package cn.langpy.nlp2cron.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.util.*;


public class CrondConfig {
    public  String modelPath = "src\\main\\resources\\model";
    public  JSONObject word2id = null;
    public List<String> outputStrs = null;
    static public Map<Integer, String> id2str = new HashMap<>();
    static public Map<String, Integer> str2id = new HashMap<>();

    {
        try {
            ClassPathResource classPathResource = new ClassPathResource("word2id.json");
            String mapping = IOUtils.toString(classPathResource.getInputStream(),"utf-8");
            word2id = JSON.parseObject(mapping);
        } catch (Exception e) {
            e.printStackTrace();
        }
        outputStrs = Arrays.asList("#","*","0","S","?","E","1","2","3","5","4","7","8","6","9","/", "<UNK>");
        for (int i = 0; i <outputStrs.size() ; i++) {
            str2id.put(outputStrs.get(i), i);
            id2str.put(i,outputStrs.get(i));
        }
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public JSONObject getWord2id() {
        return word2id;
    }

    public void setWord2id(JSONObject word2id) {
        this.word2id = word2id;
    }

    public List<String> getOutputStrs() {
        return outputStrs;
    }

    public void setOutputStrs(List<String> outputStrs) {
        this.outputStrs = outputStrs;
    }

    public static Map<Integer, String> getId2str() {
        return id2str;
    }

    public static void setId2str(Map<Integer, String> id2str) {
        CrondConfig.id2str = id2str;
    }

    public static Map<String, Integer> getStr2id() {
        return str2id;
    }

    public static void setStr2id(Map<String, Integer> str2id) {
        CrondConfig.str2id = str2id;
    }
}
