package com.baeldung.lss.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by tracxn-lp-175 on 20/12/17.
 */
@EnableWebSecurity
public class LssSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    public LssSecurityConfig() {
        super();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/signup",
                        "/register",
                        "/registrationConfirm*",
                        "/forgotPassword*",
                        "/resetPassword*",
                        "/changePassword*",
                        "/savePassword*").permitAll()
                .anyRequest().authenticated()
            .and().formLogin()
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/doLogin")
            .and().logout().permitAll().logoutRequestMatcher(new AntPathRequestMatcher("/doLogout", "GET"))
            .and().httpBasic();
    }
}
