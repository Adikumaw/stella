package com.nothing.stella.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TestController {
    @GetMapping("/testseller")
    public String testSeller() {
        return "seller passed ...";
    }

    @GetMapping("/testbuyer")
    public String testBuyer() {
        return "buyer passed ...";
    }

}
