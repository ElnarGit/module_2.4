package org.elnar.crudapp.servlet;

import org.elnar.crudapp.controller.UserController;
import org.elnar.crudapp.entity.User;
import org.elnar.crudapp.repository.impl.UserRepositoryImpl;
import org.elnar.crudapp.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
	private final UserController userController = new UserController(new UserServiceImpl(new UserRepositoryImpl()));
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		if (id == null) {
			List<User> users = userController.getAllUsers();
			// преобразовать список пользователей в JSON и отправить в ответе
		} else {
			User user = userController.getUserById(Integer.parseInt(id));
			// преобразовать пользователя в JSON и отправить в ответе
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// распарсить JSON из тела запроса и создать объект User
		User user = new User();
		userController.createUser(user);
		// вернуть созданного пользователя в ответе в формате JSON
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// распарсить JSON из тела запроса и создать объект User
		User user = new User();
		userController.updateUser(user);
		// вернуть обновленного пользователя в ответе в формате JSON
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		userController.deleteUserById(Integer.parseInt(id));
		// вернуть код состояния 204 No Content в ответе
	}
}