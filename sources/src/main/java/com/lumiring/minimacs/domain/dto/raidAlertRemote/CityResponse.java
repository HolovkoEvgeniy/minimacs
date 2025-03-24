package com.lumiring.minimacs.domain.dto.raidAlertRemote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)  // Чтобы избежать ошибок при лишних полях
public class CityResponse {
    private boolean alarmed;
    private Long lastHeartbeat;
    private Long controlPointIdx;
//    private String address;
}
