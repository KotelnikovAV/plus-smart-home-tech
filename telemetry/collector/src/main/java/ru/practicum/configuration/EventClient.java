package ru.practicum.configuration;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;

public interface EventClient {
    Producer<String, SpecificRecordBase> getProducer();

    long getTimeUntilClosingProducer();
}
