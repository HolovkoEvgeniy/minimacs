package com.lumiring.minimacs.domain.dto.raidAlertRemote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CPStateList {
    private Info info;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Info {
        private int switcherState;
        private List<Config> config;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Config {
        private String root_topic;
        private int currentState;
        @JsonProperty("Control_point_idx")
        private int Control_point_idx;
    }
}

