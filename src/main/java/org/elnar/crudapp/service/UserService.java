package org.elnar.crudapp.service;

import lombok.RequiredArgsConstructor;
import org.elnar.crudapp.entity.User;
import org.elnar.crudapp.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  
  public User getById(Integer id) {
    return userRepository.getById(id);
  }
  
  public List<User> getAll() {
    return userRepository.getAll();
  }
  
  public User save(User user) {
    return userRepository.save(user);
  }
  
  public User update(User user) {
    return userRepository.update(user);
  }
  
  public void deleteById(Integer id) {
    userRepository.deleteById(id);
  }
}
