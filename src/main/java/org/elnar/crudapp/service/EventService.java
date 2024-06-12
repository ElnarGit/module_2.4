package org.elnar.crudapp.service;

import lombok.RequiredArgsConstructor;
import org.elnar.crudapp.entity.Event;
import org.elnar.crudapp.repository.EventRepository;

import java.util.List;

@RequiredArgsConstructor
public class EventService {
  private final EventRepository eventRepository;

  public Event getById(Integer id) {
    return eventRepository.getById(id);
  }

  public List<Event> getAll() {
    return eventRepository.getAll();
  }

  public Event save(Event event) {
    return eventRepository.save(event);
  }

  public Event update(Event event) {
    return eventRepository.update(event);
  }

  public void deleteById(Integer id) {
    eventRepository.deleteById(id);
  }
}
