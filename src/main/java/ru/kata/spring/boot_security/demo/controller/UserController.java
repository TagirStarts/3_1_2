package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping()
    public User adminInfo(@AuthenticationPrincipal User user) {
        return user;
    }
}