package org.elnar.crudapp.repository.impl;

import static org.elnar.crudapp.util.HibernateUtil.openSession;

import java.util.List;
import org.elnar.crudapp.entity.File;
import org.elnar.crudapp.exception.FileNotFoundException;
import org.elnar.crudapp.exception.RepositoryException;
import org.elnar.crudapp.repository.FileRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class FileRepositoryImpl implements FileRepository {

  public File getById(Integer id) {
    try (Session session = openSession()) {
      File file = session.get(File.class, id);

      if (file == null) {
        throw new FileNotFoundException(id);
      }

      return file;
    } catch (HibernateException e) {
      throw new RepositoryException("Ошибка при получении файла по идентификатору", e);
    }
  }

  public List<File> getAll() {
    try (Session session = openSession()) {

      return session.createQuery("FROM File", File.class).getResultList();
    } catch (HibernateException e) {
      throw new RepositoryException("Ошибка при получении всех файлов", e);
    }
  }

  public File save(File file) {
    try (Session session = openSession()) {
      session.beginTransaction();

      session.persist(file);

      session.getTransaction().commit();

      return file;
    } catch (HibernateException e) {
      throw new RepositoryException("Ошибка при сохранении файла", e);
    }
  }

  public File update(File file) {
    try (Session session = openSession()) {
      session.beginTransaction();

      session.merge(file);

      session.getTransaction().commit();

      return file;
    } catch (HibernateException e) {
      throw new RepositoryException("Ошибка при обновление файла", e);
    }
  }

  public void deleteById(Integer id) {
    try (Session session = openSession()) {
      session.beginTransaction();

      File file = getById(id);

      if (file == null) {
        throw new FileNotFoundException(id);
      }

      session.remove(file);

      session.getTransaction().commit();
    } catch (HibernateException e) {
      throw new RepositoryException("Ошибка при удаление файла", e);
    }
  }
}
