package com.lumiring.minimacs.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum JwtErrorCode {
    OK("Ok"),
    EXPIRED ("Jwt expired"),
    UNSUPPORTED ("Jwt malformed"),
    MALFORMED ("Unsupported Jwt"),
    INVALID_SIGNATURE ("Invalid signature"),
    INVALID_TOKEN ("Invalid token");

    @Getter
    private final String vale;


}
