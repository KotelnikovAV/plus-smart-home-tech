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
@PropertySource("classpath:application.properties")
public class EventClientConfiguration {

    @Bean
    public static EventClient getClient() {
        return new EventClient() {
            @Value("${producer.serializer}")
            private String serializer;

            @Value("${producer.lingerMs}")
            private String lingerMs;

            @Value("${producer.batch-size}")
            private String batchSize;

            @Value("${producer.port}")
            private String port;

            @Override
            public Producer<String, SpecificRecordBase> getProducer() {
                Properties config = new Properties();
                config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, port);
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