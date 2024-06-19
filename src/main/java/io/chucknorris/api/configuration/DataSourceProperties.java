package io.chucknorris.api.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class DataSourceProperties {

    abstract String getDriverClassName();

    abstract String getPassword();

    abstract String getUrl();

    abstract String getUsername();
}
