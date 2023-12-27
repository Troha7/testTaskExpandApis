package com.expandapis.config;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MySQLContainer;

/**
 * {@link MySqlTestContainerConfig}
 *
 * @author Dmytro Trotsenko on 12/26/23
 */
public class MySqlTestContainerConfig implements Extension, BeforeAllCallback, AfterAllCallback {

  public static final String MYSQL_IMAGE = "mysql:8.2.0";
  public static final String JDBC_URL = "JDBC_URL";
  public static final String MYSQL_USERNAME = "MYSQL_USERNAME";
  public static final String MYSQL_PASSWORD = "MYSQL_PASSWORD";
  private static final MySQLContainer<?> container = new MySQLContainer<>(MYSQL_IMAGE)
      .withEnv("TZ", "UTC");

  @Override
  public void beforeAll(ExtensionContext extensionContext) {
    container.start();
    System.setProperty(JDBC_URL, container.getJdbcUrl());
    System.setProperty(MYSQL_USERNAME, container.getUsername());
    System.setProperty(MYSQL_PASSWORD, container.getPassword());
  }

  @Override
  public void afterAll(ExtensionContext extensionContext) {
    //should do nothing, testcontainers will shut down the container
  }
}
