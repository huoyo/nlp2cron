package cn.langpy.nlp2cron.core;

import org.tensorflow.SavedModelBundle;

/**
 * @name：
 * @function：
 * @author：zhangchang
 * @date 2020/12/31 10:43
 */
public class ModelLoader {

    public SavedModelBundle loadModel(String path) {
        SavedModelBundle tensorflowModelBundle = null;
        try {
            tensorflowModelBundle = SavedModelBundle.load(path,"serve");
        } catch (Exception e) {
        }
        return tensorflowModelBundle;
    }
}
