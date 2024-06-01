package org.elnar.crudapp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class JsonUtil {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  // Записывает объект в формате JSON в тело HTTP-ответа
  public static void writeObjectToJson(HttpServletResponse response, Object object)
      throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    objectMapper.writeValue(response.getOutputStream(), object);
  }

  // Читает JSON из тела HTTP-запроса и преобразует его в объект Java
  public static <T> T readJsonFromRequest(HttpServletRequest request, Class<T> clazz)
      throws IOException {
    return objectMapper.readValue(request.getInputStream(), clazz);
  }
}
