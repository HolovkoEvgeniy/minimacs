package com.lumiring.minimacs.domain.response.exception;

import com.lumiring.minimacs.domain.constant.Code;
import com.lumiring.minimacs.domain.response.error.DomainError;
import com.lumiring.minimacs.domain.response.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

import static com.lumiring.minimacs.domain.constant.Code.NOT_SUPPORTED;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class DomainErrorHandler {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> handleCommonException(CommonException ex) {
        log.error("CommonException: {}", ex.toString());
        return new ResponseEntity<>(ErrorResponse.builder().error(DomainError.builder()
                .code(ex.getCode())
                .message(ex.getUserMessage())
                .build()).build(), ex.getHttpStatus());
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorResponse> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
        log.error("InternalAuthenticationServiceException: {}", ex.getCause().toString());
        return new ResponseEntity<>(ErrorResponse.builder().error(DomainError.builder().code(Code.BAD_CREDENTIALS).message("Bad credentials").build()).build(), BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedErrorException(Exception ex) {
        log.error("Exception: {}", ex.toString());
        return new ResponseEntity<>(ErrorResponse.builder().error(DomainError.builder().code(Code.INTERNAL_SERVER_ERROR).message("Inner service error").build()).build(), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        log.error("MissingRequestHeaderException: {}", ex.toString());
        return new ResponseEntity<>(ErrorResponse.builder().error(DomainError.builder().code(Code.MISSING_REQUEST_HEADER).message(ex.getMessage()).build()).build(), BAD_REQUEST);
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        log.error("NoResourceFoundException: {}", ex.toString());
        return new ResponseEntity<>(ErrorResponse.builder().error(DomainError.builder().code(NOT_SUPPORTED).message(ex.getMessage()).build()).build(), NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("BadCredentialsException: {}", ex.toString());
        return new ResponseEntity<>(ErrorResponse.builder().error(DomainError.builder().code(Code.BAD_CREDENTIALS).message(ex.getMessage()).build()).build(), BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error("HttpRequestMethodNotSupportedException: {}", ex.toString());
        return new ResponseEntity<>(ErrorResponse.builder().error(DomainError.builder().code(Code.METHOD_NOT_ALLOWED).message(ex.getMessage()).build()).build(), METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(ErrorResponse.builder().error(DomainError.builder().errors(errors).code(Code.BAD_CREDENTIALS).message("Validation failed").build()).build(), BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException: {}", ex.toString());
        return new ResponseEntity<>(ErrorResponse.builder().error(DomainError.builder().code(Code.JSON_NOT_READABLE).message(ex.getMessage()).build()).build(), BAD_REQUEST);
    }


}
