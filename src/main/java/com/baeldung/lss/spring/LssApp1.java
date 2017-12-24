package com.baeldung.lss.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import com.baeldung.lss.model.User;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.baeldung.lss")
@EnableJpaRepositories("com.baeldung.lss")
@EntityScan("com.baeldung.lss.model")
public class LssApp1 {

//    @Bean
//    public UserRepository userRepository() {
//        return new InMemoryUserRepository();
//    }

//    @Bean
//    public Converter<String, User> messageConverter() {
//        return new Converter<String, User>() {
//            @Override
//            public User convert(String id) {
//                return userRepository().findUser(Long.valueOf(id));
//            }
//        };
//    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Class[] {LssSecurityConfig.class, LssApp1.class}, args);
    }

}
