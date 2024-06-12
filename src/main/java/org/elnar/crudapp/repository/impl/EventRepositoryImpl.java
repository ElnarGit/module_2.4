package org.elnar.crudapp.repository.impl;

import static org.elnar.crudapp.util.HibernateUtil.openSession;

import java.util.List;
import org.elnar.crudapp.entity.Event;
import org.elnar.crudapp.exception.EventNotFoundException;
import org.elnar.crudapp.exception.RepositoryException;
import org.elnar.crudapp.repository.EventRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class EventRepositoryImpl implements EventRepository {

  public Event getById(Integer id) {
    try (Session session = openSession()) {

      Event event =
          session
              .createQuery(
                  "FROM Event e "
                      + "left join fetch e.user u "
                      + "left join fetch e.file f "
                      + "where e.id = :eventId",
                  Event.class)
              .setParameter("eventId", id)
              .uniqueResult();

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
