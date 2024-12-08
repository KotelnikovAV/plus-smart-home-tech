package ru.practicum.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.payment.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {

}
