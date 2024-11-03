package ru.practicum.sensor.starter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.sensor.consumer.SnapshotConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyzerSnapshotsStarter {
    private final SnapshotConsumer snapshotConsumer;

    public void run() {
        log.info("Starting aggregator consumer service");
        snapshotConsumer.consumeSnapshotsEvents();
    }
}
