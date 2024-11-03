package ru.practicum.sensor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.sensor.model.Sensor;

import java.util.Collection;
import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, String> {
    boolean existsByIdInAndHubId(Collection<String> ids, String hubId);

    Optional<Sensor> findByIdAndHubId(String id, String hubId);
}
