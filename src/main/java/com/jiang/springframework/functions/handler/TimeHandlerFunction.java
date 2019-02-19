package com.jiang.springframework.functions.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * body 方法生产json/xml等数据
 * render方法返回视图
 */
@Component
public class TimeHandlerFunction {

    public Mono<ServerResponse> getTime(ServerRequest serverRequest) {
        return time(serverRequest).onErrorReturn("some errors has happened !").flatMap(s -> {
            return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).syncBody(s);
        });
    }

    private Mono<String> time(ServerRequest request) {
        //using Mono<T> defer(Supplier<? extends Mono<? extends T>> supplier)
        //用以响应Mono.onErrorXXX();
        return Mono.defer(() -> {
            String format = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
            return Mono.just("time is:" + format + "," + request.queryParam("name").get());
        });
    }

    public Mono<ServerResponse> getDate(ServerRequest serverRequest) {
        return date(serverRequest).flatMap(s -> {
            return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).syncBody(s).onErrorResume(e -> {
                return Mono.just("exception message:" + e.getMessage()).flatMap(msg -> {
                    return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).syncBody(msg);
                });
            });
        });
    }

    private Mono<String> date(ServerRequest request) {
        return Mono.defer(() -> {
            String format = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()) + request.queryParam("name").get();
            return Mono.just(format);
        });
    }

    public Mono<ServerResponse> render(ServerRequest serverRequest) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("path", serverRequest.path());
        return ServerResponse.ok().contentType(MediaType.TEXT_HTML).render("index", map);
    }
}
