package com.example.hx_api;

import com.example.hx_api.Service.UpDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class ShoubaApplication {
    @Autowired
    UpDate upDate;

    public static void main(String[] args) {
        SpringApplication.run(ShoubaApplication.class, args);
    }

    @Scheduled(cron = "00 30 23 * * ?")
    public void start(){
        System.out.println("执行定时任务");
        upDate.start();
    }

}
