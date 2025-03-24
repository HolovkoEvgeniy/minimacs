package com.lumiring.minimacs.services.user;

import java.beans.FeatureDescriptor;

import com.lumiring.minimacs.dao.user.RoleRepository;
import com.lumiring.minimacs.dao.user.UserRepository;
import com.lumiring.minimacs.domain.dto.user.*;
import com.lumiring.minimacs.domain.constant.Code;
import com.lumiring.minimacs.entity.user.Role;
import com.lumiring.minimacs.entity.user.Roles;
import com.lumiring.minimacs.entity.user.UserEntity;
import com.lumiring.minimacs.domain.response.Response;
import com.lumiring.minimacs.domain.response.SuccessResponse;
import com.lumiring.minimacs.domain.response.exception.CommonException;
import com.lumiring.minimacs.services.auth.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public ResponseEntity<Response> updateUserCreds(Long userId, UpdateUserCredsRequest request) {
        UserEntity existingUserEntity = userRepository.findById(userId)

                .orElseThrow(() -> CommonException.builder().code(Code.NOT_FOUND)
                        .userMessage("User not found").httpStatus(HttpStatus.BAD_REQUEST).build());


        if (userRepository.existsByUsernameAndIdNot(request.getUsername(), userId)) {
            throw CommonException.builder()
                    .code(Code.USERNAME_BUSY)
                    .userMessage(String.format("Username '%s' already exists", request.getUsername()))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }


        if (userRepository.existsByEmailAndIdNot(request.getEmail(), userId)) {
            throw CommonException.builder()
                    .code(Code.EMAIL_BUSY)
                    .userMessage(String.format("Email '%s' already exists", request.getEmail()))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }


        if (!passwordEncoder.matches(request.getPassword(), existingUserEntity.getPassword())) {
            throw CommonException.builder().code(Code.AUTHORIZATION_ERROR).userMessage("Wrong password").httpStatus(HttpStatus.BAD_REQUEST).build();
        }


        UserEntity newUserEntity = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
        newUserEntity.setUsername(request.getUsername());
        newUserEntity.setEmail(request.getEmail());
        newUserEntity.setPassword(request.getPassword());

        BeanUtils.copyProperties(newUserEntity, existingUserEntity, getNullPropertyNames(newUserEntity));
        UserEntity returnedUserEntity = userRepository.save(existingUserEntity);

        String jwt = jwtProvider.generateAccessToken(returnedUserEntity);

        return new ResponseEntity<>(
                SuccessResponse.builder()
                        .data(
                                UpdateUserCredsResponse.builder()
                                        .email(returnedUserEntity.getEmail())
                                        .username(returnedUserEntity.getUsername())
                                        .accessToken(jwt)
                                        .build()
                        )
                        .build(),
                HttpStatus.OK
        );
    }


//    public ResponseEntity<Response> updateUserInfo(Long userId, UpdateUserInfoRequest infoRequest) {
//        User user = userDao.getUserById(userId);
//
//        if (infoRequest.getCompany() != null) {
//            user.setCompany(infoRequest.getCompany());
//        }
//
//        User returnedUser = userDao.updateUserCreds(user);
//
//        return new ResponseEntity<>(
//                SuccessResponse.builder()
//                        .data(
//                                UserResponse.builder()
//                                        .company(returnedUser.getCompany())
//                                        .build()
//                        )
//                        .build(),
//                HttpStatus.OK
//        );
//    }

    public ResponseEntity<Response> getProfile(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> CommonException.builder().code(Code.NOT_FOUND)
                        .userMessage("User not found").httpStatus(HttpStatus.BAD_REQUEST).build());


        return new ResponseEntity<>(SuccessResponse.builder().data(UserResponse.builder()
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .build()).build(), HttpStatus.OK);
    }


    public ResponseEntity<Response> getProfileByUsernameOrEmail(String username, String email) {

        UserEntity userEntity = new UserEntity();
        if (StringUtils.hasText(username)) {
            userEntity = userRepository.getByUsernameIgnoreCase(username)
                    .orElseThrow(() -> CommonException.builder().code(Code.NOT_FOUND)
                            .userMessage("User not found").httpStatus(HttpStatus.BAD_REQUEST).build());
        } else if (StringUtils.hasText(email)) {
            userEntity = userRepository.getByEmailIgnoreCase(email)
                    .orElseThrow(() -> CommonException.builder().code(Code.NOT_FOUND)
                            .userMessage("User not found").httpStatus(HttpStatus.BAD_REQUEST).build());
        }
        return new ResponseEntity<>(SuccessResponse.builder().data(UserResponse.builder()
                .email(userEntity.getEmail()).username(userEntity.getUsername()).build()).build(), HttpStatus.OK);
    }


    public ResponseEntity<Response> getUserProfileList(Integer page, Integer size) {

        // Создание объекта Pageable для пагинации
        Pageable pageable = PageRequest.of(page - 1, size); // page - 1, т.к. Spring JPA использует нумерацию с 0
        Page<UserEntity> userPage = userRepository.findAll(pageable);

        // Извлечение данных из результата
        List<UserEntity> userEntityList = userPage.getContent();
        long totalElements = userPage.getTotalElements();
        int totalPages = userPage.getTotalPages();
        boolean isLast = userPage.isLast();

        // Формирование ответа
        return new ResponseEntity<>(
                SuccessResponse.builder()
                        .data(UserPageResponse.<UserEntity>builder()
                                .content(userEntityList)
                                .page(page)
                                .size(size)
                                .totalElements(totalElements)
                                .totalPages(totalPages)
                                .last(isLast)
                                .build())
                        .build(),
                HttpStatus.OK
        );
    }


    public ResponseEntity<Response> deleteUser(Long userId) {
        userRepository.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Response> updateUserRoles(Long userId, Set<String> roleNames) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> CommonException.builder().code(Code.NOT_FOUND)
                        .userMessage("User not found").httpStatus(HttpStatus.BAD_REQUEST).build());

        // Проверяем, существуют ли переданные роли в Enum Roles
        Set<Role> newRoles = roleNames.stream()
                .map(roleName -> {
                    if (!isValidRole(roleName)) {
                        throw CommonException.builder().code(Code.NOT_FOUND)
                                .userMessage("Role not allowed: " + roleName).httpStatus(HttpStatus.BAD_REQUEST).build();
                    }
                    return roleRepository.findByName(roleName)
                            .orElseThrow(() -> CommonException.builder().code(Code.NOT_FOUND)
                                    .userMessage("Role not found: " + roleName).httpStatus(HttpStatus.BAD_REQUEST).build());
                })
                .collect(Collectors.toSet());

        user.setRoles(newRoles); // Заменяем старые роли
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private String[] getNullPropertyNames(Object source) {
        BeanWrapper src = new BeanWrapperImpl(source);
        return Arrays.stream(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(name -> {
                    src.getPropertyValue(name);
                    return false;
                })
                .toArray(String[]::new);
    }

    private boolean isValidRole(String roleName) {
        return Arrays.stream(Roles.values())
                .map(Roles::name)
                .anyMatch(enumRole -> enumRole.equals(roleName));
    }

}
