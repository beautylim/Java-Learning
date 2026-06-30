package org.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("org.example.mapper")  // 扫描mapper接口
@EnableTransactionManagement
public class DsSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(DsSecurityApplication.class, args);
    }
}
