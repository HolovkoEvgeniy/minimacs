package com.lumiring.minimacs.config;

import com.lumiring.minimacs.dao.user.RoleRepository;
import com.lumiring.minimacs.entity.user.Role;
import com.lumiring.minimacs.entity.user.Roles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        initializeRole(Roles.ROLE_ADMIN.toString(), "Administrator");
        initializeRole(Roles.ROLE_USER.toString(), "Common user");
        initializeRole(Roles.ROLE_OBSERVER.toString(), "Observer only user");
    }

    private void initializeRole(String roleName, String description) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            Role role = new Role();
            role.setName(roleName);
            role.setDescription(description);
            roleRepository.save(role);
            log.info("Создана роль: {}", roleName);
        }
    }
}

