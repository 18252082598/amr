package com.joymeter.scheduled;



import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyScheduledTest {

    @Scheduled(cron="0/5 * * * * ? ")
    public void Mytest() {
        System.out.println("进入测试：");
    }
}
