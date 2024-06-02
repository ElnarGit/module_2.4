package org.elnar.crudapp.repository.impl;

import static org.elnar.crudapp.util.HibernateUtil.openSession;

import java.util.List;
import org.elnar.crudapp.entity.User;
import org.elnar.crudapp.exception.RepositoryException;
import org.elnar.crudapp.exception.UserNotFoundException;
import org.elnar.crudapp.repository.UserRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class UserRepositoryImpl implements UserRepository {

  public User getById(Integer id) {
    try (Session session = openSession()) {
      User user =
          session
              .createQuery(
                  "FROM User u " + "left join fetch u.events " + "where u.id = :userId", User.class)
              .setParameter("userId", id)
              .uniqueResult();

      if (user == null) {
        throw new UserNotFoundException(id);
      }

      return user;
    } catch (HibernateException e) {
      throw new RepositoryException("Ошибка при получении пользователя по идентификатору", e);
    }
  }

  public List<User> getAll() {
    try (Session session = openSession()) {

      return session.createQuery("FROM User", User.class).getResultList();

    } catch (HibernateException e) {
      throw new RepositoryException("Ошибка при получении всех пользователей", e);
    }
  }

  public User save(User user) {
    try (Session session = openSession()) {
      session.beginTransaction();

      session.persist(user);

      session.getTransaction().commit();

      return user;
    } catch (HibernateException e) {
      throw new RepositoryException("Ошибка при сохранении пользователя", e);
    }
  }

  public User update(User user) {
    try (Session session = openSession()) {
      session.beginTransaction();
  
      User updateUser = getById(user.getId());
  
      if(updateUser == null){
        throw new UserNotFoundException(user.getId());
      }

      session.merge(user);

      session.getTransaction().commit();

      return user;
    } catch (HibernateException e) {
      throw new RepositoryException("Ошибка при обновление пользователя", e);
    }
  }

  public void deleteById(Integer id) {
    try (Session session = openSession()) {
      session.beginTransaction();

      User user = getById(id);

      if (user == null) {
        throw new UserNotFoundException(id);
      }

      session.remove(user);

      session.getTransaction().commit();
    } catch (HibernateException e) {
      throw new RepositoryException("Ошибка при удаление пользователя", e);
    }
  }
}
