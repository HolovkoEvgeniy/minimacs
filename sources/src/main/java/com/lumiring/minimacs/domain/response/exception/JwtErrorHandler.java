package com.lumiring.minimacs.domain.response.exception;

import com.lumiring.minimacs.domain.constant.Code;
import com.lumiring.minimacs.domain.response.error.DomainError;
import com.lumiring.minimacs.domain.response.error.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
public class JwtErrorHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        log.error("ExpiredJwtException: {}", ex.toString());
        return new ResponseEntity<>(ErrorResponse.builder().error(DomainError.builder().code(Code.JWT_EXPIRED).message("jwt expired").build()).build(), BAD_REQUEST);
    }
}
