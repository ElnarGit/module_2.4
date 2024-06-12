package org.elnar.crudapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import org.elnar.crudapp.entity.User;
import org.elnar.crudapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UserServiceTests {
  private static UserRepository userRepository;
  private static UserService userService;
  private static User testUser;

  @BeforeAll
  static void setUp() {
    userRepository = Mockito.mock(UserRepository.class);
    userService = new UserService(userRepository);

    testUser = User.builder().id(1).name("Test name").events(new ArrayList<>()).build();
  }

  @Test
  @DisplayName("Get User by id")
  void testGetUserById() {
    when(userRepository.getById(anyInt())).thenReturn(testUser);

    User user = userService.getById(1);

    assertNotNull(user);
    assertEquals(user.getName(), "Test name");
  }

  @Test
  @DisplayName("Get all users")
  void testGetAllUsers() {
    List<User> users = new ArrayList<>();
    users.add(testUser);

    when(userRepository.getAll()).thenReturn(users);

    List<User> result = userService.getAll();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Test name", result.get(0).getName());
  }

  @Test
  @DisplayName("Save user")
  void testSaveUser() {
    when(userRepository.save(any(User.class))).thenReturn(testUser);

    User user = userService.save(testUser);

    assertNotNull(user);
    assertEquals("Test name", user.getName());
  }

  @Test
  @DisplayName("Update user")
  void testUpdateUser() {
    User updateUser = User.builder().id(1).name("Update name").events(new ArrayList<>()).build();

    when(userRepository.update(any(User.class))).thenReturn(updateUser);

    User user = userService.update(updateUser);

    assertNotNull(user);
    assertEquals("Update name", user.getName());

    verify(userRepository, times(1)).update(updateUser);
  }

  @Test
  @DisplayName("Delete user")
  void testDeleteUser() {
    userService.deleteById(1);
    verify(userRepository, times(1)).deleteById(1);
  }
}
