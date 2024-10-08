package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleServiceImpl roleServiceImpl;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleServiceImpl roleServiceImpl) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleServiceImpl = roleServiceImpl;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s not found", username));
        }
        return user;
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    @Transactional
    public void saveUser(User user, List <String> roleNames) {
        if (user.getId() == 0 || !userRepository.findById(user.getId()).get().getPassword().equals(user.getPassword()))
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set <Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            roles.add(roleServiceImpl.findByName(roleName));
        }
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> getUser(long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void deleteUser(long id) {
        if (getUser(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }
}
