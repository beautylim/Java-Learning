package org.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class TimeFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        long start = System.currentTimeMillis();
        log.info("Request URI : {}, enter", path);

        return chain.filter(exchange).doFinally((s) -> {
            long end = System.currentTimeMillis();
            log.info("Request URI : {}, exit, totalTime: {} millisec", path, end - start);

        });
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
