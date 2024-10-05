package com.fastride;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class PostgresTestContainer {

	@Container
	public static PostgreSQLContainer<?> postgreSqlContainer = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("fast_ride_integration_tests_db");

	static {
		postgreSqlContainer.withUsername(System.getenv("POSTGRES_USER"))
				.withPassword(System.getenv("POSTGRES_PASSWORD"));
		postgreSqlContainer.start();
	}

	public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues
					.of("spring.datasource.url=" + postgreSqlContainer.getJdbcUrl(),
							"spring.datasource.username=" + postgreSqlContainer.getUsername(),
							"spring.datasource.password=" + postgreSqlContainer.getPassword())
					.applyTo(configurableApplicationContext.getEnvironment());
		}
	}

}
