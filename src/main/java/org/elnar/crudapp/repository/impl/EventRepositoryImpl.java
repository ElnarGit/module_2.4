package org.elnar.crudapp.repository.impl;

import jakarta.persistence.EntityGraph;
import org.elnar.crudapp.entity.Event;
import org.elnar.crudapp.exception.EventNotFoundException;
import org.elnar.crudapp.exception.RepositoryException;
import org.elnar.crudapp.repository.EventRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.Collections;
import java.util.List;

import static org.elnar.crudapp.util.HibernateUtil.openSession;

public class EventRepositoryImpl implements EventRepository {
  
  public Event getById(Integer id) {
    try (Session session = openSession()) {
      
      EntityGraph<Event> graph = session.createEntityGraph(Event.class);
      graph.addAttributeNodes("user", "file");
      
      Event event = session.find(Event.class, id,
              Collections.singletonMap("javax.persistence.fetchgraph", graph));
      
      if (event == null) {
        throw new EventNotFoundException(id);
      }
      
      return event;
    } catch (HibernateException e) {
      throw new RepositoryException("Ошибка при получении события по идентификатору", e);
    }
  }
  
  public List<Event> getAll() {
    try (Session session = openSession()) {

      return session.createQuery("FROM Event ", Event.class).getResultList();

    } catch (HibernateException e) {
      throw new RepositoryException("Ошибка при получении всех событий", e);
    }
  }

  public Event save(Event event) {
    try (Session session = openSession()) {
      session.beginTransaction();

      session.persist(event);

      session.getTransaction().commit();

      return event;
    } catch (HibernateException e) {
      throw new RepositoryException("Ошибка при сохранении события", e);
    }
  }

  public Event update(Event event) {
    try (Session session = openSession()) {
      session.beginTransaction();

      Event updateEvent = getById(event.getId());

      if (updateEvent == null) {
        throw new EventNotFoundException(event.getId());
      }

      session.merge(event);

      session.getTransaction().commit();

      return event;
    } catch (HibernateException e) {
      throw new RepositoryException("Ошибка при обновление события", e);
    }
  }

  public void deleteById(Integer id) {
    try (Session session = openSession()) {
      session.beginTransaction();

      Event event = getById(id);

      if (event == null) {
        throw new EventNotFoundException(id);
      }

      session.remove(event);

      session.getTransaction().commit();
    } catch (HibernateException e) {
      throw new RepositoryException("Ошибка при удаление события", e);
    }
  }
}
