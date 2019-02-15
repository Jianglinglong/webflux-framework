package com.jiang.springframework.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexJson {

    @GetMapping("/json")
    public String json(){
        return "json";
    }

    @GetMapping("/errors")
    public String errors(){
        throw new RuntimeException("error");
    }
}
