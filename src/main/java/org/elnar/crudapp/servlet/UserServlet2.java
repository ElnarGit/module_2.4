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
public class UserServlet2 extends HttpServlet {
	private  final UserController userController;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idString = req.getParameter("id");
		if(idString == null){
			List<User> users = userController.getAllUsers();
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			//resp.getWriter().print(new Gson().toJson(users));
		}else {
			int id = Integer.parseInt(idString);
			User user = userController.getUserById(id);
			resp.setContentType("application/json");
			resp.getWriter().print(new Gson().toJson(user));
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = User.builder()
				.name(req.getParameter("name"))
				.build();
		
		userController.createUser(user);
		resp.sendRedirect("/api/users");
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer id = Integer.parseInt(req.getParameter("id"));
		User user = User.builder()
				.id(id)
				.name(req.getParameter("name"))
				.build();
		
		userController.updateUser(user);
		resp.sendRedirect("/api/users");
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer id = Integer.parseInt(req.getParameter("id"));
		userController.deleteUserById(id);
		resp.sendRedirect("/api/users");
	}
}
