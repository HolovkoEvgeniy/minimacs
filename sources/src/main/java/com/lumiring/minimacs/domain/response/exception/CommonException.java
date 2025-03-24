package com.lumiring.minimacs.domain.response.exception;

import com.lumiring.minimacs.domain.constant.Code;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class CommonException extends RuntimeException {
    private final Code code;
    private final String userMessage;
    private final String techMessage;
    private final HttpStatus httpStatus;
}

