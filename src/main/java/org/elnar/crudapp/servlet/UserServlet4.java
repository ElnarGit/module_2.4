package org.elnar.crudapp.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elnar.crudapp.entity.User;
import org.elnar.crudapp.repository.impl.UserRepositoryImpl;
import org.elnar.crudapp.service.UserService;
import org.elnar.crudapp.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/users/*")
public class UserServlet4 extends HttpServlet {
	
	private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			List<User> users = userService.getAll();
			String json = objectMapper.writeValueAsString(users);
			resp.setContentType("application/json");
			resp.getWriter().write(json);
		} else {
			try {
				String[] pathParts = pathInfo.split("/");
				Integer id = Integer.parseInt(pathParts[1]);
				User user = userService.getById(id);
				String json = objectMapper.writeValueAsString(user);
				resp.setContentType("application/json");
				resp.getWriter().write(json);
			} catch (Exception e) {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				resp.getWriter().write("{\"error\": \"Invalid user ID\"}");
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = objectMapper.readValue(req.getInputStream(), User.class);
		User createdUser = userService.save(user);
		String json = objectMapper.writeValueAsString(createdUser);
		resp.setContentType("application/json");
		resp.getWriter().write(json);
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = objectMapper.readValue(req.getInputStream(), User.class);
		User updatedUser = userService.update(user);
		String json = objectMapper.writeValueAsString(updatedUser);
		resp.setContentType("application/json");
		resp.getWriter().write(json);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		try {
			String[] pathParts = pathInfo.split("/");
			Integer id = Integer.parseInt(pathParts[1]);
			userService.deleteById(id);
			resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write("{\"error\": \"Invalid user ID\"}");
		}
	}
}