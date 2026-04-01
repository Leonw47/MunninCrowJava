package br.com.munnincrow.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class PushNotificationService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${firebase.server.key}")
    private String firebaseKey;

    public void enviar(String token, String titulo, String corpo) {
        Map<String, Object> payload = Map.of(
                "to", token,
                "notification", Map.of(
                        "title", titulo,
                        "body", corpo
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "key=" + firebaseKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        restTemplate.postForObject(
                "https://fcm.googleapis.com/fcm/send",
                request,
                String.class
        );
    }
}