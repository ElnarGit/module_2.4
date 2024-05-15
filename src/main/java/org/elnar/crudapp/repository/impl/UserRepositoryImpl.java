package org.elnar.crudapp.repository.impl;

import org.elnar.crudapp.entity.User;
import org.elnar.crudapp.exception.HibernateRepositoryException;
import org.elnar.crudapp.exception.UserNotFoundException;
import org.elnar.crudapp.repository.UserRepository;
import org.elnar.crudapp.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class UserRepositoryImpl implements UserRepository {
	
	public User getById(Integer id) {
		try(Session session = HibernateUtil.openSession()){
			User user = session.createQuery(
					"FROM User u " +
							"left join fetch u.events " +
					        "where u.id = :userId", User.class)
					.setParameter("userId", id)
					.uniqueResult();
			
			if(user == null){
				throw new UserNotFoundException(id);
			}
			
			return user;
		}catch (HibernateRepositoryException e){
			throw new HibernateRepositoryException("Ошибка при получении пользователя по идентификатору", e);
		}
	}
	
	public List<User> getAll() {
		try(Session session = HibernateUtil.openSession()){
			
			return session.createQuery("FROM User", User.class).getResultList();
			
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при получении всех пользователей", e);
		}
	}
	
	public User save(User user) {
		try(Session session = HibernateUtil.openSession()){
			session.beginTransaction();
			
			session.persist(user);
			
			session.getTransaction().commit();
			
			return user;
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при сохранении пользователя", e);
		}
	}
	
	public User update(User user) {
		try(Session session = HibernateUtil.openSession()){
			session.beginTransaction();
			
			session.merge(user);
			
			session.getTransaction().commit();
			
			return user;
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при обновление пользователя", e);
		}
	}
	
	public void deleteById(Integer id) {
		try(Session session = HibernateUtil.openSession()){
			session.beginTransaction();
			
			User user = getById(id);
			
			if(user == null){
				throw new UserNotFoundException(id);
			}
			
			session.remove(user);
			
			session.getTransaction().commit();
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при удаление пользователя", e);
		}
	}
}
