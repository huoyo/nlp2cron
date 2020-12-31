package cn.langpy.nlp2cron;

import cn.langpy.nlp2cron.CrondUtil;

public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            String cron = CrondUtil.toCron("明晚七点");
            System.out.println(cron);
        }
    }
}


