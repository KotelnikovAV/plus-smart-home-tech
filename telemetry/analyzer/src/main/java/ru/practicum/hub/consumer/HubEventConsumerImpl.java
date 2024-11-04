package ru.practicum.hub.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.configuration.ConfigurationAnalyzerHubKafkaConsumer;
import ru.practicum.hub.service.HubEventService;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventConsumerImpl implements HubEventConsumer {
    private final ConfigurationAnalyzerHubKafkaConsumer configurationAnalyzerHubKafkaConsumer;
    private final HubEventService hubEventService;

    @Override
    public void consumeHubsEvents() {
        Consumer<Void, HubEventAvro> consumer = configurationAnalyzerHubKafkaConsumer.getConsumer();

        try (consumer) {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(List.of(configurationAnalyzerHubKafkaConsumer.getTopics().get("hubs_events")));
            while (true) {
                ConsumerRecords<Void, HubEventAvro> records = consumer
                        .poll(configurationAnalyzerHubKafkaConsumer.getConsumeTimeout());

                log.info("Received {} records", records.count());

                for (ConsumerRecord<Void, HubEventAvro> record : records) {
                    hubEventService.handleEvent(record.value());
                }
            }
        } catch (WakeupException ignored) {

        }
    }
}
