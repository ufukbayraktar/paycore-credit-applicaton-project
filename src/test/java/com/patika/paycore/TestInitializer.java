package com.patika.paycore;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;

public class TestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final int POSTGRESQL_PORT = 5432;
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:13-alpine")
            .withUsername("sample-username")
            .withPassword("sample-password")
            .withInitScript("schema-test.sql")
            .withDatabaseName("test")
            .withExposedPorts(POSTGRESQL_PORT)
            .withEnv("TZ","UTC")
            .withReuse(true);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        postgreSQLContainer.start();
        String databaseHost = "DATABASE_URL=" + postgreSQLContainer.getJdbcUrl();
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,databaseHost);
    }
}
