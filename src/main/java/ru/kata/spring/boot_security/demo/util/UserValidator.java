package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.entity.User;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        try {
            if (user.getId() == 0 || !userService.getUser(user.getId())
                    .get().getUsername().equals(user.getUsername())) {
                userService.loadUserByUsername(user.getUsername());
            }
            else return;
        } catch (UsernameNotFoundException e) {
            return;
        }
        errors.rejectValue("username", "", String.format("Username %s already exists", user.getUsername()));
    }
}
