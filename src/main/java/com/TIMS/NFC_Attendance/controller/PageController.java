package com.TIMS.NFC_Attendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/auth")
    public String auth() {
        return "forward:/auth.html";
    }

    @GetMapping("/report")
    public String report() {
        return "forward:/report.html";
    }
}
