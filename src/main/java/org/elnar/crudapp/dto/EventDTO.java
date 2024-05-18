package org.elnar.crudapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
  private Integer id;
  private UserDTO userDTO;
  private FileDTO fileDTO;
}
