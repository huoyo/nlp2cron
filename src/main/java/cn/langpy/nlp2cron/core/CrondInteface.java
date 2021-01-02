package cn.langpy.nlp2cron.core;


public interface CrondInteface {
    String toCron(String message);
    String toDate(String message);
    String toDateTime(String message);
    String toTime(String message);
}
