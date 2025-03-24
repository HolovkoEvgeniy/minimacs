package com.lumiring.minimacs.domain.dto.device;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateDeviceRequest {
    @NotNull
    private Long deviceIdx;
    @NotNull
    private String location;

    private String address;

    private Long userId;
}
