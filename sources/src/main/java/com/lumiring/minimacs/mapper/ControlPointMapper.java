package com.lumiring.minimacs.mapper;


import com.lumiring.minimacs.dto.device.ControlPointDto;
import com.lumiring.minimacs.entity.device.ControlPointEntity;

public class ControlPointMapper {
    public static ControlPointDto toDto(ControlPointEntity controlPoint) {
        return ControlPointDto.builder()
                .id(controlPoint.getId())
                .controlPointIdx(controlPoint.getControlPointIdx())
                .state(String.valueOf(controlPoint.getState()))
                .alarmed(controlPoint.isAlarmed())
                .shelter(controlPoint.isShelter())
//                .createTime(controlPoint.getCreateTime())
//                .lastUpdated(controlPoint.getLastUpdated())
                .deviceIdx(controlPoint.getDeviceEntity().getDeviceIdx())
                .build();
    }
}

