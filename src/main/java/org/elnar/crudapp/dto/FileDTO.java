package org.elnar.crudapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


public record FileDTO (
   Integer id,
   
   @NotBlank(message = "Имя файла не может быть пустым")
   @Size(max = 100, message = "Имя файла не должно превышать 100 символов")
   String name,
  
   @NotBlank(message = "Путь к файлу не может быть пустым")
   @Size(max = 255, message = "Путь к файлу не должен превышать 255 символов")
   String filePath
){}
