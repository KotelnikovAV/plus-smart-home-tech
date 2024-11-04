package ru.practicum.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.hub.model.Condition;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
}
