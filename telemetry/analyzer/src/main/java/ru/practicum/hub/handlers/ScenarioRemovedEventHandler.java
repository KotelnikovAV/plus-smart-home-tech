package ru.practicum.hub.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.hub.repository.ScenarioRepository;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubsEventTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScenarioRemovedEventHandler implements HubEventHandler {
    private final ScenarioRepository scenarioRepository;

    @Override
    public HubsEventTypeAvro getMessageType() {
        return HubsEventTypeAvro.SCENARIO_REMOVED_EVENT;
    }

    @Override
    public void handle(HubEventAvro hubEvent) {
        log.info("Handling scenario removed: {}", hubEvent);

        ScenarioRemovedEventAvro scenarioRemovedEventAvro = (ScenarioRemovedEventAvro) hubEvent.getPayload();
        scenarioRepository.deleteByName(scenarioRemovedEventAvro.getName());

        log.info("Scenario removed: {}", scenarioRemovedEventAvro);
    }
}
