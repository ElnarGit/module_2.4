package org.elnar.crudapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "files")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  
  @NotBlank(message = "Имя файла не может быть пустым")
  @Size(max = 100, message = "Имя файла не должно превышать 100 символов")
  @Column(name = "name", nullable = false)
  private String name;
  
  @NotBlank(message = "Путь к файлу не может быть пустым")
  @Size(max = 255, message = "Путь к файлу не должен превышать 255 символов")
  @Column(name = "file_path", nullable = false)
  private String filePath;
}
