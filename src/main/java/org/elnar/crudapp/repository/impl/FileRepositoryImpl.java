package org.elnar.crudapp.repository.impl;

import org.elnar.crudapp.entity.File;
import org.elnar.crudapp.exception.FileNotFoundException;
import org.elnar.crudapp.exception.HibernateRepositoryException;
import org.elnar.crudapp.repository.FileRepository;
import org.elnar.crudapp.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class FileRepositoryImpl implements FileRepository {
	
	public File getById(Integer id) {
		try(Session session = HibernateUtil.openSession()){
			File file= session.get(File.class, id);
			
			if(file == null){
				throw new FileNotFoundException(id);
			}
			
			return file;
		}catch (HibernateRepositoryException e){
			throw new HibernateRepositoryException("Ошибка при получении файла по идентификатору", e);
		}
	}
	
	public List<File> getAll() {
		try(Session session = HibernateUtil.openSession()){
			
			return session.createQuery("FROM File", File.class).getResultList();
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при получении всех файлов", e);
		}
	}
	
	public File save(File file) {
		try(Session session = HibernateUtil.openSession()){
			session.beginTransaction();
			
			session.persist(file);
			
			session.getTransaction().commit();
			
			return file;
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при сохранении файла", e);
		}
	}
	
	public File update(File file) {
		try(Session session = HibernateUtil.openSession()){
			session.beginTransaction();
			
			session.merge(file);
			
			session.getTransaction().commit();
			
			return file;
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при обновление файла", e);
		}
	}
	
	public void deleteById(Integer id) {
		try(Session session = HibernateUtil.openSession()){
			session.beginTransaction();
			
			File file = getById(id);
			
			if(file == null){
				throw new FileNotFoundException(id);
			}
			
			session.remove(file);
			
			session.getTransaction().commit();
		}catch (HibernateRepositoryException e) {
			throw new HibernateRepositoryException("Ошибка при удаление файла", e);
		}
	}
}
