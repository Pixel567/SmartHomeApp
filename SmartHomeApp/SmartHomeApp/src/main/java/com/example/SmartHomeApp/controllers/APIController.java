package com.example.SmartHomeApp.controllers;

import com.example.SmartHomeApp.entities.Measurement;
import com.example.SmartHomeApp.entities.State;
import com.example.SmartHomeApp.entities.User;
import com.example.SmartHomeApp.repositories.MeasurementRepository;
import com.example.SmartHomeApp.repositories.UserRepository;
import com.example.SmartHomeApp.repositories.StateRepository;
import com.example.SmartHomeApp.services.ClientService;
import com.example.SmartHomeApp.services.MeasurementService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class APIController {
    private final UserRepository userRepository;
    private final StateRepository stateRepository;
    private final MeasurementService measurementService;
    private final HttpSession httpSession;
    private final ClientService clientService;

    public APIController(UserRepository userRepository, StateRepository stateRepository, MeasurementRepository measurementRepository, MeasurementService measurementService, HttpSession httpSession, WebClient webClient, ClientService clientService) {
        this.userRepository = userRepository;
        this.stateRepository = stateRepository;
        this.measurementService = measurementService;
        this.httpSession = httpSession;
        this.clientService = clientService;
    }

    @PostMapping("/device/measurements/")
    public String getMeasurement(Principal principal, @RequestBody Measurement measurement){
        measurementService.createMeasurement(principal.getName(), measurement);
        return "Measurement created.";
    }
    @PostMapping("/device/ip")
    public void setIp(Principal principal, @RequestBody String ip){
        Optional<User> device =  userRepository.findByUsername(principal.getName());
        if (device.isPresent()){
            device.get().setDeviceIp(ip);
            userRepository.save(device.get());
        }
    }

    @GetMapping("/user/main/measurements/latest")
    public Map getNewestMeasurement(Principal principal){
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (user.isPresent()){

            String connectedUsername = user.get().getConnectedUsername();

            Optional<Measurement> measurement = measurementService.readNewestMeasurement(connectedUsername);

            if (measurement.isEmpty()) {
                Map<String, Object> emptyMeasurement = new HashMap<>();
                emptyMeasurement.put("user_username", principal.getName());
                return emptyMeasurement;
            }

            Measurement m = measurement.get();

            Map<String, Object> returningMap = new HashMap<>();
            returningMap.put("temperature", m.getTemperature().toString());
            returningMap.put("humidity", m.getHumidity().toString());
            returningMap.put("motion", m.getMotion());
            returningMap.put("user_username", principal.getName());

            return returningMap;
        }
        return new HashMap();
    }


    @GetMapping("/user/main/state")
    public State getStatus(Principal principal){
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (user.isPresent()){
            String connectedUsername = user.get().getConnectedUsername();
            Optional<State> state = stateRepository.findByusername(connectedUsername);
            if (state.isPresent()){
                return state.get();
            }
        }
        return new State();
    }

    @PostMapping("/user/main/light")
    public void changeBrightness(Principal principal, @RequestBody Map<String, String> brightnessStatus){
        Optional<User> user = userRepository.findByUsername(principal.getName());
        String connectedUsername = new String();
        String ip = new String();
        if (user.isPresent()) {
            connectedUsername = user.get().getConnectedUsername();
        }
        Optional<User> device = userRepository.findByUsername(connectedUsername);
        if (device.isPresent()) {
            ip = device.get().getDeviceIp();
        }
        Optional<State> state = stateRepository.findByusername(connectedUsername);
        if (state.isPresent()) {
            state.get().setBrightness(Integer.valueOf(brightnessStatus.get("brightness")));
            stateRepository.save(state.get());
        }

        String JSESSIONID = httpSession.getId();
        clientService.postLight(Integer.valueOf( brightnessStatus.get("brightness")), JSESSIONID, ip);
    }

    @PostMapping("/user/main/lock")
    public void changeLock(Principal principal, @RequestBody Map<String, String> lockStatus){
        Optional<User> user = userRepository.findByUsername(principal.getName());
        String connectedUsername = new String();
        if (user.isPresent()) {
            connectedUsername = user.get().getConnectedUsername();
        }
        String ip = new String();
        Optional<User> device = userRepository.findByUsername(connectedUsername);
        if (device.isPresent()) {
            ip = device.get().getDeviceIp();
        }
        Optional<State> state = stateRepository.findByusername(connectedUsername);
        if (state.isPresent()) {
            state.get().setLockStatus(Boolean.valueOf(lockStatus.get("lockStatus")));
            stateRepository.save(state.get());
        }

        String JSESSIONID = httpSession.getId();
        clientService.postLock(Boolean.valueOf( lockStatus.get("lockStatus")), JSESSIONID, ip);
    }

    @PostMapping("/user/main/changeCode")
    public Map<String, Object> changeDoorCode(
            Principal principal,
            @RequestBody Map<String, Object> body
    ) {
        Optional<User> user = userRepository.findByUsername(principal.getName());
        String connectedUsername = new String();
        String ip = new String();
        if (user.isPresent()) {
            connectedUsername = user.get().getConnectedUsername();
        }
        String newCode = String.join("", (Iterable<String>) body.get("code"));

        if (user.isEmpty()) {
            return Map.of("status", "error", "msg", "User not found");
        }
        Optional<User> device = userRepository.findByUsername(connectedUsername);
        if (device.isPresent()) {
            ip = device.get().getDeviceIp();
        }

        User u = user.get();
        u.setDoorCode(newCode);
        userRepository.save(u);

        String JSESSIONID = httpSession.getId();
        Map<String, Object> piResponse = clientService.postCode(newCode, JSESSIONID, ip);

        if (!"ok".equals(piResponse.get("status"))) {
            return Map.of(
                    "status", "error",
                    "msg", piResponse.get("msg")
            );
        }

        return Map.of(
                "status", "ok",
                "msg", "code updated",
                "newCode", newCode
        );
    }

    @PostMapping("/device/doorStatus")
    public Map<String, String> updateDoorStatus(
            Principal principal,
            @RequestBody Map<String, Object> body
    ) {
        String username = principal.getName();

        Optional<State> stateOpt = stateRepository.findByusername(username);
        if (stateOpt.isEmpty()) {
            return Map.of("status", "error", "msg", "state not found");
        }

        State state = stateOpt.get();

        boolean doorOpened = Boolean.parseBoolean(body.get("doorOpened").toString());

        state.setLockStatus(doorOpened);
        stateRepository.save(state);

        return Map.of("status", "ok");
    }

    @PostMapping("/user/main/test")
    public Map<String, Object> testDevice(Principal principal) {
        Optional<User> user = userRepository.findByUsername(principal.getName());
        String connectedUsername = new String();
        String ip = new String();
        if (user.isPresent()) {
            connectedUsername = user.get().getConnectedUsername();
        }
        Optional<User> device = userRepository.findByUsername(connectedUsername);
        if (device.isPresent()) {
            ip = device.get().getDeviceIp();
        }

        String JSESSIONID = httpSession.getId();
        Map<String, Object> result = clientService.runDeviceTest(JSESSIONID, ip);

        return Map.of(
                "status", "ok",
                "deviceTest", result
        );
    }

    @PostMapping("/device/init")
    public Map<String, Object> deviceInit(Principal principal) {

        String deviceName = principal.getName(); // device1

        Optional<User> deviceUser = userRepository.findByUsername(deviceName);
        if (deviceUser.isEmpty()) {
            return Map.of("status", "error", "msg", "device not found");
        }

        String connectedUsername = deviceUser.get().getConnectedUsername();
        if (connectedUsername == null) {
            return Map.of("status", "error", "msg", "device not linked to user");
        }

        Optional<User> linkedUser = userRepository.findByUsername(connectedUsername);
        String doorCode = linkedUser.map(User::getDoorCode).orElse("");

        Optional<State> stateOpt = stateRepository.findByusername(deviceName);
        if (stateOpt.isEmpty()) {
            return Map.of("status", "error", "msg", "state missing");
        }

        State s = stateOpt.get();

        return Map.of(
                "status", "ok",
                "brightness", s.getBrightness(),
                "lockStatus", s.getLockStatus(),
                "doorCode", doorCode
        );
    }

    @GetMapping("/user/main/doorCode")
    public Map<String, Object> getDoorCode(Principal principal) {
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (user.isEmpty()) {
            return Map.of("status", "error", "doorCode", "");
        }

        return Map.of(
                "status", "ok",
                "doorCode", user.get().getDoorCode() == null ? "" : user.get().getDoorCode()
        );
    }


}