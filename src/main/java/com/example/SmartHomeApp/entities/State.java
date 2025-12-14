package com.example.SmartHomeApp.entities;

import com.example.SmartHomeApp.repositories.StateRepository;
import jakarta.persistence.*;

@Entity
@Table(name = "states")
public class State {
    @GeneratedValue
    @Id
    @Column(name="username")
    private String username;
    @Column(name = "lock_status")
    private Boolean lockStatus;
    @Column(name = "light_status")
    private Integer brightness;

    public State(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getBrightness() {
        return brightness;
    }

    public void setBrightness(Integer brightness) {
        this.brightness = brightness;
    }

    public Boolean getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Boolean lockStatus) {
        this.lockStatus = lockStatus;
    }

}
