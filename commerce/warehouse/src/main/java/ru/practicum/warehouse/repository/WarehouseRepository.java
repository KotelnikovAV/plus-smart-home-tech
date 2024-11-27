package ru.practicum.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.warehouse.model.Warehouse;


public interface WarehouseRepository extends JpaRepository<Warehouse, String> {

}
