package cn.langpy.nlp2cron;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CronModel {

    private static final Session tensorflowSession;
    private static JSONObject word2id = null;
    static Map<Integer, String> id2str = new HashMap<Integer, String>();

    static {
        SavedModelBundle tensorflowModelBundle = null;
        try {
            tensorflowModelBundle = SavedModelBundle.load("src\\main\\resources\\cronmodel","serve");
        } catch (Exception e) {
        }
        tensorflowSession = tensorflowModelBundle.session();
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


    public static String toCron(String message) {
        Tensor input = toVec(message, new float[1][46]);
        Tensor output = tensorflowSession.runner()
                .feed("serving_default_embedding_input:0", input)
                .fetch("StatefulPartitionedCall:0").run().get(0);
        float[][][] resultValues = (float[][][]) output.copyTo(new float[1][46][15]);
        return argmax(resultValues[0]);
    }

    public static Tensor toVec(String message, float[][] vecShape) {
        for(int i=0; i<vecShape[0].length; i++){
            if (i<message.length()){
                String word = message.substring(i,i+1);
                if (word2id.containsKey(word)) {
                    vecShape[0][i] = Integer.parseInt(word2id.get(word)+"");
                }else{
                    vecShape[0][i] = Integer.parseInt(word2id.get("<UNK>")+"");
                }
            }
        }
        Tensor input = Tensor.create(vecShape);
        return input;
    }
    public static String argmax(float[][] input) {
        List<Integer> list = new ArrayList<Integer>();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i< input.length; i++) {
            double maxValue = 0;
            int maxIndex = 0;
            for (int j = 0; j <input[i].length ; j++) {
                if (input[i][j]>maxValue) {
                    maxValue = input[i][j];
                    maxIndex = j;
                }
            }
            if (maxIndex==0) {
                break;
            }
            String str = id2str.get(maxIndex);
            if ("#".equals(str)) {
                stringBuilder.append(" ");
            }else{
                stringBuilder.append(str);
            }

        }
        return stringBuilder.toString();
    }

}
