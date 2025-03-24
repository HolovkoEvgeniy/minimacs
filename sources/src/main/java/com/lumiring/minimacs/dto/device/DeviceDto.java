package com.lumiring.minimacs.dto.device;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lumiring.minimacs.dto.user.UserDto;
import com.lumiring.minimacs.utils.UnixTimestampDeserializer;
import com.lumiring.minimacs.utils.UnixTimestampSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DeviceDto {
    private Long id;
    private Long deviceIdx;
    private String location;
    private String address;
//    @JsonSerialize(using = UnixTimestampSerializer.class)
//    @JsonDeserialize(using = UnixTimestampDeserializer.class)
//    private LocalDateTime createTime;
//
//    @JsonSerialize(using = UnixTimestampSerializer.class)
//    @JsonDeserialize(using = UnixTimestampDeserializer.class)
//    private LocalDateTime lastUpdated;

//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonSerialize(using = UnixTimestampSerializer.class)
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private LocalDateTime lastHeartbeat;
    private List<ControlPointDto> controlPoints;
    private UserDto user;
}
