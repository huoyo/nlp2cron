package cn.langpy.nlp2cron.test;

import cn.langpy.nlp2cron.CronUtil;

public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            String cron = CronUtil.toCron("一小时一次");
            System.out.println(cron);
        }

    }
}


