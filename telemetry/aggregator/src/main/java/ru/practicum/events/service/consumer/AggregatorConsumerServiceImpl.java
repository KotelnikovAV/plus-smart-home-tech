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
import ru.yandex.practicum.kafka.telemetry.event.*;

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

            if (oldState.getTimestamp().isAfter(event.getTimestamp()) || compareData(event, oldState)) {
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

    private boolean compareData(SensorEventAvro event, SensorStateAvro sensorState) {
        switch (event.getType()) {
            case TEMPERATURE_SENSOR_EVENT -> {
                return ((TemperatureSensorAvro) event.getPayload()).getTemperatureC() ==
                        ((TemperatureSensorAvro) sensorState.getData()).getTemperatureC();
            }
            case CLIMATE_SENSOR_EVENT -> {
                ClimateSensorAvro climateSensorEvent = (ClimateSensorAvro) event.getPayload();
                ClimateSensorAvro climateSensorState = (ClimateSensorAvro) sensorState.getData();
                return climateSensorEvent.getTemperatureC() == climateSensorState.getTemperatureC() &&
                        climateSensorEvent.getCo2Level() == climateSensorState.getCo2Level() &&
                        climateSensorEvent.getHumidity() == climateSensorState.getHumidity();
            }
            case LIGHT_SENSOR_EVENT -> {
                LightSensorAvro lightSensorEvent = (LightSensorAvro) event.getPayload();
                LightSensorAvro lightSensorState = (LightSensorAvro) sensorState.getData();
                return lightSensorState.getLuminosity() == lightSensorEvent.getLuminosity() &&
                        lightSensorState.getLinkQuality() == lightSensorEvent.getLinkQuality();
            }
            case MOTION_SENSOR_EVENT -> {
                MotionSensorAvro motionSensorEvent = (MotionSensorAvro) event.getPayload();
                MotionSensorAvro motionSensorState = (MotionSensorAvro) sensorState.getData();
                return motionSensorState.getMotion() == motionSensorEvent.getMotion() &&
                        motionSensorState.getLinkQuality() == motionSensorEvent.getLinkQuality() &&
                        motionSensorState.getVoltage() == motionSensorEvent.getVoltage();
            }
            case SWITCH_SENSOR_EVENT -> {
                return ((SwitchSensorAvro) event.getPayload()).getState() ==
                        ((SwitchSensorAvro) sensorState.getData()).getState();
            }
        }
        return false;
    }
}
