package com.salpreh.products.tests.base;

import com.salpreh.products.tests.utils.Scripts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

// TODO: Common integration test configuration
public abstract class DbSpringbootITTest {

  // TODO: Create you testcontainers and configure them

  @Autowired
  private JdbcTemplate jdbcTemplate;

  protected void loadData(String sqlScript) {
    String scriptContent = Scripts.loadScript(sqlScript);
    jdbcTemplate.execute(scriptContent);
  }
}
