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

@DirtiesContext
@SpringBootTest
public abstract class BaseDynamicPropsITTest {

  // TODO: Add containers (@ServiceConnection not available for Kafka)

  @DynamicPropertySource
  static void initProperties(DynamicPropertyRegistry registry) {
    // TODO: Add properties to registry
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

    try (var admin = AdminClient.create(Map.of(BOOTSTRAP_SERVERS_CONFIG, "TODO: bootstrap servers"))) {
      admin.createTopics(newTopics);
    }
  }

  protected static void deleteTopics(List<String> topics) {
    try (var admin = AdminClient.create(Map.of(BOOTSTRAP_SERVERS_CONFIG, "TODO: bootstrap servers"))) {
      admin.deleteTopics(topics);
    }
  }

  protected  <V> KafkaConsumer<String, V> createConsumer(Class<V> valueClass) {
    return new KafkaConsumer<>(
      Map.of(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "TODO: bootstrap servers",
        ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString(),
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
      ),
      new StringDeserializer(),
      new JsonStringDeserializer<>(valueClass, objectMapper)
    );
  }
}
