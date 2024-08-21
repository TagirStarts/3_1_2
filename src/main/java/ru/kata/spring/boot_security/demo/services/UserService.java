package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    void saveUser(User user, List<String> roleNames);
    List<User> getAllUsers();
    Optional<User> getUser(long id);
    void deleteUser(long id);
}
