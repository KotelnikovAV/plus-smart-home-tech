package ru.practicum.hub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.configuration.ConfigurationHandlers;
import ru.practicum.hub.handlers.HubEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Service
@RequiredArgsConstructor
@Transactional
public class HubEventServiceImpl implements HubEventService {
   private final ConfigurationHandlers configurationHandlers;

    @Override
    public void handleEvent(HubEventAvro event) {
        HubEventHandler hubEventHandler = configurationHandlers.getHubEventHandlers().get(event.getType());
        hubEventHandler.handle(event);
    }
}
