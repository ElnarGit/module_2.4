package org.elnar.crudapp.exception;

public class EventNotFoundException extends NotFoundException {

  public EventNotFoundException(Integer id) {
    super("Событие с идентификатором " + id + " не найдено.");
  }
}
