package org.elnar.crudapp.validator;

import static org.elnar.crudapp.util.JsonUtil.readJson;
import static org.elnar.crudapp.util.JsonUtil.writeJson;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ValidationUtil {
  private static final ValidatorFactory validatorFactory =
      Validation.buildDefaultValidatorFactory();
  private static final Validator validator = validatorFactory.getValidator();

  public static <T> T validateDTO(
      HttpServletRequest request, HttpServletResponse response, Class<T> clazz) throws IOException {
    try {
      T dto = readJson(request, clazz);

      ValidationResponse validationResponse = new ValidationResponse();
      validate(dto, validationResponse);

      if (validationResponse.getStatus() != 0) {
        response.setStatus(validationResponse.getStatus());
        writeJson(response, Map.of("error", validationResponse.getErrorMessage()));
        return null;
      }
      return dto;
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writeJson(response, Map.of("error", "Invalid request data"));
      return null;
    }
  }

  private static <T> void validate(T object, ValidationResponse response) {
    Set<ConstraintViolation<T>> violations = validator.validate(object);

    if (!violations.isEmpty()) {
      String errorMessage =
          violations.stream()
              .map(ConstraintViolation::getMessage)
              .collect(Collectors.joining(", "));

      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.setErrorMessage(errorMessage);
    }
  }
}
