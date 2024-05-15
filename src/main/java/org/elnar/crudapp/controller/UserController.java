package org.elnar.crudapp.controller;

import lombok.RequiredArgsConstructor;
import org.elnar.crudapp.entity.User;
import org.elnar.crudapp.service.UserService;

import java.util.List;

@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	
	public User getUserById(Integer id){
		return userService.getById(id);
	}
	
	public List<User> getAllUsers(){
		return userService.getAll();
	}
	
	public User createUser(User user){
		return userService.save(user);
	}
	
	public User updateUser(User user){
		return userService.update(user);
	}
	
	public void deleteUserById(Integer id){
		userService.deleteById(id);
	}
}
