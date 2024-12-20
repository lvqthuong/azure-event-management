package com.fs19.webservice.dto.mapper;

import com.fs19.webservice.dto.EventsRead;
import com.fs19.webservice.dto.EventsUpdate;
import com.fs19.webservice.dto.EventsWithAttachments;
import com.fs19.webservice.dto.UploadedAttachment;
import com.fs19.webservice.entity.Events;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

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

    static EventsWithAttachments toEventsWithAttachments(Events event, List<UploadedAttachment> attachments) {
        return new EventsWithAttachments(
            event.getId().toString(),
            event.getName(),
            event.getDescription(),
            event.getType(),
            event.getLocation(),
            event.getStartDate().toString(),
            event.getEndDate().toString(),
            event.getOrganizer().getId().toString(),
            attachments,
            event.getMetadata(),
            event.getUpdatedAt().toString(),
            event.getCreatedAt().toString());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEventsFromEventsUpdate(EventsUpdate dto, @MappingTarget Events event);
}
