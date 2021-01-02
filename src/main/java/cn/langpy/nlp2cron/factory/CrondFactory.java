package cn.langpy.nlp2cron.factory;

import cn.langpy.nlp2cron.handler.CrondHandler;

import java.lang.reflect.Proxy;


public class CrondFactory {
    public static  <T>T getCrondServce(Class<T> target) {
        T o = (T) Proxy.newProxyInstance(target.getClassLoader(), new Class[]{target}, new CrondHandler());
        return o;
    }
}
