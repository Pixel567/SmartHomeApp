package com.example.SmartHomeApp.repositories;

import com.example.SmartHomeApp.entities.State;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StateRepository extends JpaRepository<State, Long> {
    Optional<State> findByusername(String username);
}
