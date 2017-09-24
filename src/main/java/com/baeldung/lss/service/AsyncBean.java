package com.baeldung.lss.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by tracxn-lp-175 on 24/9/17.
 */
@Component
public class AsyncBean {

    @Async
    public void asyncCall() {
        System.out.println("Async Testing");
    }

}
