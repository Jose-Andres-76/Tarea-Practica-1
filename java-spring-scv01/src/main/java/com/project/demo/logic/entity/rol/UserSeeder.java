package com.project.demo.logic.entity.rol;

import com.project.demo.logic.entity.user.User;

import java.util.Optional;

import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Order(3)
@Component
public class UserSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createDefaultUser();
    }

    private void createDefaultUser() {
        User simpleUser = new User();
        simpleUser.setName("User");
        simpleUser.setLastname("Default");
        simpleUser.setEmail("simple.user@gmail.com");
        simpleUser.setPassword("simpleuser123");

        Optional<Role> optionalSimpleRole = roleRepository.findByName(RoleEnum.USER);
        Optional<User> optionalSimpleUser = userRepository.findByEmail(simpleUser.getEmail());

        if (optionalSimpleRole.isEmpty() || optionalSimpleUser.isPresent()) {
            return;
        }

        var newUser = new User();
        newUser.setName(simpleUser.getName());
        newUser.setLastname(simpleUser.getLastname());
        newUser.setEmail(simpleUser.getEmail());
        newUser.setPassword(passwordEncoder.encode(simpleUser.getPassword()));
        newUser.setRole(optionalSimpleRole.get());

        userRepository.save(newUser);
    }
}
