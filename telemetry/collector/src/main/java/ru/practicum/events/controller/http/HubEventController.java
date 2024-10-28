package ru.practicum.events.controller.http;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.events.mapper.HubEventMapper;
import ru.practicum.events.model.hub.HubEvent;
import ru.practicum.events.service.EventsService;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("events/hubs")
public class HubEventController {
    private final EventsService eventsService;

    @PostMapping
    public void collectHubEvent(@Valid @RequestBody HubEvent hubEvent) {
        log.info("Received hub event: {}", hubEvent);
        HubEventAvro message = HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(hubEvent.getTimestamp())
                .setPayload(HubEventMapper.getHubEventAvro(hubEvent))
                .build();
        eventsService.handleHubEvent(message);
    }
}