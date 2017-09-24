package com.baeldung.lss.service;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by tracxn-lp-175 on 24/9/17.
 */
@Service
public class RunAsService {

    @Secured({"ROLE_RUN_AS_REPORTER"})
    public Authentication getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
