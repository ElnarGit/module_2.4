package org.elnar.crudapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import org.flywaydb.core.Flyway;

public class DatabaseMigrator {
  public static void main(String[] args) throws IOException {
    final String PROPERTIES_FILE = "src/main/resources/flyway.properties";

    Properties properties = new Properties();
    properties.load(Files.newInputStream(Paths.get(PROPERTIES_FILE)));

    String url = properties.getProperty("flyway.url");
    String username = properties.getProperty("flyway.username");
    String password = properties.getProperty("flyway.password");
    String locations = properties.getProperty("flyway.locations");

    Flyway flyway =
        Flyway.configure().dataSource(url, username, password).locations(locations).load();

    flyway.migrate();
  }
}
