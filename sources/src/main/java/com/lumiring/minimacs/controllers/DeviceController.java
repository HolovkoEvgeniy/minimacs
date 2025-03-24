package com.lumiring.minimacs.controllers;


import com.lumiring.minimacs.domain.dto.device.ControlPointRequest;
import com.lumiring.minimacs.domain.dto.device.CreateDeviceRequest;
import com.lumiring.minimacs.domain.response.Response;
//import com.lumiring.backCore.kafka.KafkaProducerService;
import com.lumiring.minimacs.dto.device.DeviceDto;
import com.lumiring.minimacs.security.UserDetailsImpl;
import com.lumiring.minimacs.services.device.ControlPointService;
import com.lumiring.minimacs.services.device.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("api/v1/devices")
public class DeviceController {
    final public DeviceService deviceService;
    final public ControlPointService controlPointService;

//    final public KafkaProducerService kafkaProducerService;

//    @GetMapping("/ping")
//    public ResponseEntity<Response> getMyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {

//        Command command = Command.builder()
//                .command("my command")
//                .params("my params")
//                .build();
//
//        kafkaProducerService.sendCommand("topic-one", command);
//        return deviceService.ping(userDetails.userEntity().getId());
//    }




    @GetMapping("")
    public ResponseEntity<List<DeviceDto>> getDevices(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size) {

        int maxSize = 100;
        size = Math.min(size, maxSize);
        boolean isAdmin = userDetails.userEntity().getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

        return deviceService.getDevices(userDetails.userEntity().getId(), isAdmin, page, size);
    }




    @PostMapping("/createDevice")
    public ResponseEntity<Response> createDevice(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @Valid @RequestBody CreateDeviceRequest request) {
        boolean isAdmin = userDetails.userEntity().isAdmin();
        return deviceService.createDevice(userDetails.userEntity().getId(), request, isAdmin);
    }

    @PostMapping("/massCreateDevice")
    public ResponseEntity<Response> massCreateDevice(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @Valid @RequestBody List<CreateDeviceRequest> requests) {
        boolean isAdmin = userDetails.userEntity().isAdmin();
        return deviceService.massCreateDevices(userDetails.userEntity().getId(), requests, isAdmin);
    }

    @PostMapping("/createControlPoint")
    public ResponseEntity<Response> createControlPoint(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @Valid @RequestBody ControlPointRequest request) {
        boolean isAdmin = userDetails.userEntity().isAdmin();
        return controlPointService.createControlPoint(userDetails.userEntity().getId(), request, isAdmin);
    }

    @PostMapping("/massCreateControlPoint")
    public ResponseEntity<Response> massCreateControlPoint(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @Valid @RequestBody List<ControlPointRequest> requests) {
        boolean isAdmin = userDetails.userEntity().isAdmin();
        return controlPointService.massCreateControlPoints(userDetails.userEntity().getId(), requests, isAdmin);
    }


    @DeleteMapping("/deleteDevice/{deviceIdx}")
    public ResponseEntity<Response> deleteDevice(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PathVariable Long deviceIdx) {
        boolean isAdmin = userDetails.userEntity().isAdmin();
        return deviceService.deleteDevice(userDetails.userEntity().getId(), deviceIdx, isAdmin);
    }





}
