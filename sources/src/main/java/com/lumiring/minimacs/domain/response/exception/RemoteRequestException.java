package com.lumiring.minimacs.domain.response.exception;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class RemoteRequestException extends RuntimeException {
    private final String remoteMessage;
}
