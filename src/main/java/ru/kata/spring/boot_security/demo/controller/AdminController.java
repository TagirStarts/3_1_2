package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImpl userServiceImpl;
    private final RoleServiceImpl roleServiceImpl;
    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, UserValidator userValidator, RoleServiceImpl roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
        this.userValidator = userValidator;
    }

    @GetMapping
    public String allUsers(Principal principal, Model model) {
        List<User> allUsers = userServiceImpl.getAllUsers();
        model.addAttribute("allUsers", allUsers);
        User user = userServiceImpl.findByUsername(principal.getName());
        model.addAttribute("adminUser", user);
        List<Role> roles = roleServiceImpl.getAllRoles();
        model.addAttribute("roles", roles);
        return "all_users";
    }

    @GetMapping("/addNewUser")
    public String addNewUser(Principal principal, Model model) {
        User admin = userServiceImpl.findByUsername(principal.getName());
        model.addAttribute("adminUser", admin);
        User user = new User();
        model.addAttribute("user", user);
        List<Role> roles = roleServiceImpl.getAllRoles();
        model.addAttribute("roles", roles);
        return "add-new-user";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @RequestParam("role") List <String> roleNames, Model model, Principal principal) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            User admin = userServiceImpl.findByUsername(principal.getName());
            model.addAttribute("adminUser", admin);
            List<Role> roles = roleServiceImpl.getAllRoles();
            model.addAttribute("roles", roles);
            return "add-new-user";
        }
        userServiceImpl.saveUser(user, roleNames);
        return "redirect:/admin";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") long id) {
        userServiceImpl.deleteUser(id);
        return "redirect:/admin";
    }
}
