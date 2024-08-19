package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


//@Validated
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserServiceImpl userServiceImpl;
    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, UserValidator userValidator) {
        this.userServiceImpl = userServiceImpl;
        this.userValidator = userValidator;
    }

    @GetMapping
    public ResponseEntity<List<User>> allUsers(Principal principal, Model model) {
        List<User> allUsers = userServiceImpl.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        User user = userServiceImpl.getUser(id).get();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> addNewUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }
        userServiceImpl.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }
        userServiceImpl.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable long id) {
        User user = userServiceImpl.getUser(id).get();
        userServiceImpl.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
