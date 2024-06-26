package org.elnar.crudapp.controller;

import static org.elnar.crudapp.util.JsonUtil.readJsonFromRequest;
import static org.elnar.crudapp.util.JsonUtil.writeObjectToJson;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.elnar.crudapp.dto.FileDTO;
import org.elnar.crudapp.entity.File;
import org.elnar.crudapp.exception.ControllerException;
import org.elnar.crudapp.exception.FileNotFoundException;
import org.elnar.crudapp.exception.UserNotFoundException;
import org.elnar.crudapp.mapper.FileMapper;
import org.elnar.crudapp.repository.EventRepository;
import org.elnar.crudapp.repository.FileRepository;
import org.elnar.crudapp.repository.UserRepository;
import org.elnar.crudapp.repository.impl.EventRepositoryImpl;
import org.elnar.crudapp.repository.impl.FileRepositoryImpl;
import org.elnar.crudapp.repository.impl.UserRepositoryImpl;
import org.elnar.crudapp.service.EventService;
import org.elnar.crudapp.service.FileService;
import org.elnar.crudapp.service.UserService;

@WebServlet("/api/v1/files/*")
@MultipartConfig // позволяет сервлету принимать запросы, содержащие файл
public class FileRestControllerV1 extends HttpServlet {
  private final FileService fileService = createFileService();
  private final FileMapper fileMapper = FileMapper.INSTANCE;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String pathInfo = request.getPathInfo();

    if (pathInfo == null || pathInfo.isEmpty()) {
      List<File> files = fileService.getAll();
      List<FileDTO> fileDTOS = files.stream().map(fileMapper::fileToFileDTO).toList();
      writeObjectToJson(response, fileDTOS);
    } else {
      getFileById(request, response, pathInfo);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String userIdHeader = request.getHeader("user_id");
    Part filePart = request.getPart("file");

    if (userIdHeader == null || filePart == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeObjectToJson(response, Map.of("error", "Missing user ID or file in request"));
      return;
    }

    try {
      Integer userId = Integer.parseInt(userIdHeader);
      FileDTO fileDTO = fileService.uploadFile(userId, filePart);
      
      response.setStatus(HttpServletResponse.SC_CREATED);
      writeObjectToJson(response, fileDTO);
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeObjectToJson(response, Map.of("error", "Invalid user ID format"));
    } catch (UserNotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      writeObjectToJson(response, Map.of("error", e.getMessage()));
    } catch (IOException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      writeObjectToJson(response, Map.of("error", "Failed to upload file"));
    }
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    FileDTO fileDTO = readJsonFromRequest(request, FileDTO.class);

    if (fileDTO != null) {
      try {
        File file = fileMapper.fileDTOToFile(fileDTO);
        file = fileService.update(file);
        FileDTO updatedFileDTO = fileMapper.fileToFileDTO(file);
        response.setStatus(HttpServletResponse.SC_OK);
        writeObjectToJson(response, updatedFileDTO);
      } catch (FileNotFoundException e) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        writeObjectToJson(response, Map.of("error", e.getMessage()));
      } catch (ControllerException e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writeObjectToJson(response, Map.of("error", "Internal server error"));
      }
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String pathInfo = request.getPathInfo();

    if (pathInfo == null || pathInfo.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeObjectToJson(response, Map.of("error", "Missing file ID"));
    } else {
      deleteFileById(request, response, pathInfo);
    }
  }

  /////////////////////////////////////////////////////

  private static FileService createFileService() {
    FileRepository fileRepository = new FileRepositoryImpl();
    UserService userService = createUserService();
    EventService eventService = createEventService();
    return new FileService(fileRepository, userService, eventService);
  }

  private static UserService createUserService() {
    UserRepository userRepository = new UserRepositoryImpl();
    return new UserService(userRepository);
  }

  private static EventService createEventService() {
    EventRepository eventRepository = new EventRepositoryImpl();
    return new EventService(eventRepository);
  }

  private void getFileById(
      HttpServletRequest request, HttpServletResponse response, String pathInfo)
      throws IOException {
    try {
      Integer id = getFileIdFromPathInfo(pathInfo);
      File file = fileService.getById(id);

      FileDTO fileDTO = fileMapper.fileToFileDTO(file);
      response.setStatus(HttpServletResponse.SC_OK);
      writeObjectToJson(response, fileDTO);
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeObjectToJson(response, Map.of("error", "Invalid file ID format"));
    } catch (FileNotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      writeObjectToJson(response, Map.of("error", e.getMessage()));
    } catch (ControllerException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      writeObjectToJson(response, Map.of("error", "Internal server error"));
    }
  }

  private void deleteFileById(
      HttpServletRequest request, HttpServletResponse response, String pathInfo)
      throws IOException {
    try {
      Integer id = getFileIdFromPathInfo(pathInfo);
      fileService.deleteById(id);
      response.setStatus(HttpServletResponse.SC_OK);
      writeObjectToJson(response, Map.of("message", "File deleted successfully"));
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeObjectToJson(response, Map.of("error", "Invalid file ID format"));
    } catch (FileNotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      writeObjectToJson(response, Map.of("error", e.getMessage()));
    } catch (ControllerException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      writeObjectToJson(response, Map.of("error", "Internal server error"));
    }
  }

  private Integer getFileIdFromPathInfo(String pathInfo) {
    String[] pathParts = pathInfo.split("/");
    if (pathParts.length == 2) {
      return Integer.parseInt(pathParts[1]);
    } else {
      throw new NumberFormatException("Invalid path format");
    }
  }
}
