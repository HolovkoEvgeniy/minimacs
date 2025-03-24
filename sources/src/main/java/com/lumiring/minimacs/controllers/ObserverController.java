package com.lumiring.minimacs.controllers;

import com.lumiring.minimacs.domain.dto.common.CommonPageResponse;
import com.lumiring.minimacs.domain.dto.raidAlertRemote.*;

import com.lumiring.minimacs.domain.response.exception.CommonException;

import com.lumiring.minimacs.services.device.DeviceService;
import com.lumiring.minimacs.services.remoteDevice.RemoteRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("api/v1/observer")
public class ObserverController {
    private final RemoteRequestService remoteRequestService;
    final public DeviceService deviceService;

    @Value("${alert-service.token}")
    private String token;

//    @GetMapping("state-list")
//    public ResponseEntity<CPStateList> getConfig() {
//        CPStateList list = remoteRequestService.getCPStateList();
//        controlPointService.massUpdateControlPoints(list);
//        return ResponseEntity.ok(list);
//    }

    @GetMapping("shelter-list")
    public ResponseEntity<CommonPageResponse<CityResponse>> getStateList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size) {

        int maxSize = 100;
        size = Math.min(size, maxSize);

        return ResponseEntity.ok(deviceService.preformCityRequest(page, size));
    }

    @GetMapping("active-alerts")
    public ResponseEntity<ActiveAlerts> getActiveAlerts(){
        ActiveAlerts list = remoteRequestService.getActiveAlerts(token);
        return ResponseEntity.ok(list);
    }

    @GetMapping("region-state/{uid}")
    public ResponseEntity<AlarmStateResponse> getRegionAlertState(@PathVariable String uid) {
        try {
            AlarmState state = remoteRequestService.getRegionAlertState(uid, token);
            return ResponseEntity.ok(new AlarmStateResponse(state));
        } catch (RuntimeException ex){
            log.error("Error: " + ex.getMessage());
            throw CommonException.builder()
                    .userMessage("Redirect error")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

}
