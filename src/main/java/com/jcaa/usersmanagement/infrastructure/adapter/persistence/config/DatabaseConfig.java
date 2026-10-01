package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

public record DatabaseConfig(
    String type, String host, int port, String databaseName, String username, String password) {
  private static final String URL_TEMPLATE_MYSQL =
      "jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
  private static final String URL_TEMPLATE_POSTGRES =
      "jdbc:postgresql://%s:%d/%s";

  public String buildJdbcUrl() {
    if ("postgres".equalsIgnoreCase(type) || "postgresql".equalsIgnoreCase(type)) {
      return String.format(URL_TEMPLATE_POSTGRES, host, port, databaseName);
    }
    return String.format(URL_TEMPLATE_MYSQL, host, port, databaseName);
  }
}
