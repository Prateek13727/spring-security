package com.baeldung.lss.spring;

import com.baeldung.lss.security.CustomAuthenticationProvider;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.sql.DataSource;

/**
 * Created by tracxn-lp-175 on 20/12/17.
 */
@EnableWebSecurity
public class LssSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    SessionRegistry sessionRegistry;

    @Autowired
    DataSource dataSource;

//    @Autowired
//    private CustomAuthenticationProvider customAuthenticationProvider;

    public LssSecurityConfig() {
        super();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

//        auth.jdbcAuthentication().dataSource(dataSource).
//        withUser("user").password("pass").roles("USER").and().
//        withUser("admin").password("pass").roles("ADMIN");


//        auth.jdbcAuthentication().dataSource(dataSource).withDefaultSchema().
//                withUser("user").password("pass").roles("USER").and().
//                withUser("admin").password("pass").roles("ADMIN");

//        //create a user-service by default
//        auth.inMemoryAuthentication().withUser("user").password("pass").roles("USER").and().
//                withUser("admin").password("pass").roles("ADMIN");

        // default implementation
        auth.userDetailsService(userDetailsService);

        // configuring multiple providers
//        final DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
//        auth.authenticationProvider(customAuthenticationProvider).authenticationProvider(daoAuthenticationProvider);

        //configuring custom provider manager not a new class but a new instance of the provider manager
//        auth.parentAuthenticationManager(new ProviderManager(Lists.newArrayList(customAuthenticationProvider)));
//        auth.eraseCredentials(false).userDetailsService(userDetailsService);
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
            .and()
                .rememberMe()
                .key("lssAppKey")
                .tokenValiditySeconds(604800) // 1 week = 604800
                .tokenRepository(persistentTokenRepository())
            .and().logout().permitAll().logoutRequestMatcher(new AntPathRequestMatcher("/doLogout", "GET"))
            .and().httpBasic()
            .and().sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry())
            .and().sessionFixation().none();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        final JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
