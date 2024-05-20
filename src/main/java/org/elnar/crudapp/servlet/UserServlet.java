package org.elnar.crudapp.servlet;

import static org.elnar.crudapp.util.JsonUtil.writeJson;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.elnar.crudapp.controller.UserController;
import org.elnar.crudapp.dto.UserDTO;
import org.elnar.crudapp.entity.User;
import org.elnar.crudapp.exception.UserNotFoundException;
import org.elnar.crudapp.mapper.UserMapper;
import org.elnar.crudapp.repository.UserRepository;
import org.elnar.crudapp.repository.impl.UserRepositoryImpl;
import org.elnar.crudapp.service.UserService;
import org.elnar.crudapp.service.impl.UserServiceImpl;
import org.elnar.crudapp.validator.ValidationUtil;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {
  private final UserController userController = createUserController();
  private final UserMapper userMapper = UserMapper.INSTANCE;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String pathInfo = request.getPathInfo();

    if (pathInfo == null || pathInfo.isEmpty()) {
      List<User> users = userController.getAllUsers();
      List<UserDTO> userDTOS = users.stream().map(userMapper::userToUserDTO).toList();
      writeJson(response, userDTOS);
    } else {
      handleGetById(request, response, pathInfo);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    UserDTO userDTO = ValidationUtil.validateDTO(request, response, UserDTO.class);

    if (userDTO != null) {
      try {

        User user = userMapper.userDTOToUser(userDTO);
        user = userController.createUser(user);
        UserDTO createdUserDTO = userMapper.userToUserDTO(user);
        response.setStatus(HttpServletResponse.SC_CREATED);
        writeJson(response, createdUserDTO);
      } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writeJson(response, Map.of("error", "Internal server error"));
      }
    }
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    UserDTO userDTO = ValidationUtil.validateDTO(request, response, UserDTO.class);

    if (userDTO != null) {
      try {

        User user = userMapper.userDTOToUser(userDTO);
        user = userController.updateUser(user);
        UserDTO updatedUserDTO = userMapper.userToUserDTO(user);
        response.setStatus(HttpServletResponse.SC_OK);
        writeJson(response, updatedUserDTO);
      } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writeJson(response, Map.of("error", "Internal server error"));
      }
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String pathInfo = request.getPathInfo();

    if (pathInfo == null || pathInfo.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeJson(response, Map.of("error", "Missing user ID"));
    } else {
      handleDeleteById(request, response, pathInfo);
    }
  }

  //////////////////////////////////////////////////////////////

  private static UserController createUserController() {
    UserRepository userRepository = new UserRepositoryImpl();
    UserService userService = new UserServiceImpl(userRepository);
    return new UserController(userService);
  }

  private void handleGetById(
      HttpServletRequest request, HttpServletResponse response, String pathInfo)
      throws IOException {
    try {

      String[] pathParts = pathInfo.substring(1).split("/");

      if (pathParts.length == 1) {
        Integer id = Integer.parseInt(pathParts[0]);
        User user = userController.getUserById(id);
        if (user != null) {
          UserDTO userDTO = userMapper.userToUserDTO(user);
          response.setStatus(HttpServletResponse.SC_OK);
          writeJson(response, userDTO);
        }
      } else {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        writeJson(response, Map.of("error", "Invalid path format"));
      }
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeJson(response, Map.of("error", "Invalid user ID format"));
    } catch (UserNotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      writeJson(response, Map.of("error", e.getMessage()));
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      writeJson(response, Map.of("error", "Internal server error"));
    }
  }

  private void handleDeleteById(
      HttpServletRequest request, HttpServletResponse response, String pathInfo)
      throws IOException {
    try {

      String[] pathParts = pathInfo.substring(1).split("/");

      if (pathParts.length == 1) {
        Integer id = Integer.parseInt(pathParts[0]);
        userController.deleteUserById(id);
        response.setStatus(HttpServletResponse.SC_OK);
      } else {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        writeJson(response, Map.of("error", "Invalid path format"));
      }
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeJson(response, Map.of("error", "Invalid user ID format"));
    } catch (UserNotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      writeJson(response, Map.of("error", e.getMessage()));
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      writeJson(response, Map.of("error", "Internal server error"));
    }
  }
}
