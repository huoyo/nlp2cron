package cn.langpy.nlp2cron.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;
import java.util.Map;

/**
 * @name：
 * @function：
 * @author：zhangchang
 * @date 2020/12/31 10:43
 */
public class Config {
    public  String modelPath = "src\\main\\resources\\cronmodel";
    public  String modelInputName = "serving_default_embedding_input:0";
    public  String modelOutputName = "StatefulPartitionedCall:0";
    public  Integer modelInputSize = 46;
    public  Integer modelOutputSize = 15;
    public  JSONObject word2id = null;
    public  Map<Integer, String> id2str = new HashMap<Integer, String>();

    {
        try {
            ClassPathResource classPathResource = new ClassPathResource("word2id.json");
            String mapping = IOUtils.toString(classPathResource.getInputStream(),"utf-8");
            word2id = JSON.parseObject(mapping);
        } catch (Exception e) {
            e.printStackTrace();
        }
        id2str.put(1,"0");
        id2str.put(2,"1");
        id2str.put(3,"2");
        id2str.put(4,"3");
        id2str.put(5,"4");
        id2str.put(6,"5");
        id2str.put(7,"6");
        id2str.put(8,"7");
        id2str.put(9,"8");
        id2str.put(10,"9");
        id2str.put(11,"/");
        id2str.put(12,"*");
        id2str.put(13,"?");
        id2str.put(14,"#");
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public String getModelInputName() {
        return modelInputName;
    }

    public void setModelInputName(String modelInputName) {
        this.modelInputName = modelInputName;
    }

    public String getModelOutputName() {
        return modelOutputName;
    }

    public void setModelOutputName(String modelOutputName) {
        this.modelOutputName = modelOutputName;
    }

    public Integer getModelInputSize() {
        return modelInputSize;
    }

    public void setModelInputSize(Integer modelInputSize) {
        this.modelInputSize = modelInputSize;
    }

    public Integer getModelOutputSize() {
        return modelOutputSize;
    }

    public void setModelOutputSize(Integer modelOutputSize) {
        this.modelOutputSize = modelOutputSize;
    }

    public JSONObject getWord2id() {
        return word2id;
    }

    public void setWord2id(JSONObject word2id) {
        this.word2id = word2id;
    }

    public Map<Integer, String> getId2str() {
        return id2str;
    }

    public void setId2str(Map<Integer, String> id2str) {
        this.id2str = id2str;
    }
}
