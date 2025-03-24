package com.lumiring.minimacs.services.mqtt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MqttListener {

    private final HeartbeatService heartbeatService;

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMqttMessage(Message<String> message) {
        String payload = message.getPayload();
        String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
        Long deviceIdx = extractDeviceIdxFromTopic(topic);

        if (isErrorNoCodeZero(payload)) {
            heartbeatService.updateHeartbeat(deviceIdx);
        } else {
            log.warn("Device {} did not confirm successful operation, heartbeat is not updated. Payload: {}", deviceIdx, payload);
        }
    }


    private Long extractDeviceIdxFromTopic(String topic) {
        String marker = "registration/";
        int startIndex = topic.indexOf(marker);

        if (startIndex != -1) {
            int idStart = startIndex + marker.length();
            int idEnd = topic.indexOf("/", idStart);
            if (idEnd == -1) idEnd = topic.length(); // Если ID в конце строки

            String deviceIdStr = topic.substring(idStart, idEnd);
            try {
                return Long.parseLong(deviceIdStr);
            } catch (NumberFormatException e) {
                System.err.println("Error: Failed to parse device ID from topic: " + topic);
            }
        }
        return null; // Вернем null, если ID не найден
    }

    private boolean isErrorNoCodeZero(String payload) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(payload);
            int errorNo = rootNode.path("result").path("errorNo").asInt();
            return errorNo == 0;
        } catch (Exception e) {
            log.error("Error parsing JSON: {}", e.getMessage(), e);
            return false; // В случае ошибки считаем, что errorNo != 0
        }
    }
}

