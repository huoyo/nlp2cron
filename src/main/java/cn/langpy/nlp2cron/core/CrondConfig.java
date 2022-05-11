package cn.langpy.nlp2cron.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;


public class CrondConfig {
    public String modelPath = "src\\main\\resources\\model";
    public JSONObject inputId2Word = null;
    public JSONObject inputWord2Id = null;
    public JSONObject outputId2Word = null;

    {
        try (InputStream inputWordsStream = CrondConfig.class.getResourceAsStream("/input.json"); InputStream outputWordsStream = CrondConfig.class.getResourceAsStream("/output.json")) {
            String inMapping = IOUtils.toString(inputWordsStream, "utf-8");
            inputId2Word = JSON.parseObject(inMapping);
            inputWord2Id = new JSONObject();
            for (String id : inputId2Word.keySet()) {
                String word = inputId2Word.getString(id);
                inputWord2Id.put(word, Integer.valueOf(id));
            }
            String outMapping = IOUtils.toString(outputWordsStream, "utf-8");
            outputId2Word = JSON.parseObject(outMapping);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getModelPath() {
        return modelPath;
    }

    public JSONObject getInputId2Word() {
        return inputId2Word;
    }

    public void setInputId2Word(JSONObject inputId2Word) {
        this.inputId2Word = inputId2Word;
    }

    public JSONObject getInputWord2Id() {
        return inputWord2Id;
    }

    public void setInputWord2Id(JSONObject inputWord2Id) {
        this.inputWord2Id = inputWord2Id;
    }

    public JSONObject getOutputId2Word() {
        return outputId2Word;
    }

    public void setOutputId2Word(JSONObject outputId2Word) {
        this.outputId2Word = outputId2Word;
    }
}
