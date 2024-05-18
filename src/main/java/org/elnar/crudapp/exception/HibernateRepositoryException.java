package org.elnar.crudapp.exception;

public class HibernateRepositoryException extends RuntimeException {

  public HibernateRepositoryException(String message, Throwable cause) {
    super(message, cause);
  }
}
