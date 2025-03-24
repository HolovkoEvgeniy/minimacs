package com.lumiring.minimacs.services.mqtt;

import com.lumiring.minimacs.dao.device.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HeartbeatService {

    private final DeviceRepository deviceRepository;

    public void updateHeartbeat(Long deviceIdx) {
        deviceRepository.updateLastHeartbeat(deviceIdx, LocalDateTime.now());
    }

}
