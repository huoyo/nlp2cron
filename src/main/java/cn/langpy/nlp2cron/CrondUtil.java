package cn.langpy.nlp2cron;

import cn.langpy.nlp2cron.core.CrondInteface;
import cn.langpy.nlp2cron.factory.CrondFactory;

/**
 * @name：
 * @function：
 * @author：zhangchang
 * @date 2020/12/31 15:54
 */
public class CrondUtil {
    static CrondInteface crondInteface = null;

    static {
        crondInteface = CrondFactory.getCrondServce(CrondInteface.class);
    }

    public static String toCron(String message) {
        return crondInteface.toCron(message);
    }

    public static String toDate(String message) {
        return crondInteface.toDate(message);
    }

    public static String toDateTime(String message) {
        return crondInteface.toDateTime(message);
    }

    public static String toTime(String message) {
        return crondInteface.toTime(message);
    }
}
