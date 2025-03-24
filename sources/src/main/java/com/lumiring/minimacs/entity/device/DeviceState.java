package com.lumiring.minimacs.entity.device;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DeviceState {
    STATE_CLOSED("STATE_CLOSED"),
    STATE_CREDENTIAL("STATE_CREDENTIAL"),
    STATE_OPENED("STATE_OPENED");

    private final String value;  // Переименуем поле "vale" в "value"

    public String getValue() {
        return value;
    }
}
