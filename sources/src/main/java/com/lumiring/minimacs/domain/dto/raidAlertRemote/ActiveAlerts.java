package com.lumiring.minimacs.domain.dto.raidAlertRemote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)  // Чтобы избежать ошибок при лишних полях
public class ActiveAlerts {
    private List<Alert> alerts;

    @Data
    public static class Alert {
        private Long id;

        @JsonProperty("location_title")
        private String locationTitle;

        @JsonProperty("location_type")
        private String locationType;

        @JsonProperty("started_at")
        private Instant startedAt;

        @JsonProperty("finished_at")
        private Instant finishedAt;

        @JsonProperty("updated_at")
        private Instant updatedAt;

        @JsonProperty("alert_type")
        private String alertType;

        @JsonProperty("location_uid")
        private String locationUid;

        @JsonProperty("location_oblast")
        private String locationOblast;

        @JsonProperty("location_oblast_uid")
        private String locationOblastUid;

        @JsonProperty("location_raion")
        private String locationRaion;

        private String notes;
        private boolean calculated;
    }
}

