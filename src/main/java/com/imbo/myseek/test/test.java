package com.imbo.myseek.test;

import com.imbo.myseek.login.service.UserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * Created by wcg on 16/2/20.
 */
@RestController
@RequestMapping("/api")
public class test {

    @Inject
    private UserService userService;

    @RequestMapping("/test")
    public void test(){
        System.out.println("test");
    }
}
