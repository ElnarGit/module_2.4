package org.elnar.crudapp.servlet;

import org.elnar.crudapp.controller.FileController;
import org.elnar.crudapp.dto.FileDTO;
import org.elnar.crudapp.entity.File;
import org.elnar.crudapp.exception.FileNotFoundException;
import org.elnar.crudapp.mapper.FileMapper;
import org.elnar.crudapp.repository.FileRepository;
import org.elnar.crudapp.repository.impl.FileRepositoryImpl;
import org.elnar.crudapp.service.FileService;
import org.elnar.crudapp.service.impl.FileServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.elnar.crudapp.util.JsonUtil.writeObjectToJson;
import static org.elnar.crudapp.validator.ValidationUtil.validateDTO;

@WebServlet("/files/*")
public class FileServlet extends HttpServlet {
  private final FileController fileController = createFileController();
  private final FileMapper fileMapper = FileMapper.INSTANCE;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String pathInfo = request.getPathInfo();

    if (pathInfo == null || pathInfo.isEmpty()) {
      List<File> files = fileController.getAllFiles();
      List<FileDTO> fileDTOS = files.stream().map(fileMapper::fileToFileDTO).toList();
      writeObjectToJson(response, fileDTOS);
    } else {
      getFileById(request, response, pathInfo);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    FileDTO fileDTO = validateDTO(request, response, FileDTO.class);

    if (fileDTO != null) {
      try {
        File file = fileMapper.fileDTOToFile(fileDTO);
        file = fileController.createFile(file);
        FileDTO createdFileDTO = fileMapper.fileToFileDTO(file);
        response.setStatus(HttpServletResponse.SC_CREATED);
        writeObjectToJson(response, createdFileDTO);
      } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writeObjectToJson(response, Map.of("error", "Internal server error"));
      }
    }
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    FileDTO fileDTO = validateDTO(request, response, FileDTO.class);

    if (fileDTO != null) {
      try {
        File file = fileMapper.fileDTOToFile(fileDTO);
        file = fileController.updateFile(file);
        FileDTO updatedFileDTO = fileMapper.fileToFileDTO(file);
        response.setStatus(HttpServletResponse.SC_OK);
        writeObjectToJson(response, updatedFileDTO);
      } catch (Exception e) {
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

  private static FileController createFileController() {
    FileRepository fileRepository = new FileRepositoryImpl();
    FileService fileService = new FileServiceImpl(fileRepository);
    return new FileController(fileService);
  }

  private void getFileById(
      HttpServletRequest request, HttpServletResponse response, String pathInfo)
      throws IOException {
    try {
      Integer id = getFileIdFromPathInfo(pathInfo);
      File file = fileController.getFileById(id);

      FileDTO fileDTO = fileMapper.fileToFileDTO(file);
      response.setStatus(HttpServletResponse.SC_OK);
      writeObjectToJson(response, fileDTO);
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeObjectToJson(response, Map.of("error", "Invalid file ID format"));
    } catch (FileNotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      writeObjectToJson(response, Map.of("error", e.getMessage()));
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      writeObjectToJson(response, Map.of("error", "Internal server error"));
    }
  }

  private void deleteFileById(
      HttpServletRequest request, HttpServletResponse response, String pathInfo)
      throws IOException {
    try {
      Integer id = getFileIdFromPathInfo(pathInfo);
      fileController.deleteFileById(id);
      response.setStatus(HttpServletResponse.SC_OK);
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeObjectToJson(response, Map.of("error", "Invalid file ID format"));
    } catch (FileNotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      writeObjectToJson(response, Map.of("error", e.getMessage()));
    } catch (Exception e) {
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
