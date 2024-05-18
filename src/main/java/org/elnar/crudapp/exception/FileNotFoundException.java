package org.elnar.crudapp.exception;

public class FileNotFoundException extends NotFoundException {

  public FileNotFoundException(Integer id) {
    super("Файл с идентификатором " + id + " не найден.");
  }
}
