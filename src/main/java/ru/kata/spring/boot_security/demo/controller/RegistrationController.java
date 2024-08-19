package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RegistrationController {

    private final UserServiceImpl userServiceImpl;
    private final UserValidator userValidator;
    private final RoleServiceImpl roleServiceImpl;

    @Autowired
    public RegistrationController(UserServiceImpl userServiceImpl, UserValidator userValidator, RoleServiceImpl roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.userValidator = userValidator;
        this.roleServiceImpl = roleServiceImpl;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleServiceImpl.getAllRoles());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @RequestParam("role")List <String> roleNames) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "register";
        }

        userServiceImpl.saveUser(user, roleNames);
        return "redirect:/login";
    }
}

