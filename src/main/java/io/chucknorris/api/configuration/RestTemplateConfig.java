package io.chucknorris.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  public @Bean RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
