package org.elnar.crudapp.dto;

import javax.validation.constraints.NotNull;

public record EventDTO(
    Integer id,
    @NotNull(message = "Пользователь не может быть пустым") UserDTO userDTO,
    @NotNull(message = "Файл не может быть пустым") FileDTO fileDTO) {}
