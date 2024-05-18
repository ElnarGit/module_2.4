package org.elnar.crudapp.servlet;

import org.elnar.crudapp.controller.EventController;
import org.elnar.crudapp.dto.EventDTO;
import org.elnar.crudapp.entity.Event;
import org.elnar.crudapp.mapper.EventMapper;
import org.elnar.crudapp.repository.EventRepository;
import org.elnar.crudapp.repository.impl.EventRepositoryImpl;
import org.elnar.crudapp.service.EventService;
import org.elnar.crudapp.service.impl.EventServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.elnar.crudapp.util.JsonUtil.readJson;
import static org.elnar.crudapp.util.JsonUtil.writeJson;

@WebServlet("/events/*")
public class EventServlet extends HttpServlet {
  private final EventController eventController = createEventController();
  private final EventMapper eventMapper = EventMapper.INSTANCE;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String pathInfo = request.getPathInfo();

    if (pathInfo == null || pathInfo.isEmpty()) {
      List<Event> events = eventController.getAllEvents();
      List<EventDTO> eventDTOS = events.stream().map(eventMapper::eventToEventDTO).toList();

      writeJson(response, eventDTOS);
    } else {
      String[] pathParts = pathInfo.split("/");
      if (pathParts.length == 2) {
        Integer id = Integer.parseInt(pathParts[1]);
        Event event = eventController.getEventById(id);
        EventDTO eventDTO = eventMapper.eventToEventDTO(event);
        writeJson(response, eventDTO);
      } else {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    Event event = readJson(request, Event.class);
    event = eventController.createEvent(event);
    EventDTO eventDTO =  eventMapper.eventToEventDTO(event);
    writeJson(response, eventDTO);
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    Event event = readJson(request, Event.class);
    event = eventController.updateEvent(event);
    EventDTO eventDTO =  eventMapper.eventToEventDTO(event);
    writeJson(response, eventDTO);
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
    String pathInfo = request.getPathInfo();

    if (pathInfo == null || pathInfo.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    } else {
      String[] pathParts = pathInfo.split("/");
      if (pathParts.length == 2) {
        Integer id = Integer.parseInt(pathParts[1]);
        eventController.deleteEventById(id);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
      } else {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
    }
  }

  private static EventController createEventController() {
    EventRepository eventRepository = new EventRepositoryImpl();
    EventService eventService = new EventServiceImpl(eventRepository);
    return new EventController(eventService);
  }
}
