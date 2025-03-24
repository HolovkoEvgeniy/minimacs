package com.lumiring.minimacs.entity.user;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum Roles {
    ROLE_ROOT("ROLE_ROOT"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER"),
    ROLE_OBSERVER("ROLE_OBSERVER");

    private final String vale;


    public static boolean isValidRole(String roleName) {
        return Arrays.stream(Roles.values())
                .anyMatch(role -> role.name().equals(roleName));
    }
}
