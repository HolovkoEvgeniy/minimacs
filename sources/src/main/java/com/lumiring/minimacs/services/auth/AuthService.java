package com.lumiring.minimacs.services.auth;

import com.lumiring.minimacs.dao.user.RoleRepository;
import com.lumiring.minimacs.dao.user.UserRepository;
import com.lumiring.minimacs.domain.constant.Code;
import com.lumiring.minimacs.entity.user.Role;
import com.lumiring.minimacs.entity.user.Roles;
import com.lumiring.minimacs.entity.user.UserEntity;
import com.lumiring.minimacs.domain.dto.user.auth.SingInRequest;
import com.lumiring.minimacs.domain.dto.user.auth.SingUpRequest;
import com.lumiring.minimacs.domain.dto.user.auth.SingInResponse;
import com.lumiring.minimacs.domain.dto.user.auth.SingUpResponse;
import com.lumiring.minimacs.domain.response.Response;
import com.lumiring.minimacs.domain.response.SuccessResponse;
import com.lumiring.minimacs.domain.response.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<Response> signUp(@RequestBody final SingUpRequest request) {

        // Проверка на существование пользователя с таким же именем
        if (userRepository.existsByUsername(request.getUsername())){
            throw CommonException.builder()
                    .code(Code.USERNAME_BUSY)
                    .userMessage(String.format("Username '%s' already exists", request.getUsername()))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        // Проверка на существование пользователя с таким же email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw CommonException.builder()
                    .code(Code.EMAIL_BUSY)
                    .userMessage(String.format("Email '%s' already exists", request.getEmail()))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        // Получаем роль из базы данных
        Role userRole = roleRepository.findByName(Roles.ROLE_USER.toString())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole); // Используем уже существующую роль

        // Создание пользователя
        var user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();

        // Сохраняем пользователя в базе данных
        userRepository.save(user);

        // Генерация JWT токена
        var jwt = jwtProvider.generateAccessToken(user);

        // Ответ с токеном
        return new ResponseEntity<>(SuccessResponse.builder()
                .data(SingUpResponse.builder().accessToken(jwt).build()).build(), HttpStatus.OK);
    }



    public ResponseEntity<Response> signIn(@RequestBody final SingInRequest request) {


        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));

        var user = userRepository.getByUsernameIgnoreCase(request.getLogin()).orElseThrow(() -> CommonException.builder().code(Code.USER_NOT_FOUND)
                .userMessage(String.format("Username %s not found", request.getLogin())).httpStatus(HttpStatus.NOT_FOUND).build());

        var jwt = jwtProvider.generateAccessToken(user);

        return new ResponseEntity<>(SuccessResponse.builder()
                .data(SingInResponse.builder().accessToken(jwt).build()).build(), HttpStatus.OK);
    }


}
