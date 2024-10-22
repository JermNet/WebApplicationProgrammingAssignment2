package com.example.assignment2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignupController {
    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/signupsuccess")
    public String signupSuccessPage() {
        return "signupsuccess";
    }

    @GetMapping("/signupfailure")
    public String signupFailurePage() {
        return "signupfailure";
    }
}
