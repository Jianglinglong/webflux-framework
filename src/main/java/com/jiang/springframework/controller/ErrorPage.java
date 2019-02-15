package com.jiang.springframework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorPage {
    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/index.html")
    public String index() {
        return "index";
    }
}
