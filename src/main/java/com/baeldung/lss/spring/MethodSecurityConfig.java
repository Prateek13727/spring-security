package com.baeldung.lss.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.access.intercept.RunAsManagerImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * Created by tracxn-lp-175 on 24/9/17.
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    @Override
    protected RunAsManager runAsManager() {
        RunAsManagerImpl runAsManager = new RunAsManagerImpl();
        runAsManager.setKey("MyRunAsKey");
        return runAsManager;
    }
}
