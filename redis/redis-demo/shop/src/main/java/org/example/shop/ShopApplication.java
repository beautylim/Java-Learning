package org.example.shop;

import com.alibaba.nacos.common.utils.ThreadFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@MapperScan("org.example.shop.mapper")
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"org.example.shop", "org.example.auth"})
public class ShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // 可选：禁用将日期写为时间戳的格式，使其变成更易读的 "yyyy-MM-dd'T'HH:mm:ss" 字符串
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    @Bean
    public ExecutorService executorService() {
        return new ThreadPoolExecutor(18, 30, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(12),  new ThreadPoolExecutor.CallerRunsPolicy());
    }
    
}
