package com.example.SmartHomeApp.services;

import com.example.SmartHomeApp.entities.State;
import com.example.SmartHomeApp.repositories.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StateService {
    @Autowired
    private StateRepository stateRepository;

    public void changeLock(String username, Boolean doorOpened){
        Optional<State> optionalState = stateRepository.findByusername(username);
        optionalState.ifPresent(state -> state.setLockStatus(doorOpened));
    }
    public void changeLight(String username, Integer brightness){
        Optional<State> optionalState = stateRepository.findByusername(username);
        optionalState.ifPresent(state -> state.setBrightness(brightness));
    }

}
