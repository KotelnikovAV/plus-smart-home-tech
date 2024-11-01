package ru.practicum.events.service.producer;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.configuration.ConfigurationAggregatorKafkaProducer;
import ru.practicum.events.utility.ProducerActivityTimer;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

@Service
@RequiredArgsConstructor
@Slf4j
public class AggregatorProducerServiceImpl implements AggregatorProducerService {
    private final Object monitor = new Object();

    private final ConfigurationAggregatorKafkaProducer configurationAggregatorKafkaProducer;
    private Producer<String, SpecificRecordBase> producer;

    @Override
    public void aggregateSensorsSnapshot(SensorsSnapshotAvro message) {
        log.info("The beginning of the process of aggregating sensor event");

        synchronized (monitor) {
            initializeProducerAndRunActivityTimer();

            ProducerRecord<String, SpecificRecordBase> record =
                    new ProducerRecord<>(configurationAggregatorKafkaProducer.getTopics().get("snapshots"), message);

            producer.send(record);
        }

        log.info("The sensor event has been added to the queue for sending");
    }

    private void initializeProducerAndRunActivityTimer() {
        if (ProducerActivityTimer.getNecessityNewProducer()) {
            producer = configurationAggregatorKafkaProducer.getProducer();
        }

        ProducerActivityTimer.startActivityTimerProducer(producer,
                configurationAggregatorKafkaProducer.getTimeUntilClosingMs(),
                monitor);
    }

    @PreDestroy
    private void interruptThreadForTrackingProducerActivityAndCloseProducer() {
        ProducerActivityTimer.stopActivityTimerProducer();

        if (producer != null) {
            producer.flush();
            producer.close();
        }
    }
}