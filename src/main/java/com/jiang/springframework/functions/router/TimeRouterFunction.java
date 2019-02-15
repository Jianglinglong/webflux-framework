package com.jiang.springframework.functions.router;


import com.jiang.springframework.functions.handler.TimeHandlerFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class TimeRouterFunction {
    private final TimeHandlerFunction timeHandlerFunction;

    @Autowired
    public TimeRouterFunction(TimeHandlerFunction timeHandlerFunction) {
        this.timeHandlerFunction = timeHandlerFunction;
    }

    @Bean
    public RouterFunction<ServerResponse> timeRouter() {
        return RouterFunctions.route(GET("/date"), timeHandlerFunction::getDate)
                .andRoute(GET("/time"), new HandlerFunction<ServerResponse>() {
                    @Override
                    public Mono<ServerResponse> handle(ServerRequest request) {
                        return timeHandlerFunction.getTime(request);
                    }
                })
                .andRoute(GET("/render"), timeHandlerFunction::render);
    }
    @Bean
    RouterFunction<ServerResponse> routerFunction() {
        return  RouterFunctions.route(GET("/"), req ->{
            return ServerResponse.temporaryRedirect(URI.create("/login")).build();
        });
    }
}
