package com.lumiring.minimacs.services.device;

import com.lumiring.minimacs.dao.device.ControlPointRepository;
import com.lumiring.minimacs.dao.device.DeviceRepository;
import com.lumiring.minimacs.dao.user.UserRepository;
import com.lumiring.minimacs.domain.constant.Code;
import com.lumiring.minimacs.domain.dto.device.ControlPointRequest;
import com.lumiring.minimacs.domain.dto.device.IdCreatedResponse;
import com.lumiring.minimacs.domain.dto.raidAlertRemote.CPStateList;
import com.lumiring.minimacs.domain.response.Response;
import com.lumiring.minimacs.domain.response.SuccessResponse;
import com.lumiring.minimacs.domain.response.exception.CommonException;
import com.lumiring.minimacs.entity.device.ControlPointEntity;
import com.lumiring.minimacs.entity.device.DeviceEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ControlPointService {

    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final ControlPointRepository controlPointRepository;

    public ResponseEntity<Response> getControlPointByIdx(Long controlPointIdx, Long userId, boolean isAdmin) {
        ControlPointEntity controlPoint = controlPointRepository.findByControlPointIdx(controlPointIdx)
                .filter(cp -> isAdmin || cp.getDeviceEntity().getUser().getId().equals(userId)) // Админ видит все
                .orElseThrow(() -> CommonException.builder()
                        .code(Code.NOT_FOUND)
                        .userMessage(String.format("Control point not found or access denied for control point with idx: '%s'", controlPointIdx))
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build());

        return ResponseEntity.ok(SuccessResponse.builder().data(controlPoint).build());
    }

    @Transactional
    public ResponseEntity<Response> createControlPoint(Long userId, ControlPointRequest request, boolean isAdmin) {
        DeviceEntity device = deviceRepository.findByDeviceIdx(request.getDeviceIdx())
                .filter(d -> isAdmin || d.getUser().getId().equals(userId)) // Админ видит все, пользователь только свое
                .orElseThrow(() -> CommonException.builder()
                        .code(Code.NOT_FOUND)
                        .userMessage(String.format("Device not found or access denied for device with idx: '%s'", request.getDeviceIdx()))
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build());

        if (controlPointRepository.existsByControlPointIdxAndDeviceEntityDeviceIdx(
                request.getControlPointIdx(), device.getDeviceIdx())) {
            throw CommonException.builder()
                    .code(Code.ALREADY_EXISTS)
                    .userMessage(String.format("Control point with idx '%s' already exists for this device", request.getControlPointIdx()))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        ControlPointEntity controlPoint = ControlPointEntity.builder()
                .controlPointIdx(request.getControlPointIdx())
                .shelter(request.isShelter())
                .deviceEntity(device)
                .build();

        Long controlPointId = controlPointRepository.save(controlPoint).getId();

        return ResponseEntity.ok(SuccessResponse.builder()
                .data(IdCreatedResponse.builder().id(controlPointId).build())
                .build());
    }

    @Transactional
    public ResponseEntity<Response> massCreateControlPoints(Long userId, List<ControlPointRequest> requests, boolean isAdmin) {
        List<IdCreatedResponse> createdControlPoints = new ArrayList<>();

        for (ControlPointRequest request : requests) {
            DeviceEntity device = deviceRepository.findByDeviceIdx(request.getDeviceIdx())
                    .filter(d -> isAdmin || d.getUser().getId().equals(userId)) // Админ видит все, пользователь только свое
                    .orElseThrow(() -> CommonException.builder()
                            .code(Code.NOT_FOUND)
                            .userMessage(String.format("Device not found or access denied for device with idx: '%s'", request.getDeviceIdx()))
                            .httpStatus(HttpStatus.BAD_REQUEST)
                            .build());

            if (controlPointRepository.existsByControlPointIdxAndDeviceEntityDeviceIdx(
                    request.getControlPointIdx(), device.getDeviceIdx())) {
                throw CommonException.builder()
                        .code(Code.ALREADY_EXISTS)
                        .userMessage(String.format("Control point with idx '%s' already exists for this device", request.getControlPointIdx()))
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build();
            }

            ControlPointEntity controlPoint = ControlPointEntity.builder()
                    .controlPointIdx(request.getControlPointIdx())
                    .shelter(request.isShelter())
                    .deviceEntity(device)
                    .build();

            Long controlPointId = controlPointRepository.save(controlPoint).getId();
            createdControlPoints.add(IdCreatedResponse.builder().id(controlPointId).build());
        }

        return ResponseEntity.ok(SuccessResponse.builder()
                .data(createdControlPoints)
                .build());
    }

    @Transactional
    public ResponseEntity<Response> deleteControlPoint(Long userId, Long controlPointId) {
        ControlPointEntity controlPoint = controlPointRepository.findById(controlPointId)
                .filter(cp -> cp.getDeviceEntity().getUser().getId().equals(userId))
                .orElseThrow(() -> CommonException.builder()
                        .code(Code.NOT_FOUND)
                        .userMessage("Control point not found or access denied")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build());

        controlPointRepository.delete(controlPoint);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Response> updateControlPoint(Long userId, ControlPointRequest request) {
        ControlPointEntity controlPoint = controlPointRepository.findByControlPointIdx(request.getControlPointIdx())
                .filter(cp -> cp.getDeviceEntity().getUser().getId().equals(userId))
                .orElseThrow(() -> CommonException.builder()
                        .code(Code.NOT_FOUND)
                        .userMessage(String.format("Control point not found or access denied for control point with idx: '%s'", request.getControlPointIdx()))
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build());

        DeviceEntity device = deviceRepository.findById(request.getDeviceIdx())
                .orElseThrow(() -> CommonException.builder()
                        .code(Code.NOT_FOUND)
                        .userMessage(String.format("Device not found or access denied for device with id: '%s'", request.getDeviceIdx()))
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build());

        // Обновляем поля
        controlPoint.setDeviceEntity(device);
        controlPoint.setLastUpdated(LocalDateTime.now());
        controlPointRepository.save(controlPoint);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public void massUpdateControlPoints(CPStateList cpStateList) {
        if (cpStateList == null || cpStateList.getInfo() == null) {
            return;
        }

        List<CPStateList.Config> configs = cpStateList.getInfo().getConfig();
        for (CPStateList.Config config : configs) {
            Long controlPointIdx = (long) config.getControl_point_idx(); // Приводим к Long
            boolean alarmed = config.getCurrentState() != 0; // Если 0 — false, иначе true

            controlPointRepository.findByControlPointIdx(controlPointIdx)
                    .ifPresent(controlPoint -> {
                        controlPoint.setAlarmed(alarmed);
                        controlPointRepository.save(controlPoint);
                    });
        }
    }




}
