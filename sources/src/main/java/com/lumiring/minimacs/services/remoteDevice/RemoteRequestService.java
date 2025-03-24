package com.lumiring.minimacs.services.remoteDevice;

import com.lumiring.minimacs.dao.device.DeviceRepository;
import com.lumiring.minimacs.dao.user.UserRepository;
import com.lumiring.minimacs.domain.dto.raidAlertRemote.ActiveAlerts;
import com.lumiring.minimacs.domain.dto.raidAlertRemote.AlarmState;

import com.lumiring.minimacs.domain.dto.raidAlertRemote.CPStateList;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Mono;


@Service
@AllArgsConstructor
public class RemoteRequestService {

    private final WebClient webClient;
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;


    public CPStateList getCPStateList() {
        return fetchData("http://78.27.236.191:8080/config", CPStateList.class, null);
    }

    public ActiveAlerts getActiveAlerts(String token) {
        return fetchData("https://api.alerts.in.ua/v1/alerts/active.json", ActiveAlerts.class, token);
    }

    public AlarmState getRegionAlertState(String uid, String token) {
        String url = "https://api.alerts.in.ua/v1/iot/active_air_raid_alerts/" + uid + ".json";
        String stateString = fetchData(url, String.class, token); // Получаем строку
        return AlarmState.fromCode(stateString);                  // Преобразуем строку в AlarmState
    }


    private <T> T fetchData(String url, Class<T> responseType, @Nullable String bearerToken) {
        return webClient.get()
                .uri(url)
                .headers(headers -> {
                    if (bearerToken != null) {
                        headers.setBearerAuth(bearerToken);
                    }
                })
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException(errorBody)))
                )
                .bodyToMono(responseType)
                .block();
    }

}
