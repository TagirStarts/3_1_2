package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/admin")
    public String showAdminPage() {
        return "all_users";
    }

    @GetMapping("/user")
    public String showUserPage() {
        return "user";
    }
}
