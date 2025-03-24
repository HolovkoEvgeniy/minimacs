package com.lumiring.minimacs.domain.dto.device;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ControlPointRequest {
    @NotNull
    private Long controlPointIdx;

    @NotNull
    private Long deviceIdx;

    @JsonProperty("isShelter")
    private boolean isShelter;
}
