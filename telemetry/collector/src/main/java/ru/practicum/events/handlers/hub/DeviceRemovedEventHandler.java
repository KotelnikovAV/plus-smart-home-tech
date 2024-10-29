package ru.practicum.events.handlers.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.events.model.hub.DeviceRemovedEvent;
import ru.practicum.events.model.hub.HubEvent;
import ru.practicum.events.model.hub.enums.HubsEventType;
import ru.practicum.events.service.EventsService;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
@RequiredArgsConstructor
public class DeviceRemovedEventHandler extends HubEventHandler {
    private final EventsService eventsService;

    @Override
    public HubEventProto.PayloadCase getMessageTypeRPC() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    public HubsEventType getMessageTypeHTTP() {
        return HubsEventType.DEVICE_REMOVED_EVENT;
    }

    @Override
    public void handle(HubEventProto hubEvent) {
        HubEventAvro message = HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(getInstant(hubEvent.getTimestamp()))
                .setPayload(getDeviceRemovedEvent(hubEvent.getDeviceRemoved()))
                .build();
        eventsService.collectHubEvent(message);
    }

    @Override
    public void handle(HubEvent hubEvent) {
        HubEventAvro message = HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(hubEvent.getTimestamp())
                .setPayload(getDeviceRemovedEvent((DeviceRemovedEvent) hubEvent))
                .build();
        eventsService.collectHubEvent(message);
    }

    private DeviceRemovedEventAvro getDeviceRemovedEvent(DeviceRemovedEventProto deviceRemovedEventProto) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(deviceRemovedEventProto.getId())
                .build();
    }

    private DeviceRemovedEventAvro getDeviceRemovedEvent(DeviceRemovedEvent deviceRemovedEvent) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(deviceRemovedEvent.getId())
                .build();
    }
}