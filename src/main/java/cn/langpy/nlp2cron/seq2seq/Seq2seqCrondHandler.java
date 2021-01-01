package cn.langpy.nlp2cron.seq2seq;

import cn.langpy.nlp2cron.core.CrondModelLoader;
import cn.langpy.nlp2cron.core.TimeType;
import cn.langpy.nlp2cron.rnn.NumUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Tensor;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @name：
 * @function：
 * @author：zhangchang
 * @date 2020/12/31 15:52
 */
public class Seq2seqCrondHandler implements InvocationHandler {
    static SavedModelBundle encoder = null;
    static SavedModelBundle decoder = null;
    static public Map<String, Integer> id2str = new HashMap<>();
    static public Map<Integer,String> str2id = new HashMap<>();
    static {
        try {
            encoder = SavedModelBundle.load("src\\main\\resources\\model\\encoder_model","serve");
            decoder = SavedModelBundle.load("src\\main\\resources\\model\\decoder_model","serve");
        } catch (Exception e) {
        }
        id2str.put("#", 0);
        id2str.put("*", 1);
        id2str.put("0", 2);
        id2str.put("S", 3);
        id2str.put("?", 4);
        id2str.put("E", 5);
        id2str.put("1", 6);
        id2str.put("2", 7);
        id2str.put("3", 8);
        id2str.put("4", 9);
        id2str.put("5", 10);
        id2str.put("7", 11);
        id2str.put("6", 12);
        id2str.put("8", 13);
        id2str.put("9", 14);
        id2str.put("/", 15);
        id2str.put("<UNK>", 16);

        str2id.put( 0 ,"#");
        str2id.put( 1 ,"*");
        str2id.put( 2 ,"0");
        str2id.put( 3 ,"S");
        str2id.put( 4 ,"?");
        str2id.put( 5 ,"E");
        str2id.put( 6 ,"1");
        str2id.put( 7 ,"2");
        str2id.put( 8 ,"3");
        str2id.put( 9 ,"4");
        str2id.put( 10,"5");
        str2id.put( 11,"7");
        str2id.put( 12,"6");
        str2id.put( 13,"8");
        str2id.put( 14,"9");
        str2id.put( 15,"/");
        str2id.put( 16,"<UNK>");
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String message = args[0].toString();
        TimeType timeType = NumUtil.getTimeType(message);
        switch (method.getName()){
            case "toCron":
                List<Tensor> encoderResult = encoderPredict(message);
                String re = decoderPredict(encoderResult.get(0), encoderResult.get(1));
                re = cn.langpy.nlp2cron.seq2seq.NumUtil.cronHandler(re,timeType);
                return re;
            default:
                throw new Exception("未知的方法:"+method.getName());
        }
    }
    public static List<Tensor> encoderPredict(String message) {
        Tensor input = toencoderVec(message, new float[1][14][54]);
        /*1,1000*/
        List<Tensor<?>> out = encoder.session().runner()
                .feed("serving_default_input_1:0", input)
                .fetch("StatefulPartitionedCall:0")
                .fetch("StatefulPartitionedCall:1")
                .run();

        Tensor outputH = out.get(0);
        Tensor outputS = out.get(1);
//        input.close();
//        outputH.close();
//        outputS.close();

        return Arrays.asList(outputH,outputS);
    }

    public static String decoderPredict(Tensor h,Tensor c) {
        float[][][] startArray = new float[1][1][17];
        startArray[0][0][id2str.get("S")] = 1;
        Tensor start = Tensor.create(startArray);
        /*1,1000*/
        List<Tensor<?>> out = decoder.session().runner()
                .feed("serving_default_input_2:0", start)
                .feed("serving_default_input_3:0", h)
                .feed("serving_default_input_4:0", c)
                .fetch("StatefulPartitionedCall:0")
                .fetch("StatefulPartitionedCall:1")
                .fetch("StatefulPartitionedCall:2")
                .run();
        start = out.get(0);
        StringBuffer stringBuffer = new StringBuffer();
        float[][][] resultValues = (float[][][]) start.copyTo(new float[1][1][17]);
        String sentence = argmax(resultValues[0]);
        stringBuffer.append(sentence);
        h =  out.get(1);
        c = out.get(2);
        while (!"E".equals(sentence)) {
            out = decoder.session().runner()
                    .feed("serving_default_input_2:0", start)
                    .feed("serving_default_input_3:0", h)
                    .feed("serving_default_input_4:0", c)
                    .fetch("StatefulPartitionedCall:0")
                    .fetch("StatefulPartitionedCall:1")
                    .fetch("StatefulPartitionedCall:2")
                    .run();
            start = out.get(0);
            resultValues = (float[][][]) start.copyTo(new float[1][1][17]);
            sentence = argmax(resultValues[0]);
            h =  out.get(1);
            c = out.get(2);
            if (!"E".equals(sentence)&&sentence.length()>0) {
                stringBuffer.append(sentence);
            }
        }


        return stringBuffer.toString();
    }

    private static String argmax(float[][] input) {
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
                stringBuilder.append("#");
                break;
            }
            String str = str2id.get(maxIndex);

            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }

    public static JSONObject getWord2id() {
        ClassPathResource classPathResource = new ClassPathResource("word2id2.json");
        String mapping = null;
        try {
            mapping = IOUtils.toString(classPathResource.getInputStream(),"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject word2id = JSON.parseObject(mapping);
        return word2id;
    }

    private static Tensor toencoderVec(String message, float[][][] vecShape) {
        /*1,14,54*/
        JSONObject word2id = getWord2id();
        for(int i=0; i<vecShape[0].length; i++){
            if (i<message.length()){
                String word = message.substring(i,i+1);
                int loc = 0;
                if (word2id.containsKey(word)) {
                    loc = Integer.parseInt(word2id.get(word)+"");
                }else{
                    loc = Integer.parseInt(word2id.get("<UNK>")+"");
                }
                vecShape[0][i][loc] = 1;
            }
        }
        Tensor input = Tensor.create(vecShape);
        return input;
    }


}
