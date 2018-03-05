package com.joymeter.scheduled;



import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyScheduledTest {

    @Scheduled(cron="0/5 * * * * ? ")//每隔5秒调用一次http://cron.qqe2.com/ 在线cron表达式生成
    public void Mytest() {
        System.out.println("进入测试：");
    }
}
