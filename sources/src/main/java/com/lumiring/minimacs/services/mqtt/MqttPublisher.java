package com.lumiring.minimacs.services.mqtt;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class MqttPublisher {

    private final MessageChannel mqttInputChannel;

    public MqttPublisher(@Qualifier("mqttInputChannel") MessageChannel mqttInputChannel) {
        this.mqttInputChannel = mqttInputChannel;
    }

    public void publish(String payload) {
        Message<String> message = MessageBuilder.withPayload(payload).build();
        mqttInputChannel.send(message);
    }
}

