package org.elnar.crudapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.elnar.crudapp.entity.Event;
import org.elnar.crudapp.repository.EventRepository;
import org.elnar.crudapp.service.EventService;

import java.util.List;

@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
	private final EventRepository eventRepository;
	
	@Override
	public Event getById(Integer id) {
		return eventRepository.getById(id);
	}
	
	@Override
	public List<Event> getAll() {
		return eventRepository.getAll();
	}
	
	@Override
	public Event save(Event event) {
		return eventRepository.save(event);
	}
	
	@Override
	public Event update(Event event) {
		return eventRepository.update(event);
	}
	
	@Override
	public void deleteById(Integer id) {
		eventRepository.deleteById(id);
	}
}
