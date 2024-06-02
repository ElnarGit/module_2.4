package org.elnar.crudapp.dto;

public record EventDTO(
		Integer id,
		UserDTO userDTO,
		FileDTO fileDTO
) {}
