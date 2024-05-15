package org.elnar.crudapp.servlet;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.elnar.crudapp.controller.UserController;
import org.elnar.crudapp.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class UserServlet3 extends HttpServlet {
	private final UserController userController;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idString = req.getParameter("id");
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		
		try {
			if (idString == null) {
				List<User> users = userController.getAllUsers();
				resp.getWriter().print(new Gson().toJson(users));
			} else {
				int id = Integer.parseInt(idString);
				User user = userController.getUserById(id);
				if (user != null) {
					resp.getWriter().print(new Gson().toJson(user));
				} else {
					resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
					resp.getWriter().print("{\"message\":\"User not found\"}");
				}
			}
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().print("{\"message\":\"Error processing request\"}");
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			User user = User.builder()
					.name(req.getParameter("name"))
					.build();
			userController.createUser(user);
			resp.setStatus(HttpServletResponse.SC_CREATED);
			resp.sendRedirect("/api/users");
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().print("{\"message\":\"Error creating user\"}");
		}
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Integer id = Integer.parseInt(req.getParameter("id"));
			User user = User.builder()
					.id(id)
					.name(req.getParameter("name"))
					.build();
			userController.updateUser(user);
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.sendRedirect("/api/users");
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().print("{\"message\":\"Error updating user\"}");
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Integer id = Integer.parseInt(req.getParameter("id"));
			userController.deleteUserById(id);
			resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
			resp.sendRedirect("/api/users");
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().print("{\"message\":\"Error deleting user\"}");
		}
	}
}