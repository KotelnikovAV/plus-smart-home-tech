package ru.practicum.events.service.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.configuration.ConfigurationAggregatorKafkaConsumer;
import ru.practicum.events.service.producer.AggregatorProducerService;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregatorConsumerServiceImpl implements AggregatorConsumerService {
    private final ConfigurationAggregatorKafkaConsumer configurationAggregatorKafkaConsumer;
    private final AggregatorProducerService aggregatorProducerService;
    private final Map<String, SensorsSnapshotAvro> snapshotsMap = new HashMap<>();

    @Override
    public void consumeSensorEvents() {
        Consumer<Void, SensorEventAvro> consumer = configurationAggregatorKafkaConsumer.getConsumer();

        try (consumer) {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(List.of(configurationAggregatorKafkaConsumer.getTopics().get("sensors_events")));
            while (true) {
                ConsumerRecords<Void, SensorEventAvro> records = consumer
                        .poll(configurationAggregatorKafkaConsumer.getConsumeTimeout());

                for (ConsumerRecord<Void, SensorEventAvro> record : records) {
                    Optional<SensorsSnapshotAvro> sensorsSnapshotAvro = handleRecord(record);
                    sensorsSnapshotAvro.ifPresent(aggregatorProducerService::aggregateSensorsSnapshot);
                }

                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {

        }
    }

    private Optional<SensorsSnapshotAvro> handleRecord(ConsumerRecord<Void, SensorEventAvro> record) {
        SensorsSnapshotAvro sensorsSnapshot;
        SensorEventAvro event = record.value();

        log.info("Received event {}", event);

        if (!snapshotsMap.containsKey(event.getHubId())) {
            SensorStateAvro sensorState = SensorStateAvro.newBuilder()
                    .setData(event.getPayload())
                    .setTimestamp(event.getTimestamp())
                    .setType(event.getType())
                    .build();
            sensorsSnapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(event.getHubId())
                    .setTimestamp(event.getTimestamp())
                    .setSensorsState(Map.of(event.getId(), sensorState))
                    .build();
            snapshotsMap.put(event.getHubId(), sensorsSnapshot);

            log.info("Created new snapshot {}", sensorsSnapshot);
            return Optional.of(sensorsSnapshot);
        } else {
            sensorsSnapshot = snapshotsMap.get(event.getHubId());
        }

        if (sensorsSnapshot.getSensorsState().containsKey(event.getId())) {
            SensorStateAvro oldState = sensorsSnapshot.getSensorsState().get(event.getId());

            if (oldState.getTimestamp().isAfter(event.getTimestamp()) || event.getPayload().equals(oldState.getData())) {
                log.info("Event {} handling is not required", event);
                return Optional.empty();
            } else {
                oldState.setTimestamp(event.getTimestamp());
                oldState.setType(event.getType());
                oldState.setData(event.getPayload());
                return Optional.of(sensorsSnapshot);
            }

        }

        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setData(event.getPayload())
                .setType(event.getType())
                .setTimestamp(event.getTimestamp())
                .build();
        sensorsSnapshot.getSensorsState().put(event.getId(), newState);

        log.info("Created new snapshot {}", sensorsSnapshot);
        return Optional.of(sensorsSnapshot);
    }
}