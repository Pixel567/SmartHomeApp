package com.example.SmartHomeApp.services;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class ClientService {
    private final WebClient webClient;

    public ClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void postLock(Boolean doorOpened, @RequestParam String JSESSIONID){
        Map<String,Boolean> data = new HashMap<>();
        data.put("doorOpened",doorOpened);
        webClient.post()
                .uri("/user/lock")
                 .bodyValue(data)
                .cookie("JSESSIONID", JSESSIONID)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public void postLight(Integer light, @RequestParam String JSESSIONID){
        Map<String,Integer> data = new HashMap<>();
        data.put("light",light);
        webClient.post()
                .uri("/user/light")
                .bodyValue(data)
                .cookie("JSESSIONID", JSESSIONID)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
