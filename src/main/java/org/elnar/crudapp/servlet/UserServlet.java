package org.elnar.crudapp.servlet;

import org.elnar.crudapp.controller.UserController;
import org.elnar.crudapp.dto.UserDTO;
import org.elnar.crudapp.entity.User;
import org.elnar.crudapp.mapper.UserMapper;
import org.elnar.crudapp.repository.UserRepository;
import org.elnar.crudapp.repository.impl.UserRepositoryImpl;
import org.elnar.crudapp.service.UserService;
import org.elnar.crudapp.service.impl.UserServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.elnar.crudapp.util.JsonUtil.readJson;
import static org.elnar.crudapp.util.JsonUtil.writeJson;

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
      String[] pathParts = pathInfo.split("/");
      if (pathParts.length == 2) {
        Integer id = Integer.parseInt(pathParts[1]);
        User user = userController.getUserById(id);
        UserDTO userDTO = userMapper.userToUserDTO(user);
        writeJson(response, userDTO);
      } else {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    User user = readJson(request, User.class);
    user = userController.createUser(user);
    UserDTO userDTO = userMapper.userToUserDTO(user);
    writeJson(response, userDTO);
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    User user = readJson(request, User.class);
    user = userController.updateUser(user);
    UserDTO userDTO = userMapper.userToUserDTO(user);
    writeJson(response, userDTO);
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
        userController.deleteUserById(id);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
      } else {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
    }
  }

  private static UserController createUserController() {
    UserRepository userRepository = new UserRepositoryImpl();
    UserService userService = new UserServiceImpl(userRepository);
    return new UserController(userService);
  }
}
