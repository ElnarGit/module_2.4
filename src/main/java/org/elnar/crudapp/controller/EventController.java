package org.elnar.crudapp.controller;

import static org.elnar.crudapp.util.JsonUtil.readJsonFromRequest;
import static org.elnar.crudapp.util.JsonUtil.writeObjectToJson;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.elnar.crudapp.dto.EventDTO;
import org.elnar.crudapp.entity.Event;
import org.elnar.crudapp.exception.ControllerException;
import org.elnar.crudapp.exception.EventNotFoundException;
import org.elnar.crudapp.mapper.EventMapper;
import org.elnar.crudapp.repository.EventRepository;
import org.elnar.crudapp.repository.impl.EventRepositoryImpl;
import org.elnar.crudapp.service.EventService;
import org.elnar.crudapp.service.impl.EventServiceImpl;

@WebServlet("/events/*")
public class EventController extends HttpServlet {
  private final EventService eventService = createEventService();
  private final EventMapper eventMapper = EventMapper.INSTANCE;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String pathInfo = request.getPathInfo();

    if (pathInfo == null || pathInfo.isEmpty()) {
      List<Event> events = eventService.getAll();
      List<EventDTO> eventDTOS = events.stream().map(eventMapper::eventToEventDTO).toList();
      writeObjectToJson(response, eventDTOS);
    } else {
      getEventById(request, response, pathInfo);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    EventDTO eventDTO = readJsonFromRequest(request, EventDTO.class);

    if (eventDTO != null) {
      try {
        Event event = eventMapper.eventDTOToEvent(eventDTO);
        event = eventService.save(event);
        EventDTO createdEventDTO = eventMapper.eventToEventDTO(event);
        response.setStatus(HttpServletResponse.SC_CREATED);
        writeObjectToJson(response, createdEventDTO);
      } catch (ControllerException e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writeObjectToJson(response, Map.of("error", "Internal server error"));
      }
    }
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    EventDTO eventDTO = readJsonFromRequest(request, EventDTO.class);

    if (eventDTO != null) {
      try {
        Event event = eventMapper.eventDTOToEvent(eventDTO);
        event = eventService.update(event);
        EventDTO updatedEventDTO = eventMapper.eventToEventDTO(event);
        response.setStatus(HttpServletResponse.SC_OK);
        writeObjectToJson(response, updatedEventDTO);
      } catch (EventNotFoundException e) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        writeObjectToJson(response, Map.of("error", e.getMessage()));
      }catch (ControllerException e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writeObjectToJson(response, Map.of("error", "Internal server error"));
      }
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String pathInfo = request.getPathInfo();

    if (pathInfo == null || pathInfo.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeObjectToJson(response, Map.of("error", "Missing event ID"));
    } else {
      deleteEventById(request, response, pathInfo);
    }
  }

  /////////////////////////////////////////////////////////////

  private static EventService createEventService() {
    EventRepository eventRepository = new EventRepositoryImpl();
    return new EventServiceImpl(eventRepository);
  }

  private void getEventById(
      HttpServletRequest request, HttpServletResponse response, String pathInfo)
      throws IOException {
    try {
      Integer id = getEventIdFromPathInfo(pathInfo);
      Event event = eventService.getById(id);

      EventDTO eventDTO = eventMapper.eventToEventDTO(event);
      response.setStatus(HttpServletResponse.SC_OK);
      writeObjectToJson(response, eventDTO);
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeObjectToJson(response, Map.of("error", "Invalid event ID format"));
    } catch (EventNotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      writeObjectToJson(response, Map.of("error", e.getMessage()));
    } catch (ControllerException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      writeObjectToJson(response, Map.of("error", "Internal server error"));
    }
  }

  private void deleteEventById(
      HttpServletRequest request, HttpServletResponse response, String pathInfo)
      throws IOException {
    try {
      Integer id = getEventIdFromPathInfo(pathInfo);
      eventService.deleteById(id);
      response.setStatus(HttpServletResponse.SC_OK);
      writeObjectToJson(response, Map.of("message", "Event deleted successfully"));
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeObjectToJson(response, Map.of("error", "Invalid event ID format"));
    } catch (EventNotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      writeObjectToJson(response, Map.of("error", e.getMessage()));
    } catch (ControllerException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      writeObjectToJson(response, Map.of("error", "Internal server error"));
    }
  }

  private Integer getEventIdFromPathInfo(String pathInfo) {
    String[] pathParts = pathInfo.split("/");
    if (pathParts.length == 2) {
      return Integer.parseInt(pathParts[1]);
    } else {
      throw new NumberFormatException("Invalid path format");
    }
  }
}
