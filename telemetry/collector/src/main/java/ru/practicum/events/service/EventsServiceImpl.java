package ru.practicum.events.service;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.configuration.ConfigurationCollectorKafkaProducer;
import ru.practicum.events.utility.ProducerActivityTimer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventsServiceImpl implements EventsService {
    private final Object monitor = new Object();

    private final ConfigurationCollectorKafkaProducer configurationCollectorKafkaProducer;
    private Producer<String, SpecificRecordBase> producer;

    @Override
    public void handleSensorEvent(SensorEventAvro message) {
        log.info("The beginning of the process of collecting sensor event");

        synchronized (monitor) {
            initializeProducerAndRunActivityTimer();

            ProducerRecord<String, SpecificRecordBase> record =
                    new ProducerRecord<>(configurationCollectorKafkaProducer.getTopics().get("sensors_events"), message);

            producer.send(record);
        }

        log.info("The sensor event has been added to the queue for sending");
    }

    @Override
    public void handleHubEvent(HubEventAvro message) {
        log.info("The beginning of the process of collecting hub event");

        synchronized (monitor) {
            initializeProducerAndRunActivityTimer();

            ProducerRecord<String, SpecificRecordBase> record =
                    new ProducerRecord<>(configurationCollectorKafkaProducer.getTopics().get("hubs_events"), message);
            producer.send(record);
        }

        log.info("The hub event has been added to the queue for sending");
    }

    private void initializeProducerAndRunActivityTimer() {
        if (ProducerActivityTimer.getNecessityNewProducer()) {
            producer = configurationCollectorKafkaProducer.getProducer();
        }

        ProducerActivityTimer.startActivityTimerProducer(producer,
                configurationCollectorKafkaProducer.getTimeUntilClosingMs(),
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