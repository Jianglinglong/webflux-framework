package com.jiang.springframework.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringWebFluxTemplateEngine;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Order(-2)
@Component
public class GloableExceptionHandler implements WebExceptionHandler {

    @Autowired
    private SpringWebFluxTemplateEngine templateEngine;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        boolean contains = headers.getAccept().contains(MediaType.APPLICATION_JSON);
        System.out.println(ex.getClass().getName());
        String error ;
        if (ex instanceof MethodNotAllowedException){
            MethodNotAllowedException methodNotAllowedException = (MethodNotAllowedException) ex;
            Set<HttpMethod> methods = methodNotAllowedException.getSupportedMethods();
            String httpMethod = methodNotAllowedException.getHttpMethod();
            error = httpMethod+" is not supported! "+methods+" are supported !";
        }else if (ex instanceof ResponseStatusException){
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            HttpStatus status = responseStatusException.getStatus();
            String message = responseStatusException.getMessage();
            if (responseStatusException.getStatus() == HttpStatus.NOT_FOUND){
                error  = responseStatusException.getReason();
            }else {
                error = message;
            }
        } else {
            error = ex.getMessage();
        }
        response.setStatusCode(HttpStatus.OK);

        if (contains) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        } else {
            response.getHeaders().setContentType(MediaType.TEXT_HTML);
            Map<String, Object> map = new HashMap<>();
            map.put("error", ex.getMessage());
             error = templateEngine.process("error", new Context(Locale.CHINA, map));
        }
        return response.writeWith(Mono.just(response.bufferFactory().wrap(error.getBytes())));
    }
}
