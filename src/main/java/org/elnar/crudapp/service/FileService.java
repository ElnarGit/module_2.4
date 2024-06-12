package org.elnar.crudapp.service;

import lombok.RequiredArgsConstructor;
import org.elnar.crudapp.dto.FileDTO;
import org.elnar.crudapp.entity.Event;
import org.elnar.crudapp.entity.File;
import org.elnar.crudapp.entity.User;
import org.elnar.crudapp.exception.UserNotFoundException;
import org.elnar.crudapp.mapper.FileMapper;
import org.elnar.crudapp.repository.FileRepository;

import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RequiredArgsConstructor
public class FileService {
  private final FileRepository fileRepository;
  private final UserService userService;
  private final EventService eventService;
  
  public File getById(Integer id) {
    return fileRepository.getById(id);
  }
  
  public List<File> getAll() {
    return fileRepository.getAll();
  }
  
  public File save(File file) {
    return fileRepository.save(file);
  }
  
  public File update(File file) {
    return fileRepository.update(file);
  }
  
  public void deleteById(Integer id) {
    fileRepository.deleteById(id);
  }
  
  public FileDTO uploadFile(Integer userId, Part filePart) throws IOException {
    User user = userService.getById(userId);

    if (user == null) {
      throw new UserNotFoundException(userId);
    }

    // Получение имени файла
    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
    // Преобразовываем содержимое файла в массив байтов, чтобы его можно было сохранить.
    byte[] fileData = filePart.getInputStream().readAllBytes();

    // создание директории и сохранение файла на сервере
    String uploadDir = "uploads/";
    Files.createDirectories(Paths.get(uploadDir)); // создайте директорию, если ее нет
    String filePath = uploadDir + fileName;
    // Сохранение файла на сервере
    Files.write(Paths.get(filePath), fileData);

    File file = new File();
    file.setName(fileName);
    file.setFilePath(filePath);
    file = fileRepository.save(file);

    Event event = new Event();
    event.setUser(user);
    event.setFile(file);
    eventService.save(event);

    return FileMapper.INSTANCE.fileToFileDTO(file);
  }
}
