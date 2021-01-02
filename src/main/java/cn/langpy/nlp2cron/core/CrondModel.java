package cn.langpy.nlp2cron.core;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import java.util.List;

public class CrondModel {

    static SavedModelBundle encoder = null;
    static SavedModelBundle decoder = null;
    static CrondConfig config = null;

    static {
        try {
            config=new CrondConfig();
            encoder = SavedModelBundle.load("model\\encoder_model","serve");
            decoder = SavedModelBundle.load("model\\decoder_model","serve");
        } catch (Exception e) {
        }

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
            encoder = SavedModelBundle.load(path+"/encoder_model","serve");
            decoder = SavedModelBundle.load(path+"/decoder_model","serve");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String predict(String message) {
        Session encoderSession = encoder.session();
        List<Tensor<?>> encoderOut = encoderPredict(encoderSession,message);
        Tensor h = encoderOut.get(0);
        Tensor c = encoderOut.get(1);

        float[][][] startArray = new float[1][1][17];
        startArray[0][0][CrondConfig.str2id.get("S")] = 1;
        Tensor start = Tensor.create(startArray);
        Session decoderSession = decoder.session();
        List<Tensor<?>> out = decoderPredict(decoderSession,start,h,c);
        start = out.get(0);
        StringBuffer stringBuffer = new StringBuffer();
        float[][][] resultValues = (float[][][]) start.copyTo(new float[1][1][17]);
        String sentence = argmax(resultValues[0]);
        stringBuffer.append(sentence);
        h =  out.get(1);
        c = out.get(2);
        while (!"E".equals(sentence)) {
            out = decoderPredict(decoderSession,start,h,c);
            start = out.get(0);
            resultValues = (float[][][]) start.copyTo(new float[1][1][17]);
            sentence = argmax(resultValues[0]);
            h =  out.get(1);
            c = out.get(2);
            if (!"E".equals(sentence)&&sentence.length()>0) {
                stringBuffer.append(sentence);
            }
        }
        start.close();
        h.close();
        c.close();
        return stringBuffer.toString();
    }

    public static List<Tensor<?>> encoderPredict(Session session,String message) {
        Tensor input = toVec(message, new float[1][15][334]);
        List<Tensor<?>> encoderOut = session.runner()
                .feed("serving_default_input_1:0", input)
                .fetch("StatefulPartitionedCall:0")
                .fetch("StatefulPartitionedCall:1")
                .run();
        input.close();
        return encoderOut;
    }

    public static List<Tensor<?>> decoderPredict(Session session,Tensor start,Tensor h,Tensor c) {
        List<Tensor<?>> out = session.runner()
                .feed("serving_default_input_2:0", start)
                .feed("serving_default_input_3:0", h)
                .feed("serving_default_input_4:0", c)
                .fetch("StatefulPartitionedCall:0")
                .fetch("StatefulPartitionedCall:1")
                .fetch("StatefulPartitionedCall:2")
                .run();
        return out;
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
            String str = CrondConfig.id2str.get(maxIndex);

            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }

    private static Tensor toVec(String message, float[][][] vecShape) {
        for(int i=0; i<vecShape[0].length; i++){
            if (i<message.length()){
                String word = message.substring(i,i+1);
                int loc = 0;
                if (config.getWord2id().containsKey(word)) {
                    loc = Integer.parseInt(config.getWord2id().get(word)+"");
                }else{
                    loc = Integer.parseInt(config.getWord2id().get("<UNK>")+"");
                }
                vecShape[0][i][loc] = 1;
            }
        }
        Tensor input = Tensor.create(vecShape);
        return input;
    }

    public static void close() {
        encoder.close();
        decoder.close();
    }
}
