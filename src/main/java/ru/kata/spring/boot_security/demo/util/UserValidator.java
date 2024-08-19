package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;
import ru.kata.spring.boot_security.demo.entity.User;

@Component
public class UserValidator implements Validator {
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserValidator(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        try {
            if (user.getId() == 0 || !userServiceImpl.getUser(user.getId())
                    .get().getUsername().equals(user.getUsername())) {
                userServiceImpl.loadUserByUsername(user.getUsername());
            }
            else {
                return;
            }
        } catch (UsernameNotFoundException e) {
            return;
        }
        errors.rejectValue("username", "", String.format("Username %s already exists", user.getUsername()));
    }

    public void validate(String username, Long id, Errors errors) {
        try {
            if (id == 0 || !userServiceImpl.getUser(id)
                    .get().getUsername().equals(username)) {
                userServiceImpl.loadUserByUsername(username);
            }
            else {
                return;
            }
        } catch (UsernameNotFoundException e) {
            return;
        }
        errors.rejectValue("username", "", String.format("Username %s already exists", username));
    }
}
