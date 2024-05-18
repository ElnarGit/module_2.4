package org.elnar.crudapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import org.elnar.crudapp.entity.File;
import org.elnar.crudapp.repository.FileRepository;
import org.elnar.crudapp.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class FileServiceTests {
  private static FileRepository fileRepository;
  private static FileService fileService;
  private static File testFile;

  @BeforeAll
  static void setUp() {
    fileRepository = Mockito.mock(FileRepository.class);
    fileService = new FileServiceImpl(fileRepository);

    testFile = File.builder().id(1).name("Test name").filePath("Test file path").build();
  }

  @Test
  @DisplayName("Get File by id")
  void testGetFileById() {
    when(fileRepository.getById(anyInt())).thenReturn(testFile);

    File file = fileService.getById(1);

    assertNotNull(file);
    assertEquals(file.getName(), "Test name");
    assertEquals(file.getFilePath(), "Test file path");
  }

  @Test
  @DisplayName("Get all files")
  void testGetAllFiles() {
    List<File> files = new ArrayList<>();
    files.add(testFile);

    when(fileRepository.getAll()).thenReturn(files);

    List<File> result = fileService.getAll();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Test name", result.get(0).getName());
    assertEquals("Test file path", result.get(0).getFilePath());
  }

  @Test
  @DisplayName("Save file")
  void testSaveFile() {
    when(fileRepository.save(any(File.class))).thenReturn(testFile);

    File file = fileService.save(testFile);

    assertNotNull(file);
    assertEquals("Test name", file.getName());
    assertEquals("Test file path", file.getFilePath());
  }

  @Test
  @DisplayName("Update file")
  void testUpdateFile() {
    File updateFile = File.builder().id(1).name("Update name").filePath("Update file path").build();

    when(fileRepository.update(any(File.class))).thenReturn(updateFile);

    File file = fileService.update(updateFile);

    assertNotNull(file);
    assertEquals("Update name", file.getName());
    assertEquals("Update file path", file.getFilePath());

    verify(fileRepository, times(1)).update(updateFile);
  }

  @Test
  @DisplayName("Delete file")
  void testDeleteFile() {
    fileService.deleteById(1);
    verify(fileRepository, times(1)).deleteById(1);
  }
}
