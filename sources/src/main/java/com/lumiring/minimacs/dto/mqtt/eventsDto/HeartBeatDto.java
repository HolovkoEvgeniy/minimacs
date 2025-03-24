package com.lumiring.minimacs.dto.mqtt.eventsDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class HeartBeatDto {
    private String operator;
    private String session_id;
    private Double message_id;

    private static class Info {

    }
}
