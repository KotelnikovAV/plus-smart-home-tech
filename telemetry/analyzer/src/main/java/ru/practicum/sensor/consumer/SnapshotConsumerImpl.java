package ru.practicum.sensor.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.configuration.ConfigurationAnalyzerSnapshotKafkaConsumer;
import ru.practicum.sensor.service.AnalyzerSnapshotService;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotConsumerImpl implements SnapshotConsumer {
    private final ConfigurationAnalyzerSnapshotKafkaConsumer configurationAnalyzerSnapshotKafkaConsumer;
    private final AnalyzerSnapshotService analyzerSnapshotService;

    @Override
    public void consumeSnapshotsEvents() {
        Consumer<Void, SensorsSnapshotAvro> consumer = configurationAnalyzerSnapshotKafkaConsumer.getConsumer();

        try (consumer) {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(List.of(configurationAnalyzerSnapshotKafkaConsumer.getTopics().get("snapshots")));
            while (true) {
                ConsumerRecords<Void, SensorsSnapshotAvro> records = consumer
                        .poll(configurationAnalyzerSnapshotKafkaConsumer.getConsumeTimeout());

                for (ConsumerRecord<Void, SensorsSnapshotAvro> record : records) {
                    analyzerSnapshotService.handleSnapshot(record.value());

                }

                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {

        }
    }

}
