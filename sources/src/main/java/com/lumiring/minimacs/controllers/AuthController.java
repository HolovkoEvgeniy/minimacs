package com.lumiring.minimacs.controllers;

import com.lumiring.minimacs.domain.dto.user.auth.SingInRequest;
import com.lumiring.minimacs.domain.dto.user.auth.SingUpRequest;
import com.lumiring.minimacs.domain.response.Response;
import com.lumiring.minimacs.services.auth.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("singUp")
    public ResponseEntity<Response> singUp(@Valid @RequestBody final SingUpRequest req) {

        return authService.signUp(req);
    }

    @PostMapping("singIn")
    public ResponseEntity<Response> singIn(@Valid @RequestBody final SingInRequest req) {

        return authService.signIn(req);
    }


}
