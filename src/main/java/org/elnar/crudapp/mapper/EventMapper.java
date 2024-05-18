package org.elnar.crudapp.mapper;

import org.elnar.crudapp.dto.EventDTO;
import org.elnar.crudapp.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventMapper {
	EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);
	
	EventDTO eventToEventDTO(Event event);
	
	Event eventDTOTOEvent(EventDTO eventDTO);
}
