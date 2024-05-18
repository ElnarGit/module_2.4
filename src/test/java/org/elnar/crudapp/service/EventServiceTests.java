package org.elnar.crudapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import org.elnar.crudapp.entity.Event;
import org.elnar.crudapp.entity.File;
import org.elnar.crudapp.entity.User;
import org.elnar.crudapp.repository.EventRepository;
import org.elnar.crudapp.service.impl.EventServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EventServiceTests {
  private static EventRepository eventRepository;
  private static EventService eventService;
  private static Event testEvent;
  private static User testUser;
  private static File testFile;

  @BeforeAll
  static void setUp() {
    eventRepository = Mockito.mock(EventRepository.class);
    eventService = new EventServiceImpl(eventRepository);

    testUser = User.builder().id(1).build();

    testFile = File.builder().id(1).build();

    testEvent = Event.builder().id(1).user(testUser).file(testFile).build();
  }

  @Test
  @DisplayName("Get Event by id")
  void testGetEventById() {
    when(eventRepository.getById(anyInt())).thenReturn(testEvent);

    Event event = eventService.getById(1);

    assertNotNull(event);
    assertNotNull(event.getUser());
    assertEquals(testUser.getId(), event.getUser().getId());
    assertNotNull(event.getFile());
    assertEquals(testFile.getId(), event.getFile().getId());
  }

  @Test
  @DisplayName("Get all events")
  void testGetAllEvents() {
    List<Event> events = new ArrayList<>();
    events.add(testEvent);

    when(eventRepository.getAll()).thenReturn(events);

    List<Event> result = eventService.getAll();

    assertNotNull(result);
    assertEquals(1, result.size());

    for (Event event : result) {
      assertNotNull(event.getUser());
      assertEquals(testUser.getId(), event.getUser().getId());
      assertNotNull(event.getFile());
      assertEquals(testFile.getId(), event.getFile().getId());
    }
  }

  @Test
  @DisplayName("Save event")
  void testSaveEvent() {
    when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

    Event event = eventService.save(testEvent);

    assertNotNull(event);
    assertNotNull(event.getUser());
    assertEquals(testUser.getId(), event.getUser().getId());
    assertNotNull(event.getFile());
    assertEquals(testFile.getId(), event.getFile().getId());
  }

  @Test
  @DisplayName("Update event")
  void testUpdateEvent() {
    User updateUser = User.builder().id(2).build();
    File updateFile = File.builder().id(2).build();

    Event updateEvent = Event.builder().id(1).user(updateUser).file(updateFile).build();

    when(eventRepository.update(any(Event.class))).thenReturn(updateEvent);

    Event event = eventService.update(updateEvent);

    assertNotNull(event);
    assertNotNull(event.getUser());
    assertEquals(updateUser.getId(), event.getUser().getId());
    assertNotNull(event.getFile());
    assertEquals(updateFile.getId(), event.getFile().getId());

    verify(eventRepository, times(1)).update(updateEvent);
  }

  @Test
  @DisplayName("Delete event")
  void testDeleteEvent() {
    eventService.deleteById(1);
    verify(eventRepository, times(1)).deleteById(1);
  }
}
