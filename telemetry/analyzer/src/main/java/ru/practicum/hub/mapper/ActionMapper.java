package ru.practicum.hub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.hub.model.Action;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActionMapper {
    @Mapping(target = "amount", source = "value")
    Action actionAvroToAction(DeviceActionAvro deviceActionAvro);

    List<Action> actionAvroListToActionList(List<DeviceActionAvro> deviceActionAvroList);
}
