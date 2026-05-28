package com.example.SmartHomeApp.services;

import com.example.SmartHomeApp.entities.Measurement;
import com.example.SmartHomeApp.repositories.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MeasurementService {

    @Autowired
    MeasurementRepository measurementRepository;

    public MeasurementService(MeasurementRepository measurementRepository){
        this.measurementRepository = measurementRepository;
    }

    public void createMeasurement(String username, Measurement measurement){
        Measurement m = new Measurement();
        m.setUsername(username);
        m.setDistance(measurement.getDistance());
        m.setMeasurementTime(measurement.getMeasurementTime());
        m.setMotion(measurement.getMotion());
        m.setTemperature(measurement.getTemperature());
        m.setHumidity(measurement.getHumidity());
        measurementRepository.save(m);
    }

    public Optional<Measurement> readNewestMeasurement(String username){
        return measurementRepository.findTopByUsernameOrderByMeasurementIdDesc(username);
    }

    public List<Measurement> getAllMeasurements(){
        return measurementRepository.findAll();
    }
}
