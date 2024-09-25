package com.fastride.domain;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = { SignupUseCaseTest.Initializer.class })
class SignupUseCaseTest {

	@Container
	public static PostgreSQLContainer<?> postgreSqlContainer = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("fast_ride_integration_tests_db").withUsername("").withPassword("");

	@Test
	void shouldSignUpSuccessfully() {
		fail("shouldSignUpSuccessfully");
	}

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues
					.of("spring.datasource.url=" + postgreSqlContainer.getJdbcUrl(),
							"spring.datasource.username=" + postgreSqlContainer.getUsername(),
							"spring.datasource.password=" + postgreSqlContainer.getPassword())
					.applyTo(configurableApplicationContext.getEnvironment());
		}
	}

}
