package com.example.SmartHomeApp.services;
import com.mysql.cj.Session;
import org.springframework.core.ParameterizedTypeReference;
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


    public void postLock(Boolean doorClosed, @RequestParam String JSESSIONID, @RequestParam String ip){
        Map<String,Boolean> data = new HashMap<>();
        data.put("doorClosed",doorClosed);
        webClient.post()
                .uri("http://" + ip + ":5000"+"/user/lock")
                 .bodyValue(data)
                .cookie("JSESSIONID", JSESSIONID)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void postLight(Integer light, @RequestParam String JSESSIONID, @RequestParam String ip){
        Map<String,Integer> data = new HashMap<>();
        data.put("light",light);
        webClient.post()
                .uri("http://" + ip + ":5000"+"/user/light")
                .bodyValue(data)
                .cookie("JSESSIONID", JSESSIONID)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public Map<String, Object> postCode(String code, String JSESSIONID, String ip) {
        Map<String, String> data = new HashMap<>();
        data.put("code", code);

        return webClient.post()
                .uri("http://" + ip + ":5000"+"/user/code")
                .bodyValue(data)
                .cookie("JSESSIONID", JSESSIONID)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    public Map<String, Object> runDeviceTest(String JSESSIONID, String ip) {
        return webClient.post()
                .uri("http://" + ip + ":5000"+"/user/test")
                .cookie("JSESSIONID", JSESSIONID)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

}
