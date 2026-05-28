package com.example.SmartHomeApp.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "measurements")
public class Measurement {
    @GeneratedValue
    @Column(name = "measurement_id")
    @Id
    private Integer measurementId;
    private String username;

    private Float distance;
    private Float temperature;
    private Float humidity;
    private Boolean motion;
    @Column(name = "measurement_time")
    private String measurementTime;

    public Integer getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(Integer measurementId) {
        this.measurementId = measurementId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(String measurementTime) {
        this.measurementTime = measurementTime;
    }

    public Measurement() {}

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Boolean getMotion() {
        return motion;
    }

    public void setMotion(Boolean motion) {
        this.motion = motion;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }
}
