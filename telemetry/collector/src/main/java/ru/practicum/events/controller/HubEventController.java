package ru.practicum.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.events.model.hub.HubEvent;
import ru.practicum.events.service.EventsService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("events/hubs")
public class HubEventController {
    private final EventsService eventsService;

    @PostMapping
    public void collectHubEvent(@Valid @RequestBody HubEvent hubEvent) {
        log.info("Received hub event: {}", hubEvent);
        eventsService.collectHubEvent(hubEvent);
    }
}