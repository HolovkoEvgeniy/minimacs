package com.lumiring.minimacs.config;

import com.lumiring.minimacs.dao.user.RoleRepository;
import com.lumiring.minimacs.dao.user.UserRepository;
import com.lumiring.minimacs.entity.user.Role;
import com.lumiring.minimacs.entity.user.Roles;
import com.lumiring.minimacs.entity.user.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@Order(2)
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final String username;
    private final String password;
    private final String email;

    public AdminInitializer(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.username}") String username,
            @Value("${app.admin.password}") String password,
            @Value("${app.admin.email}") String email
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!userRepository.existsByUsername(username) && !userRepository.existsByEmail(email)) {

            Role adminRole = roleRepository.findByName(Roles.ROLE_ADMIN.toString())
                    .orElseThrow(() -> new RuntimeException("Role ROLE_ADMIN not found"));

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);

            UserEntity user = UserEntity.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .roles(roles)
                    .build();

            userRepository.save(user);
            log.info("Admin with username: {} created", username);
            log.info("Email {} created", email);
        } else {
            log.info("Admin with username: {} or with email: {} already exists", username, email);
        }
    }
}
