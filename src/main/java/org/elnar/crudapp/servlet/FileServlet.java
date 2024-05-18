package org.elnar.crudapp.servlet;

import org.elnar.crudapp.controller.FileController;
import org.elnar.crudapp.dto.FileDTO;
import org.elnar.crudapp.entity.File;
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

import static org.elnar.crudapp.util.JsonUtil.readJson;
import static org.elnar.crudapp.util.JsonUtil.writeJson;

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

      writeJson(response, fileDTOS);
    } else {
      String[] pathParts = pathInfo.split("/");
      if (pathParts.length == 2) {
        Integer id = Integer.parseInt(pathParts[1]);
        File file = fileController.getFileById(id);
        FileDTO fileDTO = fileMapper.fileToFileDTO(file);
        writeJson(response, fileDTO);
      } else {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    File file = readJson(request, File.class);
    file = fileController.createFile(file);
    FileDTO fileDTO = fileMapper.fileToFileDTO(file);
    writeJson(response, fileDTO);
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    File file = readJson(request, File.class);
    file = fileController.updateFile(file);
    FileDTO fileDTO = fileMapper.fileToFileDTO(file);
    writeJson(response, fileDTO);
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
    String pathInfo = request.getPathInfo();

    if (pathInfo == null || pathInfo.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    } else {
      String[] pathParts = pathInfo.split("/");
      if (pathParts.length == 2) {
        Integer id = Integer.parseInt(pathParts[1]);
        fileController.deleteFileById(id);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
      } else {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
    }
  }

  private static FileController createFileController() {
    FileRepository fileRepository = new FileRepositoryImpl();
    FileService fileService = new FileServiceImpl(fileRepository);
    return new FileController(fileService);
  }
}
