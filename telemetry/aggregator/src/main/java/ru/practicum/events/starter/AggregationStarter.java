package ru.practicum.events.starter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.events.consumer.AggregatorConsumerService;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    private final AggregatorConsumerService aggregatorConsumerService;

    public void start() {
        log.info("Starting aggregator consumer service");
        aggregatorConsumerService.consumeSensorEvents();
    }
}
