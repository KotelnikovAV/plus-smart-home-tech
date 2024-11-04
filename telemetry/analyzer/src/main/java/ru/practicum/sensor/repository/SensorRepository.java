package ru.practicum.sensor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.sensor.model.Sensor;

public interface SensorRepository extends JpaRepository<Sensor, String> {
}
