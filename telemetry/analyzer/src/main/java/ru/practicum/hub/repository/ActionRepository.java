package ru.practicum.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.hub.model.Action;

public interface ActionRepository extends JpaRepository<Action, Long> {
}
