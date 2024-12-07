package com.salpreh.products.application.usecases.tests.base;

import com.salpreh.products.application.usecases.tests.utils.Scripts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

@SpringBootTest
public class DbManualITTest {

  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");

  @DynamicPropertySource
  static void init(DynamicPropertyRegistry registry) {
    Startables.deepStart(postgreSQLContainer).join();

    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
  }

  @Autowired
  private JdbcTemplate jdbcTemplate;

  protected void loadData(String sqlScript) {
    String scriptContent = Scripts.loadScript(sqlScript);
    jdbcTemplate.execute(scriptContent);
  }
}
