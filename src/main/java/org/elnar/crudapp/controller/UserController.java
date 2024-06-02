package org.elnar.crudapp.controller;

import static org.elnar.crudapp.util.JsonUtil.readJsonFromRequest;
import static org.elnar.crudapp.util.JsonUtil.writeObjectToJson;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.elnar.crudapp.dto.UserDTO;
import org.elnar.crudapp.entity.User;
import org.elnar.crudapp.exception.ControllerException;
import org.elnar.crudapp.exception.UserNotFoundException;
import org.elnar.crudapp.mapper.UserMapper;
import org.elnar.crudapp.repository.UserRepository;
import org.elnar.crudapp.repository.impl.UserRepositoryImpl;
import org.elnar.crudapp.service.UserService;
import org.elnar.crudapp.service.impl.UserServiceImpl;

@WebServlet("/users/*")
public class UserController extends HttpServlet {
  private final UserService userService = createUserService();
  private final UserMapper userMapper = UserMapper.INSTANCE;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String pathInfo = request.getPathInfo();

    if (pathInfo == null || pathInfo.isEmpty()) {
      List<User> users = userService.getAll();
      List<UserDTO> userDTOS = users.stream().map(userMapper::userToUserDTO).toList();
      writeObjectToJson(response, userDTOS);
    } else {
      getUserById(request, response, pathInfo);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    UserDTO userDTO = readJsonFromRequest(request, UserDTO.class);

    if (userDTO != null) {
      try {
        User user = userMapper.userDTOToUser(userDTO);
        user = userService.save(user);
        UserDTO createdUserDTO = userMapper.userToUserDTO(user);
        response.setStatus(HttpServletResponse.SC_CREATED);
        writeObjectToJson(response, createdUserDTO);
      } catch (ControllerException e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writeObjectToJson(response, Map.of("error", "Internal server error"));
      }
    }
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    UserDTO userDTO = readJsonFromRequest(request, UserDTO.class);

    if (userDTO != null) {
      try {
        User user = userMapper.userDTOToUser(userDTO);
        user = userService.update(user);
        UserDTO updatedUserDTO = userMapper.userToUserDTO(user);
        response.setStatus(HttpServletResponse.SC_OK);
        writeObjectToJson(response, updatedUserDTO);
      } catch (UserNotFoundException e) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        writeObjectToJson(response, Map.of("error", e.getMessage()));
      }catch (ControllerException e) {
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
      writeObjectToJson(response, Map.of("error", "Missing user ID"));
    } else {
      deleteUserById(request, response, pathInfo);
    }
  }

  /////////////////////////////////////////////////////////////

  private static UserService createUserService() {
    UserRepository userRepository = new UserRepositoryImpl();
    return new UserServiceImpl(userRepository);
  }

  private void getUserById(
      HttpServletRequest request, HttpServletResponse response, String pathInfo)
      throws IOException {
    try {
      Integer id = getUserIdFromPathInfo(pathInfo);
      User user = userService.getById(id);

      UserDTO userDTO = userMapper.userToUserDTO(user);
      response.setStatus(HttpServletResponse.SC_OK);
      writeObjectToJson(response, userDTO);
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeObjectToJson(response, Map.of("error", "Invalid user ID format"));
    } catch (UserNotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      writeObjectToJson(response, Map.of("error", e.getMessage()));
    } catch (ControllerException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      writeObjectToJson(response, Map.of("error", "Internal server error"));
    }
  }

  private void deleteUserById(
      HttpServletRequest request, HttpServletResponse response, String pathInfo)
      throws IOException {
    try {
      Integer id = getUserIdFromPathInfo(pathInfo);
      userService.deleteById(id);
      response.setStatus(HttpServletResponse.SC_OK);
      writeObjectToJson(response, Map.of("message", "User deleted successfully"));
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeObjectToJson(response, Map.of("error", "Invalid user ID format"));
    } catch (UserNotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      writeObjectToJson(response, Map.of("error", e.getMessage()));
    } catch (ControllerException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      writeObjectToJson(response, Map.of("error", "Internal server error"));
    }
  }

  private Integer getUserIdFromPathInfo(String pathInfo) {
    String[] pathParts = pathInfo.split("/");
    if (pathParts.length == 2) {
      return Integer.parseInt(pathParts[1]);
    } else {
      throw new NumberFormatException("Invalid path format");
    }
  }
}
