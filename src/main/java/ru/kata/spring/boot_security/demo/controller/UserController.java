package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.entity.User;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping()
    public User adminInfo(@AuthenticationPrincipal User user) {
        return user;
    }
}