package com.baeldung.lss.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by tracxn-lp-175 on 23/12/17.
 */
@Controller
public class PathController {

    @RequestMapping("/login")
    public String list() {
        return "loginPage";
    }
}
