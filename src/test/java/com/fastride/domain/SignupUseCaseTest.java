package com.fastride.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = { SignupUseCaseTest.Initializer.class })
@Transactional
class SignupUseCaseTest {

	@Container
	public static PostgreSQLContainer<?> postgreSqlContainer = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("fast_ride_integration_tests_db").withUsername("").withPassword("");

	@Autowired
	private SignupUseCase signupUseCase;

	@AfterEach
	public void tearDown() throws SQLException {
		String url = "jdbc:postgresql://localhost:5432/fast_ride_db";
		String user = "";
		String password = "";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				Statement statement = connection.createStatement();) {
			statement.executeUpdate("delete from fast_ride.account");
		}
	}

	@Test
	void shouldSignUpSuccessfully() {
		Object input = new Object[] { "John Doe", true, "john22@example.com", "32421438098", null, true, false };
		Object objectWithAccountId = signupUseCase.signup(input);
		Object[] arrayFromObject = (Object[]) objectWithAccountId;
		Pattern UUID_REGEX = Pattern
				.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

		assertTrue(!Objects.isNull(objectWithAccountId));
		assertTrue(arrayFromObject.length == 2);
		assertEquals("accountId", arrayFromObject[0]);
		assertTrue(UUID_REGEX.matcher(((UUID) arrayFromObject[1]).toString()).matches());
	}

	@Test
	void shouldSignUpDriverSuccessfully() {
		Object input = new Object[] { "John Doe", true, "joh23n@example.com", "32421438098", "ABC1234", false, true };
		Object objectWithAccountId = signupUseCase.signup(input);
		Object[] arrayFromObject = (Object[]) objectWithAccountId;
		Pattern UUID_REGEX = Pattern
				.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

		assertTrue(!Objects.isNull(objectWithAccountId));
		assertTrue(arrayFromObject.length == 2);
		assertEquals("accountId", arrayFromObject[0]);
		assertTrue(UUID_REGEX.matcher(((UUID) arrayFromObject[1]).toString()).matches());
	}

	@Test
	void shouldNotSignUpWhenAccountAlreadyExists() {
		Object input = new Object[] { "John Doe", true, "john@example.com", "32421438098", null, true, false };
		Object signupResult = signupUseCase.signup(input);
		signupResult = signupUseCase.signup(input);

		assertEquals(-4, signupResult);
	}

	@Test
	void shouldNotSignUpWhenEmailIsInvalid() {
		Object input = new Object[] { "John Doe", true, "john@", "12345678901", null, true, false };
		Object signupResult = signupUseCase.signup(input);

		assertEquals(-2, signupResult);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "12345678910", "11111111111", "22222222222", "234bc" })
	void shouldNotSignUpWhenCpfIsInvalid(String cpf) {
		Object input = new Object[] { "John Doe", true, "john@example.com", cpf, null, true, false };
		Object signupResult = signupUseCase.signup(input);

		assertEquals(-1, signupResult);
	}

	@Test
	void shouldNotSignUpWhenNameIsInvalid() {
		Object input = new Object[] { "", true, "john@example.com", "12345678901", null, true, false };
		Object signupResult = signupUseCase.signup(input);

		assertEquals(-3, signupResult);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "AA", "ABC-234", "AB-1234", "7896-ABC", "5462", "ABC" })
	void shouldNotSignUpDriverWhenCarPlateIsInvalid(String carPlate) {
		Object input = new Object[] { "John Doe", true, "john@example.com", "32421438098", carPlate, false, true };
		Object signupResult = signupUseCase.signup(input);

		assertEquals(-5, signupResult);
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
