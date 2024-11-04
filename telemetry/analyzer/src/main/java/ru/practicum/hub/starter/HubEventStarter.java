package ru.practicum.hub.starter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.hub.consumer.HubEventConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventStarter implements Runnable {
    private final HubEventConsumer hubEventConsumer;

    @Override
    public void run() {
        log.info("Starting aggregator consumer service");
        hubEventConsumer.consumeHubsEvents();
    }
}
