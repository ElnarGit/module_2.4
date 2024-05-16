package org.elnar.crudapp.util;

import lombok.experimental.UtilityClass;
import org.elnar.crudapp.entity.Event;
import org.elnar.crudapp.entity.File;
import org.elnar.crudapp.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@UtilityClass
public final class HibernateUtil {
	private static final SessionFactory sessionFactory;
	
	static {
		try {
			Configuration configuration = new Configuration()
					.setProperty("hibernate.driver_class", "org.postgresql.Driver")
					.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/postgres")
					.setProperty("hibernate.connection.username", "postgres")
					.setProperty("hibernate.connection.password", "12345")
					.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
					.setProperty("hibernate.show_sql", "true")
					.setProperty("hibernate.format_sql", "true")
					.addAnnotatedClass(User.class)
					.addAnnotatedClass(File.class)
					.addAnnotatedClass(Event.class);
			
			sessionFactory = configuration.buildSessionFactory();
		}catch (Exception e){
			System.err.println("Не удалось создать экземпляр SessionFactory." + e);
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public static Session openSession(){
		return sessionFactory.openSession();
	}
}
