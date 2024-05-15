package org.elnar.crudapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.elnar.crudapp.entity.User;
import org.elnar.crudapp.repository.UserRepository;
import org.elnar.crudapp.service.UserService;

import java.util.List;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	
	@Override
	public User getById(Integer id) {
		return userRepository.getById(id);
	}
	
	@Override
	public List<User> getAll() {
		return userRepository.getAll();
	}
	
	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
	
	@Override
	public User update(User user) {
		return userRepository.update(user);
	}
	
	@Override
	public void deleteById(Integer id) {
		userRepository.deleteById(id);
	}
}
