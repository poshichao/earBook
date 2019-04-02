package com.android.earBook.controller;

import com.android.earBook.entity.User;
import com.android.earBook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器，用户相关接口
 * @author poshichao
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/registered")
    public void registered(@RequestBody User user) {
        userService.registered(user);
    }

    @PostMapping("/login")
    public void login(@RequestBody User user) {
        userService.login(user);
    }
}
