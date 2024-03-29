package cn.langpy.nlp2cron.handler;

import cn.langpy.nlp2cron.core.TimeType;

import java.time.LocalDate;


public class NumUtil {

    public static String cronHandler(String re, TimeType timeType) {
        String[] reSplit = re.split("#");
        if (timeType==TimeType.today) {
            LocalDate now = LocalDate.now();
            re = reSplit[0]+" "+reSplit[1]+" "+reSplit[2]+" "+now.getDayOfMonth()+" "+now.getMonth().getValue()+" ? "+now.getYear();
        } else if (timeType==TimeType.tomorrow) {
            LocalDate now = LocalDate.now().plusDays(1);
            re = reSplit[0]+" "+reSplit[1]+" "+reSplit[2]+" "+now.getDayOfMonth()+" "+now.getMonth().getValue()+" ? "+now.getYear();
        }else{
            re = String.join(" ",reSplit);
        }
        return re;
    }

    public static String dateHandler(String re,TimeType timeType) {
        if (timeType==TimeType.today) {
            LocalDate now = LocalDate.now();
            re = now.getYear()+"-"+(now.getMonthValue()<10?"0"+now.getMonthValue():now.getMonthValue())+"-"+(now.getDayOfMonth()<10?"0"+now.getDayOfMonth():now.getDayOfMonth());
            return re;
        } else if (timeType==TimeType.tomorrow) {
            LocalDate now = LocalDate.now().plusDays(1);
            re = now.getYear()+"-"+(now.getMonthValue()<10?"0"+now.getMonthValue():now.getMonthValue())+"-"+(now.getDayOfMonth()<10?"0"+now.getDayOfMonth():now.getDayOfMonth());
            return re;
        }
        return null;
    }

    public static String dateTimeHandler(String re,TimeType timeType) {
        String[] reSplit = re.split("#");
        if (timeType==TimeType.today) {
            LocalDate now = LocalDate.now();
            re = now.getYear()+"-"+(now.getMonthValue()<10?"0"+now.getMonthValue():now.getMonthValue())+"-"+(now.getDayOfMonth()<10?"0"+now.getDayOfMonth():now.getDayOfMonth())+" "+(reSplit[2].length()==1?"0"+reSplit[2]:reSplit[2])+":"+(reSplit[1].length()==1?"0"+reSplit[1]:reSplit[1])+":"+(reSplit[0].length()==1?"0"+reSplit[0]:reSplit[0]);
            return re;
        } else if (timeType==TimeType.tomorrow) {
            LocalDate now = LocalDate.now().plusDays(1);
            re = now.getYear()+"-"+(now.getMonthValue()<10?"0"+now.getMonthValue():now.getMonthValue())+"-"+(now.getDayOfMonth()<10?"0"+now.getDayOfMonth():now.getDayOfMonth())+" "+(reSplit[2].length()==1?"0"+reSplit[2]:reSplit[2])+":"+(reSplit[1].length()==1?"0"+reSplit[1]:reSplit[1])+":"+(reSplit[0].length()==1?"0"+reSplit[0]:reSplit[0]);
            return re;
        }
        return null;
    }
    public static String timeHandler(String re,TimeType timeType) {
        String[] reSplit = re.split("#");
        return reSplit[2]+":"+reSplit[1]+":"+reSplit[0];
    }

    public static TimeType getTimeType(String message) {
        if (message.matches(".*(明天|明早|明晚).*")) {
            return TimeType.tomorrow;
        }else if((message.matches(".*(今天|今晚|今早|早上|晚上).*") && !message.contains("每")) || message.matches(".{1,2}(:|点)") ) {
            return TimeType.today;
        }
        return TimeType.others;
    }
}
