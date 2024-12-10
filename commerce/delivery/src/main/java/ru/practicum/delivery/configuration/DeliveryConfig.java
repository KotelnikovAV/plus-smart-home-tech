package ru.practicum.delivery.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.practicum.dto.DeliveryMultipliers;

import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
@ConfigurationProperties("delivery")
public class DeliveryConfig {
    private final String warehouseAddress1;
    private final String warehouseAddress2;
    private final Double basicDeliveryCost;
    private final Map<DeliveryMultipliers, Double> multipliers;
}
