package cn.langpy.nlp2cron.handler;

import cn.langpy.nlp2cron.core.CrondModel;
import cn.langpy.nlp2cron.core.TimeType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class CrondHandler implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String message = args[0].toString();
        String re = CrondModel.predict(message);
        TimeType timeType = NumUtil.getTimeType(message);

        switch (method.getName()){
            case "toCron":
                return NumUtil.cronHandler(re, timeType);
            case "toDateTime":
                return NumUtil.dateTimeHandler(re, timeType);
            case "toTime":
                return NumUtil.timeHandler(re, timeType);
            case "toDate":
                return NumUtil.dateHandler(re, timeType);
            default:
                throw new Exception("未知的方法:"+method.getName());
        }
    }

}
