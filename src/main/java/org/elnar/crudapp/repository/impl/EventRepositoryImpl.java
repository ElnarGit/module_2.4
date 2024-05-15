package org.elnar.crudapp.repository.impl;

import org.elnar.crudapp.entity.Event;
import org.elnar.crudapp.exception.EventNotFoundException;
import org.elnar.crudapp.exception.HibernateRepositoryException;
import org.elnar.crudapp.repository.EventRepository;
import org.elnar.crudapp.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class EventRepositoryImpl implements EventRepository {
	
	public Event getById(Integer id) {
		try(Session session = HibernateUtil.openSession()){
			Event event = session.createQuery(
					"FROM Event e " +
							"left join fetch e.user " +
							"left join fetch  e.file "+
							"where e.id = :eventId", Event.class)
					.setParameter("eventId", id)
					.uniqueResult();
			
			if(event == null){
				throw new EventNotFoundException(id);
			}
			
			return event;
		}catch (HibernateRepositoryException e){
			throw new HibernateRepositoryException("Ошибка при получении события по идентификатору", e);
		}
	}
	
	public List<Event> getAll() {
		try(Session session = HibernateUtil.openSession()){
			
			return session.createQuery("FROM Event ", Event.class).getResultList();
			
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при получении всех событий", e);
		}
	}
	
	public Event save(Event event) {
		try(Session session = HibernateUtil.openSession()){
			session.beginTransaction();
			
			session.persist(event);
			
			session.getTransaction().commit();
			
			return event;
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при сохранении события", e);
		}
	}
	
	public Event update(Event event) {
		try(Session session = HibernateUtil.openSession()){
			session.beginTransaction();
			
			session.merge(event);
			
			session.getTransaction().commit();
			
			return event;
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при обновление события", e);
		}
	}
	
	public void deleteById(Integer id) {
		try(Session session = HibernateUtil.openSession()){
			session.beginTransaction();
			
			Event event = getById(id);
			
			if(event == null){
				throw new EventNotFoundException(id);
			}
			
			session.remove(event);
			
			session.getTransaction().commit();
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при удаление события", e);
		}
	}
}
