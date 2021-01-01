package cn.langpy.nlp2cron.test;

import cn.langpy.nlp2cron.CrondUtil;
import cn.langpy.nlp2cron.core.CrondModelLoader;

public class Main {
    public static void main(String[] args) {
        CrondModelLoader.init();
        String s = "明早七点";
        String cron = CrondUtil.toCron(s);
        System.out.println(s+"："+cron);
        s = "早上7点";
        cron = CrondUtil.toCron(s);
        System.out.println(s+"："+cron);
        s = "每2小时一次";
        cron = CrondUtil.toCron(s);
        System.out.println(s+"："+cron);
    }

    public static void testCron() {
        String s = "每2个小时一次";
        String cron = CrondUtil.toCron(s);
        System.out.println(s+"："+cron);
    }
    public static void testDate() {
        String s = "明早七点";
        String cron = CrondUtil.toDate(s);
        System.out.println(s+"："+cron);
    }
    public static void testDateTime() {
        String s = "明早七点";
        String cron = CrondUtil.toDateTime(s);
        System.out.println(s+"："+cron);
    }
}


