package com.example.hx_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 应用启动类
 */
@SpringBootApplication
@EnableScheduling
public class ShoubaApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ShoubaApplication.class, args);
    }
}
