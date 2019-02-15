package com.jiang.springframework;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.netty.http.server.HttpServer;

import java.io.IOException;


public class WebFluxApplication {
    public static void main(String[] args) throws IOException {
        run("127.0.0.1",8080,WebFluxApplication.class);
    }
    public static void run(String host,int port,String ...basePackages){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(basePackages);
        //通过ApplicationContext创建HttpHandler
        HttpHandler httpHandler = WebHttpHandlerBuilder.applicationContext(applicationContext).build();
        ReactorHttpHandlerAdapter httpHandlerAdapter = new ReactorHttpHandlerAdapter(httpHandler);
        HttpServer.create().host(host).port(port).handle(httpHandlerAdapter).bindNow();
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void run(String host,int port,Class bootClass){
        run(host,port,bootClass.getName().replace("."+bootClass.getSimpleName(),""));
    }
}
