package com.lumiring.minimacs.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {

    public CustomMetrics(MeterRegistry meterRegistry) {
        // Пример создания своей метрики
        meterRegistry.gauge("custom_metric", 100);
    }
}
