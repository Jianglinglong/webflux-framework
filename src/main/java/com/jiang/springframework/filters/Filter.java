package com.jiang.springframework.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Order(1)
@Component
public class Filter implements WebFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        logger.info("访问：{}",path);
        ServerHttpResponse response = exchange.getResponse();
        if ("complete".equals(exchange.getRequest().getQueryParams().getFirst("complete"))){
            ServerWebExchange webExchange = exchange.mutate().response(exchange.getResponse()).build();
            //转发
            ServerWebExchange build = exchange.mutate().request(exchange.getRequest().mutate().path("/index.html").build()).build();
            return chain.filter(build);
        }
        return chain.filter(exchange);
    }
}
