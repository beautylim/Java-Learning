package org.example.auth.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.example.auth.context.TokenContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonFeignClient {

    @Bean
    public RequestInterceptor tokenInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 从 ThreadLocal 获取 token
                String token = TokenContext.get();
                if (token != null) {
                    template.header("Authorization", "Bearer " + token);
                }
            }
        };
    }
}
