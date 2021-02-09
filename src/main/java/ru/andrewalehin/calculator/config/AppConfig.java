package ru.andrewalehin.calculator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.andrewalehin.calculator.util.JsonUtil;

@Configuration
@EnableCaching
public class AppConfig {

  @Bean(initMethod = "start", destroyMethod = "stop")
  Server h2Server() throws SQLException {
    return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "8085");
  }

  @Autowired
  public void storeObjectMapper(ObjectMapper objectMapper) {
    JsonUtil.setMapper(objectMapper);
  }
}