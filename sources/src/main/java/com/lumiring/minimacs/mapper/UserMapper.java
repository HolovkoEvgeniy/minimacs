package com.lumiring.minimacs.mapper;

import com.lumiring.minimacs.dto.user.UserDto;
import com.lumiring.minimacs.entity.user.UserEntity;
import com.lumiring.minimacs.entity.user.Role;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto toDto(UserEntity user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );
    }

}