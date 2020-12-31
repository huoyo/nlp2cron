package cn.langpy.nlp2cron.factory;

import cn.langpy.nlp2cron.core.CrondHandler;

import java.lang.reflect.Proxy;

/**
 * @name：
 * @function：
 * @author：zhangchang
 * @date 2020/12/31 15:58
 */
public class CrondFactory {
    public static  <T>T getCrondServce(Class<T> target) {
        T o = (T) Proxy.newProxyInstance(target.getClassLoader(), new Class[]{target}, new CrondHandler());
        return o;
    }
}
