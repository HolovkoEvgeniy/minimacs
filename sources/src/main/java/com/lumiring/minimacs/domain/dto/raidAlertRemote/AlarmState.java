package com.lumiring.minimacs.domain.dto.raidAlertRemote;

import com.fasterxml.jackson.annotation.JsonValue;



public enum AlarmState {
    ACTIVE_ALARM("A"),
    PARTIAL_ALARM("P"),
    NO_ALARM("N");

    private final String code;

    AlarmState(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return name(); // Возвращает имя енума (ACTIVE_ALARM, PARTIAL_ALARM и т. д.)
    }

    public static AlarmState fromCode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("AlarmState code is null");
        }
        code = code.trim().replace("\"", ""); // Убираем кавычки и пробелы

        for (AlarmState state : values()) {
            if (state.code.equals(code)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown AlarmState code: " + code);
    }
}



