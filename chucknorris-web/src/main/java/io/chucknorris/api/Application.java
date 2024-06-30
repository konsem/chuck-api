package io.chucknorris.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@ComponentScan(basePackages = { "io.chucknorris" })
@EnableJpaAuditing
@Slf4j
@SpringBootApplication
public class Application implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        event
                .getApplicationContext()
                .getBean(RequestMappingHandlerMapping.class)
                .getHandlerMethods()
                .keySet()
                .forEach(it -> log.info("Mapped endpoint: {}", it));
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
