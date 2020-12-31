package cn.langpy.nlp2cron;

import cn.langpy.nlp2cron.core.Config;
import cn.langpy.nlp2cron.core.ModelLoader;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Tensor;

/**
 * @name：
 * @function：
 * @author：zhangchang
 * @date 2020/12/31 10:47
 */
public class CronUtil {

    static SavedModelBundle tensorflowModelBundle = null;
    static Config config = null;
    static {
        config = new Config();
        ModelLoader modelLoader = new ModelLoader();
        tensorflowModelBundle = modelLoader.loadModel(config.getModelPath());
    }

    public static String toCron(String message) {
        Tensor input = toVec(message, new float[1][config.getModelInputSize()]);
        Tensor output = tensorflowModelBundle.session().runner()
                .feed(config.getModelInputName(), input)
                .fetch(config.getModelOutputName()).run().get(0);
        float[][][] resultValues = (float[][][]) output.copyTo(new float[1][config.getModelInputSize()][config.getModelOutputSize()]);
        input.close();
        output.close();
        return argmax(resultValues[0]);
    }

    private static Tensor toVec(String message, float[][] vecShape) {
        for(int i=0; i<vecShape[0].length; i++){
            if (i<message.length()){
                String word = message.substring(i,i+1);
                if (config.getWord2id().containsKey(word)) {
                    vecShape[0][i] = Integer.parseInt(config.getWord2id().get(word)+"");
                }else{
                    vecShape[0][i] = Integer.parseInt(config.getWord2id().get("<UNK>")+"");
                }
            }
        }
        Tensor input = Tensor.create(vecShape);
        return input;
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
                break;
            }
            String str = config.getId2str().get(maxIndex);
            if ("#".equals(str)) {
                stringBuilder.append(" ");
            }else{
                stringBuilder.append(str);
            }

        }
        return stringBuilder.toString();
    }

}
