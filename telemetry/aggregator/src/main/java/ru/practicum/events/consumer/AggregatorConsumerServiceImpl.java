package ru.practicum.events.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.configuration.ConfigurationAggregatorKafkaConsumer;
import ru.practicum.events.producer.AggregatorProducerService;
import ru.practicum.events.service.AggregatorService;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregatorConsumerServiceImpl implements AggregatorConsumerService {
    private final ConfigurationAggregatorKafkaConsumer configurationAggregatorKafkaConsumer;
    private final AggregatorProducerService aggregatorProducerService;
    private final AggregatorService aggregatorService;

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
                    Optional<SensorsSnapshotAvro> sensorsSnapshotAvro = aggregatorService.handleRecord(record);
                    sensorsSnapshotAvro.ifPresent(aggregatorProducerService::aggregateSensorsSnapshot);
                }

                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {

        }
    }
}