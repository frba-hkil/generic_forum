package org.forumdemo.ftest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuxController {
    @GetMapping
    public String getPosts() {
        return "redirect:/api/v1/posts";
    }
}
