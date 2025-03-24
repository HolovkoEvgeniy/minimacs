package com.lumiring.minimacs.services.mqtt;

import com.lumiring.minimacs.dao.device.DeviceRepository;
import com.lumiring.minimacs.entity.device.DeviceEntity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MqttService {
    private final DeviceRepository deviceRepository;
    private final MqttPahoMessageDrivenChannelAdapter mqttAdapter;

    @PostConstruct
    public void init() {
        refreshSubscriptions();
    }

    public void refreshSubscriptions() {
        List<String> topics = deviceRepository.findAll().stream()
                .map(device -> device.getLocation() + "/registration/" + device.getDeviceIdx() + "/#")
                .toList();

        mqttAdapter.removeTopic("#");
        topics.forEach(topic -> mqttAdapter.addTopic(topic, 1));
    }

    public void deviceSubscribe(DeviceEntity device) {
        String newTopic = device.getLocation() + "/registration/" + device.getDeviceIdx() + "/#";
        mqttAdapter.addTopic(newTopic, 1);
    }
}
