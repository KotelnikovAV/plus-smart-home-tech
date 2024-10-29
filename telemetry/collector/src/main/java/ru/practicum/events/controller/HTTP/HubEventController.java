package ru.practicum.events.controller.HTTP;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.configuration.ConfigurationHandlers;
import ru.practicum.events.model.hub.HubEvent;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("events/hubs")
public class HubEventController {
    private final ConfigurationHandlers configurationHandlers;

    @PostMapping
    public void collectHubEvent(@Valid @RequestBody HubEvent hubEvent) {
        log.info("Received hub event: {}", hubEvent);
        if (configurationHandlers.getHubEventHandlersHTTP().containsKey(hubEvent.getType())) {
            configurationHandlers.getHubEventHandlersHTTP().get(hubEvent.getType()).handle(hubEvent);
        } else {
            throw new IllegalArgumentException("There is no handler for the event " + hubEvent.getType());
        }
    }
}