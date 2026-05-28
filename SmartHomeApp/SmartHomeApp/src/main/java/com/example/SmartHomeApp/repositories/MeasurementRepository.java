package com.example.SmartHomeApp.repositories;

import com.example.SmartHomeApp.entities.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
    Optional<Measurement> findByMeasurementId(Integer measurementId);
    Optional<Measurement> findTopByUsernameOrderByMeasurementIdDesc(String username);
}
