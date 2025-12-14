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

    @GetMapping("/user/main/measurements/latest")
    public Map getNewestMeasurement(Principal principal){
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (user.isPresent()){
            String connectedUsername = user.get().getConnectedUsername();
            Optional<Measurement> measurement = measurementService.readNewestMeasurement(connectedUsername);
            Map<String, String> returningMap = new HashMap<String, String>();
            returningMap.put("temperature", measurement.get().getTemperature().toString());
            returningMap.put("humidity", measurement.get().getHumidity().toString());
            returningMap.put("motion", measurement.get().getMotion().toString());

            returningMap.put("user_username", user.get().getUsername());
            return returningMap;
        }
        return new HashMap();
    }

    // Mapping URL to a method of an API controller.
    @GetMapping("/user/main/state")
    // Method getStatus with argument "principal" from dependency injection.
    public State getStatus(Principal principal){
        // Using "userRepository" from DI to make a query to retrieve record from "Users" table
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (user.isPresent()){
            // Getting "connectedUsername" (device username) that is assigned to a specific user.
            String connectedUsername = user.get().getConnectedUsername();
            // Finding the state assigned to the device username;
            Optional<State> state = stateRepository.findByusername(connectedUsername);
            if (state.isPresent()){
                // Returning JSON file of a state for a specific device.
                return state.get();
            }
        }
        return new State();
    }

    @PostMapping("/user/main/light")
    public void changeBrightness(Principal principal, @RequestBody Map<String, String> brightnessStatus){
        Optional<User> user = userRepository.findByUsername(principal.getName());
        String connectedUsername = new String();
        if (user.isPresent()) {
            connectedUsername = user.get().getConnectedUsername();
        }
        Optional<State> state = stateRepository.findByusername(connectedUsername);
        if (state.isPresent()) {
            state.get().setBrightness(Integer.valueOf(brightnessStatus.get("brightness")));
            stateRepository.save(state.get());
        }

        String JSESSIONID = httpSession.getId();
        clientService.postLight(Integer.valueOf( brightnessStatus.get("brightness")), JSESSIONID);
    }

    @PostMapping("/user/main/lock")
    public void changeLock(Principal principal, @RequestBody Map<String, String> lockStatus){
        Optional<User> user = userRepository.findByUsername(principal.getName());
        String connectedUsername = new String();
        if (user.isPresent()) {
            connectedUsername = user.get().getConnectedUsername();
        }
        Optional<State> state = stateRepository.findByusername(connectedUsername);
        if (state.isPresent()) {
            state.get().setLockStatus(Boolean.valueOf(lockStatus.get("lockStatus")));
            stateRepository.save(state.get());
        }

        String JSESSIONID = httpSession.getId();
        clientService.postLock(Boolean.valueOf( lockStatus.get("lockStatus")), JSESSIONID);
    }

}