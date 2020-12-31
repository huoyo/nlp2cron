package cn.langpy.nlp2cron.core;

import java.time.LocalDate;

/**
 * @name：
 * @function：
 * @author：zhangchang
 * @date 2020/12/31 16:26
 */
public class NumUtil {

    public static String cronHandler(String re,TimeType timeType) {
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
            re = now.getYear()+"-"+now.getMonthValue()+"-"+now.getDayOfMonth();
            return re;
        } else if (timeType==TimeType.tomorrow) {
            LocalDate now = LocalDate.now().plusDays(1);
            re = now.getYear()+"-"+now.getMonthValue()+"-"+now.getDayOfMonth();
            return re;
        }
        return null;
    }

    public static String dateTimeHandler(String re,TimeType timeType) {
        String[] reSplit = re.split("#");
        if (timeType==TimeType.today) {
            LocalDate now = LocalDate.now();
            re = now.getYear()+"-"+now.getMonthValue()+"-"+now.getDayOfMonth()+" "+reSplit[2]+":"+reSplit[1]+":"+reSplit[0];
            return re;
        } else if (timeType==TimeType.tomorrow) {
            LocalDate now = LocalDate.now().plusDays(1);
            re = now.getYear()+"-"+now.getMonthValue()+"-"+now.getDayOfMonth()+" "+reSplit[2]+":"+reSplit[1]+":"+reSplit[0];
            return re;
        }
        return null;
    }
    public static String timeHandler(String re,TimeType timeType) {
        String[] reSplit = re.split("#");
        return reSplit[2]+":"+reSplit[1]+":"+reSplit[0];
    }

    public static TimeType getTimeType(String re) {
        if (re.matches(".*(明天|明早|明晚).*")) {
            return TimeType.tomorrow;
        }else if(re.matches(".*(今天|今晚|今早).*") || re.matches(".{1,2}(:|点)") ) {
            return TimeType.today;
        }
        return TimeType.others;
    }
}
