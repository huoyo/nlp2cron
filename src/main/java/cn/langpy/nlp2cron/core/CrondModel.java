package cn.langpy.nlp2cron.core;

import com.alibaba.fastjson.JSONObject;
import org.tensorflow.RawTensor;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.ndarray.NdArray;
import org.tensorflow.ndarray.StdArrays;
import org.tensorflow.ndarray.buffer.DoubleDataBuffer;
import org.tensorflow.ndarray.buffer.FloatDataBuffer;
import org.tensorflow.types.TFloat32;

import java.util.ArrayList;
import java.util.List;


public class CrondModel {

    static Session model = null;
    static CrondConfig config = null;

    static {
        config=new CrondConfig();
    }


    public static void init(String path) {
        loadModel(path);
    }

    public static void init(CrondConfig crondConfig) {
        config = crondConfig;
        loadModel(config.getModelPath());
    }

    public static void init() {
        loadModel(config.getModelPath());
    }

    private static void loadModel(String path) {
        try {
            SavedModelBundle savedModel = SavedModelBundle.load(path, "serve");
            model = savedModel.session();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String predict(String message) {
        Tensor output = model.runner()
                .feed("serving_default_input_1:0", toVec(message))
                .fetch("StatefulPartitionedCall:0")
                .run().get(0);
        RawTensor rawTensor = output.asRawTensor();
        FloatDataBuffer floatDataBuffer = rawTensor.data().asFloats();
        List<Float> labelIndexs = new ArrayList<>();
        List<String> crons = new ArrayList<>();
        JSONObject id2Word = config.getOutputId2Word();
        for (long i = 0; i < floatDataBuffer.size(); i++) {
            if ((i+1)%127!=0) {
                labelIndexs.add(floatDataBuffer.getFloat(i));
            }else {
                int maxIndex = argmax(labelIndexs);
                String string = id2Word.getString(maxIndex + "");
                crons.add(string);
                labelIndexs.clear();
            }
        }
        rawTensor.close();
        return String.join("#",crons);
    }

    private static int argmax(List<Float> labelIndexs) {
        double maxValue = 0;
        int maxIndex = 0;
        for (int i = 0; i < labelIndexs.size(); i++) {
            if (labelIndexs.get(i) > maxValue) {
                maxValue = labelIndexs.get(i);
                maxIndex = i;
            }
        }
        return maxIndex;
    }


    private static Tensor toVec(String message) {
        float[][] vecShape = new float[1][40];
        JSONObject word2id = config.getInputWord2Id();

        char[] chars = message.toCharArray();
        int i = 0;
        for (char aChar : chars) {
            if (word2id.containsKey(aChar+"")) {
                vecShape[0][i] = word2id.getInteger(aChar+"");
            }else {
                vecShape[0][i] = word2id.getInteger("<UNK>");
            }
            i++;
        }
        NdArray ndArray = StdArrays.ndCopyOf(vecShape);
        return TFloat32.tensorOf(ndArray);
    }

    public static void close() {
        model.close();
    }
}
