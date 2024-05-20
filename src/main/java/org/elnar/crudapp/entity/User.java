package org.elnar.crudapp.entity;

import jakarta.persistence.*;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @NotBlank(message = "Имя не может быть пустым")
  @Size(max = 20, message = "Имя должно быть меньше 20 символов")
  @Column(name = "name", nullable = false)
  private String name;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Event> events;
}
