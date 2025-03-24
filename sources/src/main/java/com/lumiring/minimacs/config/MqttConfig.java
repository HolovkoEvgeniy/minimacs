package com.lumiring.minimacs.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.messaging.*;

@Configuration
public class MqttConfig {

    private static final String BROKER_URL = "tcp://mqtt.alldoors.online";  // MQTT-брокер
    private static final String CLIENT_ID = "my-spring-boot-app";              // Уникальный ID клиента
    private static final String USERNAME = "unimacs";                                 // Логин (если нужен)
    private static final String PASSWORD = "tThc7W7DFv";                                 // Пароль (если нужен)
    private static final String DEFAULT_TOPIC = "+/+/registration/+/Events/";                    // Топик по умолчанию
    private static final int QOS = 1;                                          // QoS (0, 1, 2)


    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    @Value("${mqtt.client.id}")
    private String clientId;

    @Value("${mqtt.credentials.username}")
    private String username;

    @Value("${mqtt.credentials.password}")
    private String password;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{brokerUrl});
        options.setUserName(username);  // Устанавливаем логин
        options.setPassword(password.toCharArray());  // Устанавливаем пароль
        options.setCleanSession(true);
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter mqttAdapter(MqttPahoClientFactory factory) {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId, factory, "#");  // Подписка на все топики
        adapter.setCompletionTimeout(5000);
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());  // Канал, куда будут приходить сообщения
        return adapter;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

//    @Bean
//    @ServiceActivator(inputChannel = "mqttInputChannel")
//    public MessageHandler mqttMessageHandler() {
//        return message -> {
//            System.out.println("Received MQTT message: " + message.getPayload());
//        };
//    }
}


