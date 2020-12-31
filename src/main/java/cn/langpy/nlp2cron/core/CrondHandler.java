package cn.langpy.nlp2cron.core;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Tensor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.LocalDate;

/**
 * @name：
 * @function：
 * @author：zhangchang
 * @date 2020/12/31 15:52
 */
public class CrondHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String message = args[0].toString();
        String re = ModelLoader.predict(message);
        TimeType timeType = NumUtil.getTimeType(message);
        if (method.getName().equals("toCron")) {
            return NumUtil.cronHandler(re,timeType);
        }else if (method.getName().equals("toDateTime")){
            return NumUtil.dateTimeHandler(re,timeType);
        }else if (method.getName().equals("toTime")){
            return NumUtil.timeHandler(re,timeType);
        }else if (method.getName().equals("toDate")){
            return NumUtil.dateHandler(re,timeType);
        }
        return null;
    }

}
