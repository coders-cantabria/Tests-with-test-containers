package com.salpreh.products.tests.base;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salpreh.products.config.serializers.JsonStringDeserializer;
import com.salpreh.products.tests.utils.Scripts;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;

@Testcontainers
@DirtiesContext
@SpringBootTest
public abstract class BaseDynamicPropsITTest {

  @Container
  static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");

  @Container
  static ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.4.3");

  @DynamicPropertySource
  static void initProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);

    registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
  }

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  protected void loadData(String sqlScript) {
    String scriptContent = Scripts.loadScript(sqlScript);
    jdbcTemplate.execute(scriptContent);
  }

  protected static void createTopics(List<String> topics) {
    List<NewTopic> newTopics =
      topics.stream()
        .map(topic -> new NewTopic(topic, 1, (short) 1))
        .toList();

    try (var admin = AdminClient.create(Map.of(BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers()))) {
      admin.createTopics(newTopics);
    }
  }

  protected static void deleteTopics(List<String> topics) {
    try (var admin = AdminClient.create(Map.of(BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers()))) {
      admin.deleteTopics(topics);
    }
  }

  protected  <V> KafkaConsumer<String, V> createConsumer(Class<V> valueClass) {
    return new KafkaConsumer<>(
      Map.of(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers(),
        ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString(),
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
      ),
      new StringDeserializer(),
      new JsonStringDeserializer<>(valueClass, objectMapper)
    );
  }
}
