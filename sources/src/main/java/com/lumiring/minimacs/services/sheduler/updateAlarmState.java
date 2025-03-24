package com.lumiring.minimacs.services.sheduler;

import com.lumiring.minimacs.domain.dto.raidAlertRemote.CPStateList;
import com.lumiring.minimacs.services.device.ControlPointService;
import com.lumiring.minimacs.services.remoteDevice.RemoteRequestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class updateAlarmState {

    private final RemoteRequestService remoteRequestService;
    private final ControlPointService controlPointService;

    @Scheduled(fixedRate = 20000)
    public void updateControlPointStates(){
        CPStateList list = remoteRequestService.getCPStateList();
        controlPointService.massUpdateControlPoints(list);
    }
}
