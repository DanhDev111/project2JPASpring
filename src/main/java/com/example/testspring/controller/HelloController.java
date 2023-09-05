package com.example.testspring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class HelloController {
    @GetMapping(value = "/hello")

//    public ModelAndView homePage() {
//        // map url vào 1 hàm trả về tên file view
//        ModelAndView mav = new ModelAndView("hi.html");
//
//        return mav;
//
//    }
    public String hi(){
        return "hi.html";
    }
}
