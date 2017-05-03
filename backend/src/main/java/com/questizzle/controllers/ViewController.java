package com.questizzle.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by danny.grullon on 4/9/17.
 */
@Controller
public class ViewController {

    @RequestMapping({
            "/login",
            "/register/**",
            "/registration-confirmation",
            "/portal-search",
            "/create-portal",
            "/portal/**",
            "/assessment-search/**",
            "/create-assessment/**",
            "/assessment/**",
            "/create-question/**",
            "/question/**",
            "/practice/**",
            "/print-assessment/**"
    })
    public String index() {
        return "forward:/";
    }
}
