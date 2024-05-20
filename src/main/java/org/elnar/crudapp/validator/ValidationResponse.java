package org.elnar.crudapp.validator;

import lombok.Data;

@Data
public class ValidationResponse {
  private int status;
  private String errorMessage;
}
