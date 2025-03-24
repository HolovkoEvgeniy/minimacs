package com.lumiring.minimacs.domain.dto.user;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfoRequest {

    @Size(max = 255, message = "Company must contain 100 characters maximum")
    private String company;


}
