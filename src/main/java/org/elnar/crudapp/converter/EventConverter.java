package org.elnar.crudapp.converter;

import org.elnar.crudapp.dto.EventDTO;
import org.elnar.crudapp.dto.FileDTO;
import org.elnar.crudapp.dto.UserDTO;
import org.elnar.crudapp.entity.Event;

public class EventConverter {
	public static EventDTO toDTO(Event event){
		UserDTO userDTO = UserConverter.toDTO(event.getUser());
		FileDTO fileDTO = FileConverter.toDTO(event.getFile());
		
		return new EventDTO(event.getId(), userDTO, fileDTO);
		
	}
}
