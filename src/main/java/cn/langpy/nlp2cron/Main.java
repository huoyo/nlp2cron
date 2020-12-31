package cn.langpy.nlp2cron;

import cn.langpy.nlp2cron.CrondUtil;

public class Main {
    public static void main(String[] args) {
        String s = "每天下午七点";
        String cron = CrondUtil.toCron(s);
        System.out.println(s+"："+cron);
         s = "早上七点";
        cron = CrondUtil.toCron(s);
        System.out.println(s+"："+cron);
        s = "每1小时一次";
        cron = CrondUtil.toCron(s);
        System.out.println(s+"："+cron);
    }
}


