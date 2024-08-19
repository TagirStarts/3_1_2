package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private final UserServiceImpl userServiceImpl;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserServiceImpl userServiceImpl, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.successUserHandler = successUserHandler;
        this.userServiceImpl = userServiceImpl;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/process_login")
                .failureUrl("/login?error")
                .successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll();
    }


    @Bean
    JdbcUserDetailsManager users(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        authenticationProvider.setUserDetailsService(userServiceImpl);
        return authenticationProvider;
    }
}