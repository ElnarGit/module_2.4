package org.elnar.crudapp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@UtilityClass
public final class JsonUtil {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static void writeJson(HttpServletResponse response, Object object) throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    objectMapper.writeValue(response.getOutputStream(), object);
  }

  public static <T> T readJson(HttpServletRequest request, Class<T> clazz) throws IOException {
    return objectMapper.readValue(request.getInputStream(), clazz);
  }
}
