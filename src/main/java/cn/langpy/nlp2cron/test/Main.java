package cn.langpy.nlp2cron.test;

import cn.langpy.nlp2cron.CrondUtil;
import cn.langpy.nlp2cron.core.CrondModel;

public class Main {
    public static void main(String[] args) {
        CrondModel.init("src/main/resources/model");
        String test1 = "七点";
        String test2 = "每天晚上7点开始";
        String test3 = "每15分钟一次";
        String test4 = "每2小时一次";
        String test5 = "每天晚上7点开始";
        String test6 = "每天早上7点开始";
        String test7 = "上午一点";
        String cron1 = CrondUtil.toCron(test1);
        String cron2 = CrondUtil.toCron(test2);
        String cron3 = CrondUtil.toCron(test3);
        String cron4 = CrondUtil.toCron(test4);
        String cron5 = CrondUtil.toCron(test5);
        String cron6 = CrondUtil.toCron(test6);
        String cron7 = CrondUtil.toCron(test7);
        System.out.println(test1+" 转为cron表达式："+cron1);
        System.out.println(test2+" 转为cron表达式："+cron2);
        System.out.println(test3+" 转为cron表达式："+cron3);
        System.out.println(test4+" 转为cron表达式："+cron4);
        System.out.println(test5+" 转为cron表达式："+cron5);
        System.out.println(test6+" 转为cron表达式："+cron6);
        System.out.println(test7+" 转为cron表达式："+cron7);
        CrondModel.close();

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


