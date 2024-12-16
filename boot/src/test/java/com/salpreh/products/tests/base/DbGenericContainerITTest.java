package com.salpreh.products.tests.base;

import com.salpreh.products.tests.utils.Scripts;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;

@DirtiesContext
@SpringBootTest
public class DbGenericContainerITTest {

  private static final String DB_NAME = "products";
  private static final String DB_USER = "root";
  private static final String DB_PASSWORD = "tests";

  @Autowired
  protected JdbcTemplate jdbcTemplate;

  static GenericContainer<?> postgreSQLContainer = new GenericContainer<>("postgres:17")
    .withExposedPorts(5432)
    .withEnv("POSTGRES_DB", DB_NAME)
    .withEnv("POSTGRES_USER", DB_USER)
    .withEnv("POSTGRES_PASSWORD", DB_PASSWORD)
    .waitingFor(new LogMessageWaitStrategy()
      .withRegEx(".*database system is ready to accept connections.*\\s")
      .withStartupTimeout(Duration.of(60, ChronoUnit.SECONDS))
    );

  @DynamicPropertySource
  static void initProperties(DynamicPropertyRegistry registry) {
    postgreSQLContainer.start();
    registry.add("spring.datasource.url", DbGenericContainerITTest::getJdbcUrl);
    registry.add("spring.datasource.username", () -> DB_USER);
    registry.add("spring.datasource.password", () -> DB_PASSWORD);
  }

  protected void loadData(String sqlScript) {
    String scriptContent = Scripts.loadScript(sqlScript);
    jdbcTemplate.execute(scriptContent);
  }

  private static String getJdbcUrl() {
    return "jdbc:postgresql://localhost:" + postgreSQLContainer.getMappedPort(5432) + "/" + DB_NAME;
  }
}
