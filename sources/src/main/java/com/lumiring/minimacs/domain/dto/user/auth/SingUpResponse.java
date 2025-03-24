package com.lumiring.minimacs.domain.dto.user.auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SingUpResponse {
    private String accessToken;
}
