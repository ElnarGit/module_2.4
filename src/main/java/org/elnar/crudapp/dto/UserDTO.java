package org.elnar.crudapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record UserDTO(
    Integer id,
    @NotBlank(message = "Имя не может быть пустым")
        @Size(max = 20, message = "Имя должно быть меньше 20 символов")
        String name) {}
