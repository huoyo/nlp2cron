package cn.langpy.nlp2cron.factory;

import cn.langpy.nlp2cron.rnn.RnnCrondHandler;
import cn.langpy.nlp2cron.seq2seq.Seq2seqCrondHandler;

import java.lang.reflect.Proxy;

/**
 * @name：
 * @function：
 * @author：zhangchang
 * @date 2020/12/31 15:58
 */
public class CrondFactory {
    public static  <T>T getCrondServce(Class<T> target) {
//        T o = (T) Proxy.newProxyInstance(target.getClassLoader(), new Class[]{target}, new RnnCrondHandler());
        T o = (T) Proxy.newProxyInstance(target.getClassLoader(), new Class[]{target}, new Seq2seqCrondHandler());
        return o;
    }
}
