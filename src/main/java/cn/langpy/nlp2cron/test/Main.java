package cn.langpy.nlp2cron.test;

import cn.langpy.nlp2cron.CronModel;

public class Main {
    public static void main(String[] args) {
        String cron = CronModel.toCron("每天上午七点");
        System.out.println(cron);
    }
}


