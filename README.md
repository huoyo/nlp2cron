# nlp2cron

#### 介绍
nlp2cron是一个将自然语言转换为cron表达式的工具包，可用于对话机器人的定时任务以及平常开发中的cron表达式识别，识别率在85%左右




#### 使用教程

1.  引入依赖 或者 下载发行版本
```
 <dependency>
    <groupId>cn.langpy</groupId>
    <artifactId>nlp2cron</artifactId>
    <version>1.0-SNAPSHOT</version>
  </dependency>
```
2.  模型配置

下载对应的发行版本的模型，并解压到任意目录，然后在代码种配置模型路径，如：
```java
CrondModel.init("d:/model");
```


3.  使用
```java
 public static void main(String[] args) {
        /*模型初始化，初始化需要时间，可提前进行初始化*/
        CrondModel.init("src/main/resources/model");
        String test1 = "明早八点";
        String test2 = "每天晚上7点开始";
        String test3 = "每15分钟一次";
        String test4 = "每2小时一次";
        String test5 = "每天晚上7点开始";
        String test6 = "每天早上7点开始";
        String test7 = "上午一点执行";
        String cron1 = CrondUtil.toCron(test1);
        String cron2 = CrondUtil.toCron(test2);
        String cron3 = CrondUtil.toCron(test3);
        String cron4 = CrondUtil.toCron(test4);
        String cron5 = CrondUtil.toCron(test5);
        String cron6 = CrondUtil.toCron(test6);
        String cron7 = CrondUtil.toCron(test7);
        /*使用完关闭*/
        CrondModel.close();
        //明早八点 转为cron表达式：0 0 8 3 1 ? 2021
        //每天晚上7点开始 转为cron表达式：0 0 19 * * ? *
        //每15分钟一次 转为cron表达式：0 0/15 * * * ? *
        //每2小时一次 转为cron表达式：0 0 0/2 * * ? *
        //每天晚上7点开始 转为cron表达式：0 0 19 * * ? *
        //每天早上7点开始 转为cron表达式：0 0 7 * * ? *
        //上午一点执行 转为cron表达式：0 0 1 * * ? *
}
```


#### 版本说明

> V1.0-SNAPSHOT：试用版





