package com.lumiring.minimacs.services.device;

import com.lumiring.minimacs.dao.device.ControlPointRepository;
import com.lumiring.minimacs.dao.device.DeviceRepository;
import com.lumiring.minimacs.dao.user.UserRepository;
import com.lumiring.minimacs.domain.constant.Code;
import com.lumiring.minimacs.domain.dto.common.CommonPageResponse;
import com.lumiring.minimacs.domain.dto.device.CreateDeviceRequest;
import com.lumiring.minimacs.domain.dto.device.IdCreatedResponse;
import com.lumiring.minimacs.domain.dto.raidAlertRemote.CityResponse;
import com.lumiring.minimacs.domain.response.Response;
import com.lumiring.minimacs.domain.response.SuccessResponse;
import com.lumiring.minimacs.domain.response.exception.CommonException;
import com.lumiring.minimacs.dto.device.DeviceDto;
import com.lumiring.minimacs.entity.device.DeviceEntity;
import com.lumiring.minimacs.entity.user.UserEntity;
import com.lumiring.minimacs.mapper.DeviceMapper;
import com.lumiring.minimacs.services.mqtt.MqttService;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.lumiring.minimacs.entity.device.ControlPointEntity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final ControlPointRepository controlPointRepository;
    private final UserRepository userRepository;
    private final MqttService mqttService;


    @Transactional
    public ResponseEntity<Response> saveDevice(Long userId, DeviceEntity device) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> CommonException.builder().code(Code.NOT_FOUND)
                        .userMessage("User not found")
                        .httpStatus(HttpStatus.BAD_REQUEST).build());


        device.setUser(user);
        device.setCreateTime(LocalDateTime.now());
        device.setLastUpdated(LocalDateTime.now());
        Long deviceId = deviceRepository.save(device).getId();


        return new ResponseEntity<>(SuccessResponse.builder().data(IdCreatedResponse.builder()
                .id(deviceId)
                .build()).build(), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<Response> createDevice(Long userDetailsId, CreateDeviceRequest request, boolean isAdmin) {
        // Если админ и не указал userDetailsId в запросе, то создаем устройство для него самого
        Long targetUserId = (isAdmin && request.getUserId() != null) ? request.getUserId() : userDetailsId;

        UserEntity user = userRepository.findById(targetUserId)
                .orElseThrow(() -> CommonException.builder()
                        .code(Code.NOT_FOUND)
                        .userMessage(String.format("User not found with id: '%s'", targetUserId))
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build());

        DeviceEntity device = DeviceEntity.builder()
                .user(user)
                .deviceIdx(request.getDeviceIdx())
                .location(request.getLocation())
                .address(request.getAddress())
                .build();

        mqttService.deviceSubscribe(device);
        Long deviceId = deviceRepository.save(device).getId();

        return ResponseEntity.ok(SuccessResponse.builder()
                .data(IdCreatedResponse.builder().id(deviceId).build())
                .build());
    }


    @Transactional
    public ResponseEntity<Response> massCreateDevices(Long userDetailsId, List<CreateDeviceRequest> requests, boolean isAdmin) {
        List<Long> createdDeviceIds = new ArrayList<>();

        for (CreateDeviceRequest request : requests) {
            Long targetUserId = (isAdmin && request.getUserId() != null) ? request.getUserId() : userDetailsId;

            UserEntity user = userRepository.findById(targetUserId)
                    .orElseThrow(() -> CommonException.builder()
                            .code(Code.NOT_FOUND)
                            .userMessage(String.format("User not found with id: '%s'", targetUserId))
                            .httpStatus(HttpStatus.BAD_REQUEST)
                            .build());

            DeviceEntity device = DeviceEntity.builder()
                    .user(user)
                    .deviceIdx(request.getDeviceIdx())
                    .location(request.getLocation())
                    .address(request.getAddress())
                    .build();

            Long deviceId = deviceRepository.save(device).getId();
            createdDeviceIds.add(deviceId);
        }

        mqttService.refreshSubscriptions();
        return ResponseEntity.ok(SuccessResponse.builder()
                .data(Map.of("createdDeviceIds", createdDeviceIds))
                .build());
    }


    @Transactional(readOnly = true)
    public ResponseEntity<List<DeviceDto>> getDevices(Long userId, boolean isAdmin, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<DeviceEntity> devices = isAdmin
                ? deviceRepository.findAll(pageable) // Админ видит все
                : deviceRepository.findByUserId(userId, pageable); // Пользователь видит только свои

        // Преобразуем список устройств в список DTO
        List<DeviceDto> deviceDtos = devices.getContent().stream()
                .map(DeviceMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(deviceDtos);
    }


    @Transactional
    public ResponseEntity<Response> deleteDevice(Long userId, Long deviceIdx, boolean isAdmin) {
        DeviceEntity device = deviceRepository.findByDeviceIdx(deviceIdx)
                .filter(d -> isAdmin || d.getUser().getId().equals(userId)) // Админ может удалять любое, юзер только свое
                .orElseThrow(() -> CommonException.builder()
                        .code(Code.NOT_FOUND)
                        .userMessage(String.format("Device not found or access denied for device with idx: '%s'", deviceIdx))
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build());

        deviceRepository.delete(device);

        return new ResponseEntity<>(HttpStatus.OK);
    }



//    public CommonPageResponse<CityRequest> preformCityRequest(Long userId, boolean isAdmin, int page, int size) {
//        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());
//
//        Page<DeviceEntity> devicePage = isAdmin
//                ? deviceRepository.findAll(pageable)
//                : deviceRepository.findByUserId(userId, pageable);
//
//        List<CityRequest> responseList = devicePage.getContent().stream()
//                .flatMap(device -> device.getControlPoints().stream()
//                        .filter(ControlPointEntity::isShelter)
//                        .map(cp -> new CityRequest(
//                                cp.isAlarmed(),
//                                device.getLastHeartbeat() != null ? device.getLastHeartbeat().toEpochSecond(ZoneOffset.UTC) : null,
//                                cp.getControlPointIdx(),
//                                device.getAddress()
//                        )))
//                .collect(Collectors.toList());
//
//        return CommonPageResponse.<CityRequest>builder()
//                .content(responseList)
//                .totalPages(devicePage.getTotalPages())
//                .totalElements(devicePage.getTotalElements())
//                .pageSize(devicePage.getSize())
//                .currentPage(devicePage.getNumber() + 1)
//                .first(devicePage.isFirst())
//                .last(devicePage.isLast())
//                .build();
//    }

    public CommonPageResponse<CityResponse> preformCityRequest(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());

        // Получаем страницу контрольных точек, которые являются убежищами
        Page<ControlPointEntity> controlPointPage = controlPointRepository.findByShelterTrue(pageable);

        // Преобразуем данные из контрольных точек в CityRequest
        List<CityResponse> responseList = controlPointPage.getContent().stream()
                .map(cp -> new CityResponse(
                        cp.isAlarmed(),
                        cp.getDeviceEntity().getLastHeartbeat() != null
                                ? cp.getDeviceEntity().getLastHeartbeat().toEpochSecond(ZoneOffset.UTC)
                                : null,  // Если lastHeartbeat существует, конвертируем в секунды
                        cp.getControlPointIdx()
//                        cp.getDeviceEntity().getAddress()  // Адрес устройства
                ))
                .collect(Collectors.toList());

        // Формируем и возвращаем ответ с пагинацией
        return CommonPageResponse.<CityResponse>builder()
                .content(responseList)
                .totalPages(controlPointPage.getTotalPages())
                .totalElements(controlPointPage.getTotalElements())
                .pageSize(controlPointPage.getSize())
                .currentPage(controlPointPage.getNumber() + 1)
                .first(controlPointPage.isFirst())
                .last(controlPointPage.isLast())
                .build();
    }





//    @Transactional
//    public void updateIsAlarmed(String controlPointIdx) {
//        deviceRepository.updateIsAlarmed(controlPointIdx);
//    }


}
