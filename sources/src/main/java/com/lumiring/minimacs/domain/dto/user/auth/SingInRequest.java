package com.lumiring.minimacs.domain.dto.user.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SingInRequest {

    @NotBlank(message = "login должен быть заполнен")
    private String login;

    @NotBlank(message = "password должен быть заполнен")
    private String password;

}
