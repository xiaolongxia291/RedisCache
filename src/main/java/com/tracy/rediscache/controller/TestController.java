package com.tracy.rediscache.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestController {
    @GetMapping("/search1")
    public String test1(){
        return "success";
    }
    @GetMapping("/search2")
    public String test2(){
        return "success";
    }
    @GetMapping("/search3")
    public String test3(){
        return "success";
    }
}
