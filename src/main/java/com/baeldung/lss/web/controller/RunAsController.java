package com.baeldung.lss.web.controller;

import com.baeldung.lss.service.RunAsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by tracxn-lp-175 on 24/9/17.
 */
@Controller
@RequestMapping("/runas")
public class RunAsController {

    @Autowired
    private RunAsService runAsService;

    @Secured({"ROLE_USER", "RUN_AS_REPORTER"})
    @RequestMapping
    @ResponseBody
    public String tryRunAs() {
        return runAsService.getCurrentUser().getAuthorities().toString();
    }

}
