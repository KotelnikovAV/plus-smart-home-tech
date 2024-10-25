package ru.practicum.configuration;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Properties;

@Configuration
@PropertySource("classpath:application.yaml")
public class EventClientConfiguration {

    @Bean
    public static EventClient getClient() {
        return new EventClient() {
            @Value("${collector.kafka.producer.properties.value_serializer}")
            private String serializer;

            @Value("${collector.kafka.producer.properties.lingerMs}")
            private String lingerMs;

            @Value("${collector.kafka.producer.properties.batch_size}")
            private String batchSize;

            @Value("${collector.kafka.producer.properties.bootstrap_servers}")
            private String bootstrap_servers;

            @Value("${collector.kafka.producer.properties.timeUntilClosingMs}")
            private long timeUntilClosingProducer;

            @Value("${collector.kafka.producer.topics.sensors_events}")
            private String sensorsEventsTopic;

            @Value("${collector.kafka.producer.topics.hubs_events}")
            private String hubsEventsTopic;

            @Override
            public long getTimeUntilClosingMs() {
                return timeUntilClosingProducer;
            }

            @Override
            public String getSensorsEventsTopic() {
                return sensorsEventsTopic;
            }

            @Override
            public String getHubsEventsTopic() {
                return hubsEventsTopic;
            }

            @Override
            public Producer<String, SpecificRecordBase> getProducer() {
                Properties config = new Properties();
                config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap_servers);
                config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                        "org.apache.kafka.common.serialization.StringSerializer");
                config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serializer);
                config.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
                config.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);

                return new KafkaProducer<>(config);
            }
        };
    }
}