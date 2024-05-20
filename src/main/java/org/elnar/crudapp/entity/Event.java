package org.elnar.crudapp.entity;

import jakarta.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @NotNull(message = "Пользователь не может быть пустым") @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @NotNull(message = "Файл не может быть пустым") @ManyToOne
  @JoinColumn(name = "file_id", referencedColumnName = "id")
  private File file;
}
