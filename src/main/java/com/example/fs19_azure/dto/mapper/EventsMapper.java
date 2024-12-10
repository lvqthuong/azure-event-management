package com.example.fs19_azure.dto.mapper;

import com.example.fs19_azure.dto.EventsRead;
import com.example.fs19_azure.dto.EventsUpdate;
import com.example.fs19_azure.entity.Events;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventsMapper {
    EventsMapper INSTANCE = Mappers.getMapper(EventsMapper.class);

    static EventsRead toEventsRead(Events event) {
        return new EventsRead(
            event.getId().toString(),
            event.getName(),
            event.getDescription(),
            event.getType(),
            event.getLocation(),
            event.getStartDate().toString(),
            event.getEndDate().toString(),
            event.getOrganizer().getId().toString(),
            event.getMetadata(),
            event.getUpdatedAt().toString(),
            event.getCreatedAt().toString());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEventsFromEventsUpdate(EventsUpdate dto, @MappingTarget Events event);
}
