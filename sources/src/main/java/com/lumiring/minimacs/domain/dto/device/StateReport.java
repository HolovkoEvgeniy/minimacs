package com.lumiring.minimacs.domain.dto.device;

import com.lumiring.minimacs.entity.device.DeviceState;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class StateReport {

    private Long controlPointIdx;
    private boolean alarmed;
    private DeviceState deviceState;
    private LocalDateTime lastHeartbeat;

    public StateReport(Long controlPointIdx, boolean alarmed, DeviceState deviceState, LocalDateTime lastHeartbeat) {
        this.controlPointIdx = controlPointIdx;
        this.alarmed = alarmed;
        this.deviceState = deviceState;
        this.lastHeartbeat = lastHeartbeat;
    }
}
