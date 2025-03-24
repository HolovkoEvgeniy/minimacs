package com.lumiring.minimacs.domain.dto.raidAlertRemote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)  // Чтобы избежать ошибок при лишних полях
public class CPCreds {

    @JsonProperty("root_topic")
    private String rootTopic;

    @JsonProperty("currentState")
    private Integer currentState;

    @JsonProperty("Control_point_idx")
    private Integer controlPointIdx;
}
