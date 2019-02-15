package com.jiang.springframework.controller;

import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Controller
@RequestMapping("/hello")
public class IndexPage {
    @GetMapping("mono.html")
    public Mono<String> hello(ServerWebExchange exchange, Model model){
        int i = 1/0;
        String path = "/hello/mono.html";
        HashMap<String, Object> map = new HashMap<>();
        map.put("path",path);
        ServerHttpRequest request = exchange.getRequest();
        RequestPath requestPath = request.getPath();
        map.put("full_path",requestPath.value());
        model.addAllAttributes(map);
        Mono<WebSession> session = exchange.getSession();
        session.block().getAttributes().put("session","session");
        return Mono.just("hello");
    }

    @GetMapping("index.html")
    public String hello1(Model model){
        String path = "/hello/index.html";
        model.addAttribute("path",path);
        return "hello";
    }
}
