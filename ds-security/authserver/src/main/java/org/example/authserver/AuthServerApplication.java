package org.example.authserver;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.crypto.SecretKey;

import static org.example.authserver.constant.Constants.SECRET;

@SpringBootApplication
@MapperScan("org.example.authserver.mapper")  // 扫描mapper接口
@EnableTransactionManagement
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

    @Bean
    public SecretKey secretKey() {
       return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    }


}
