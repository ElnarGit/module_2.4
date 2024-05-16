package org.elnar.crudapp.controller;

import lombok.RequiredArgsConstructor;
import org.elnar.crudapp.entity.Event;
import org.elnar.crudapp.service.EventService;

import java.util.List;

@RequiredArgsConstructor
public class EventController {
	private final EventService eventService;
	
	public Event getEventById(Integer id){
		return eventService.getById(id);
	}
	
	public List<Event> getAllEvents(){
		return eventService.getAll();
	}
	
	public Event createEvent(Event event){
		return eventService.save(event);
	}
	
	public Event updateEvent(Event event){
		return eventService.update(event);
	}
	
	public void deleteEventById(Integer id){
		eventService.deleteById(id);
	}
}
