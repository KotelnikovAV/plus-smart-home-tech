package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.configuration.EventClient;
import ru.practicum.events.mapper.HubEventMapper;
import ru.practicum.events.mapper.SensorEventMapper;
import ru.practicum.events.model.hub.HubEvent;
import ru.practicum.events.model.sensor.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventsServiceImpl implements EventsService {
    private static final String TOPIC_SENSOR_EVENT = "telemetry.sensors.v1";
    private static final String TOPIC_HUB_EVENT = "telemetry.hubs.v1";

    private final EventClient eventClient;
    private Producer<String, SpecificRecordBase> producer;
    private Thread threadForTrackingProducerActivity;

    @Override
    public void collectSensorEvent(SensorEvent sensorEvent) {
        log.info("The beginning of the process of collecting sensor event");
        startActivityTimerProducer();

        synchronized (TOPIC_SENSOR_EVENT) {
            if (producer == null) {
                producer = eventClient.getProducer();
            }

            SensorEventAvro message = SensorEventAvro.newBuilder()
                    .setHubId(sensorEvent.getHubId())
                    .setId(sensorEvent.getId())
                    .setTimestamp(sensorEvent.getTimestamp())
                    .setPayload(SensorEventMapper.getSensorEventAvro(sensorEvent))
                    .build();

            ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(TOPIC_SENSOR_EVENT, message);

            producer.send(record);
        }
        log.info("The sensor event has been added to the queue for sending");
    }

    @Override
    public void collectHubEvent(HubEvent hubEvent) {
        log.info("The beginning of the process of collecting hub event");
        startActivityTimerProducer();

        synchronized (TOPIC_HUB_EVENT) {
            if (producer == null) {
                producer = eventClient.getProducer();
            }

            HubEventAvro message = HubEventAvro.newBuilder()
                    .setHubId(hubEvent.getHubId())
                    .setTimestamp(hubEvent.getTimestamp())
                    .setPayload(HubEventMapper.getHubEventAvro(hubEvent))
                    .build();

            ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(TOPIC_HUB_EVENT, message);
            producer.send(record);
        }
        log.info("The hub event has been added to the queue for sending");
    }

    private void startActivityTimerProducer() {
        if (threadForTrackingProducerActivity != null && threadForTrackingProducerActivity.isAlive()) {
            threadForTrackingProducerActivity.interrupt();
            try {
                threadForTrackingProducerActivity.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        threadForTrackingProducerActivity = new Thread(() -> {
            while (producer != null) {
                try {
                    Thread.sleep(eventClient.getTimeUntilClosingProducer());
                    synchronized (TOPIC_SENSOR_EVENT) {
                        producer.flush();
                        producer.close();
                        producer = null;
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        threadForTrackingProducerActivity.start();
    }
}