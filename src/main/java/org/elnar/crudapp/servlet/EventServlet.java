package org.elnar.crudapp.servlet;

import static org.elnar.crudapp.util.JsonUtil.writeJson;
import static org.elnar.crudapp.validator.ValidationUtil.validateDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.elnar.crudapp.controller.EventController;
import org.elnar.crudapp.dto.EventDTO;
import org.elnar.crudapp.entity.Event;
import org.elnar.crudapp.exception.EventNotFoundException;
import org.elnar.crudapp.mapper.EventMapper;
import org.elnar.crudapp.repository.EventRepository;
import org.elnar.crudapp.repository.impl.EventRepositoryImpl;
import org.elnar.crudapp.service.EventService;
import org.elnar.crudapp.service.impl.EventServiceImpl;

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
      getEventById(request, response, pathInfo);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    EventDTO eventDTO = validateDTO(request, response, EventDTO.class);

    if (eventDTO != null) {
      try {
        Event event = eventMapper.eventDTOTOEvent(eventDTO);
        event = eventController.createEvent(event);
        EventDTO createdEventDTO = eventMapper.eventToEventDTO(event);
        response.setStatus(HttpServletResponse.SC_CREATED);
        writeJson(response, createdEventDTO);
      } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writeJson(response, Map.of("error", "Internal server error"));
      }
    }
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    EventDTO eventDTO = validateDTO(request, response, EventDTO.class);

    if (eventDTO != null) {
      try {
        Event event = eventMapper.eventDTOTOEvent(eventDTO);
        event = eventController.updateEvent(event);
        EventDTO updatedEventDTO = eventMapper.eventToEventDTO(event);
        response.setStatus(HttpServletResponse.SC_OK);
        writeJson(response, updatedEventDTO);
      } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writeJson(response, Map.of("error", "Internal server error"));
      }
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String pathInfo = request.getPathInfo();

    if (pathInfo == null || pathInfo.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeJson(response, Map.of("error", "Missing event ID"));
    } else {
      deleteEventById(request, response, pathInfo);
    }
  }

  /////////////////////////////////////////////////////////////

  private static EventController createEventController() {
    EventRepository eventRepository = new EventRepositoryImpl();
    EventService eventService = new EventServiceImpl(eventRepository);
    return new EventController(eventService);
  }

  private void getEventById(
      HttpServletRequest request, HttpServletResponse response, String pathInfo)
      throws IOException {
    try {
      Integer id = extractEventId(pathInfo);
      Event event = eventController.getEventById(id);

      EventDTO eventDTO = eventMapper.eventToEventDTO(event);
      response.setStatus(HttpServletResponse.SC_OK);
      writeJson(response, eventDTO);
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeJson(response, Map.of("error", "Invalid event ID format"));
    } catch (EventNotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      writeJson(response, Map.of("error", e.getMessage()));
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      writeJson(response, Map.of("error", "Internal server error"));
    }
  }

  private void deleteEventById(
      HttpServletRequest request, HttpServletResponse response, String pathInfo)
      throws IOException {
    try {
      Integer id = extractEventId(pathInfo);
      eventController.deleteEventById(id);
      response.setStatus(HttpServletResponse.SC_OK);
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeJson(response, Map.of("error", "Invalid event ID format"));
    } catch (EventNotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      writeJson(response, Map.of("error", e.getMessage()));
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      writeJson(response, Map.of("error", "Internal server error"));
    }
  }

  private Integer extractEventId(String pathInfo) {
    String[] pathParts = pathInfo.split("/");
    if (pathParts.length == 2) {
      return Integer.parseInt(pathParts[1]);
    } else {
      throw new NumberFormatException("Invalid path format");
    }
  }
}
