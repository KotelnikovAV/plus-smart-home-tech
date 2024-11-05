package ru.practicum.events.handlers.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.events.model.hub.DeviceAddedEvent;
import ru.practicum.events.model.hub.HubEvent;
import ru.practicum.events.model.hub.enums.HubsEventType;
import ru.practicum.events.producer.EventsProducer;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubsEventTypeAvro;

@Component
@RequiredArgsConstructor
public class DeviceAddedEventHandler extends HubEventHandler {
    private final EventsProducer eventsProducer;

    @Override
    public HubEventProto.PayloadCase getMessageTypeRPC() {
        return HubEventProto.PayloadCase.DEVICE_ADDED_EVENT;
    }

    @Override
    public HubsEventType getMessageTypeHTTP() {
        return HubsEventType.DEVICE_ADDED_EVENT;
    }

    @Override
    public void handle(HubEventProto hubEvent) {
        HubEventAvro message = HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(getInstant(hubEvent.getTimestamp()))
                .setPayload(getDeviceAddedEvent(hubEvent.getDeviceAddedEvent()))
                .setType(HubsEventTypeAvro.DEVICE_ADDED_EVENT)
                .build();
        eventsProducer.collectHubEvent(message);
    }

    @Override
    public void handle(HubEvent hubEvent) {
        HubEventAvro message = HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(hubEvent.getTimestamp())
                .setPayload(getDeviceAddedEvent((DeviceAddedEvent) hubEvent))
                .setType(HubsEventTypeAvro.DEVICE_ADDED_EVENT)
                .build();
        eventsProducer.collectHubEvent(message);
    }

    private DeviceAddedEventAvro getDeviceAddedEvent(DeviceAddedEventProto deviceAddedEventProto) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedEventProto.getId())
                .setType(DeviceTypeAvro.valueOf(deviceAddedEventProto.getType().toString()))
                .build();
    }

    private DeviceAddedEventAvro getDeviceAddedEvent(DeviceAddedEvent deviceAddedEvent) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedEvent.getId())
                .setType(DeviceTypeAvro.valueOf(deviceAddedEvent.getDeviceType().toString()))
                .build();
    }
}
