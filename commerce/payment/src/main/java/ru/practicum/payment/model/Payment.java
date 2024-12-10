package ru.practicum.payment.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payments")
@Getter
@Setter
@EqualsAndHashCode(of = "paymentId")
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    private String paymentId;

    @Column
    private Double totalPayment;

    @Column
    private Double deliveryTotal;

    @Column
    private Double feeTotal;

    @Column
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;
}
