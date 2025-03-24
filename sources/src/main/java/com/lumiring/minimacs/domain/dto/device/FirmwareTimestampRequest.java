package com.lumiring.minimacs.domain.dto.device;


import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FirmwareTimestampRequest {

    @NotNull(message = "unixtime is required field and must not be null")
    private Long unixtime;

    @Size(max = 255, message = "deviceModel must contain 255 characters maximum")
    @NotNull(message = "deviceModel is required field and must not be null")
    @NotBlank(message = "deviceModel must not be empty")
    private String deviceModel;

    @Size(max = 255, message = "deviceSerial must contain 255 characters maximum")
    @NotNull(message = "deviceSerial is required field and must not be null")
    @NotBlank(message = "deviceSerial must not be empty")
    private String deviceSerial;

    @Size(max = 255, message = "firmwareVersion must contain 255 characters maximum")
    @NotNull(message = "firmwareVersion is required field and must not be null")
    @NotBlank(message = "firmwareVersion must not be empty")
    private String firmwareVersion;

    private JsonNode testResult;
}
