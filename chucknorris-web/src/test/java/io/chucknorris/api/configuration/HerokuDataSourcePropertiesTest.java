package io.chucknorris.api.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HerokuDataSourcePropertiesTest {

    @Test
    void testShouldCreateDataSourcePostgres() {
        // given:
        String driverClassName = "org.postgresql.Driver";

        // and:
        String uri = "postgres://username:password@ec2-00-000-00-000.eu-west-1.compute.amazonaws.com:5432/d409qm4ujtafvk";

        // when:
        HerokuDataSourceProperties actual = new HerokuDataSourceProperties(driverClassName, uri);

        // then:
        Assertions.assertEquals("org.postgresql.Driver", actual.getDriverClassName());
        Assertions.assertEquals("password", actual.getPassword());
        Assertions.assertEquals("jdbc:postgresql://ec2-00-000-00-000.eu-west-1.compute.amazonaws.com:5432/d409qm4ujtafvk", actual.getUrl());
        Assertions.assertEquals("username", actual.getUsername());
    }
}
