package com.lumiring.minimacs.mapper;

import com.lumiring.minimacs.dto.device.DeviceDto;
import com.lumiring.minimacs.entity.device.DeviceEntity;

import java.util.stream.Collectors;

public class DeviceMapper {
    public static DeviceDto toDto(DeviceEntity device) {
        return DeviceDto.builder()
                .id(device.getId())
                .deviceIdx(device.getDeviceIdx())
                .location(device.getLocation())
//                .address(device.getAddress())
//                .createTime(device.getCreateTime())
//                .lastUpdated(device.getLastUpdated())
                .lastHeartbeat(device.getLastHeartbeat())
                .controlPoints(device.getControlPoints() != null ? device.getControlPoints().stream()
                        .map(ControlPointMapper::toDto)
                        .collect(Collectors.toList()) : null)  // если контрольные точки есть, мапим их
                .user(UserMapper.toDto(device.getUser()))  // маппим объект User в UserDto
                .build();
    }
}

