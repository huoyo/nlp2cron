package cn.langpy.nlp2cron.core;

/**
 * @name：
 * @function：
 * @author：zhangchang
 * @date 2020/12/31 15:49
 */
public interface CrondInteface {
    String toCron(String message);
    String toDate(String message);
    String toDateTime(String message);
    String toTime(String message);
}
