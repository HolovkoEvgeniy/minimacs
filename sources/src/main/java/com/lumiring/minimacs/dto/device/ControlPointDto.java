package com.lumiring.minimacs.dto.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ControlPointDto {
    private Long id;
    private Long controlPointIdx;
    private String state;
    private Boolean alarmed;
    private Boolean shelter;
//    private LocalDateTime createTime;  // Дата и время создания контрольной точки
//    private LocalDateTime lastUpdated;  // Дата и время последнего обновления контрольной точки
    private Long deviceIdx;  // ID устройства, к которому привязана контрольная точка
    // Или можно использовать полный объект DeviceDto
//    private DeviceDto device;  // Полный объект устройства, если требуется
}

