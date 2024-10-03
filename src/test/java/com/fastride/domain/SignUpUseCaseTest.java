package com.fastride.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

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
@ContextConfiguration(initializers = { SignUpUseCaseTest.Initializer.class })
@Transactional
class SignUpUseCaseTest {

	@Container
	public static PostgreSQLContainer<?> postgreSqlContainer = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("fast_ride_integration_tests_db").withUsername("").withPassword("");

	@Autowired
	private SignUpUseCase signUpUseCase;

	@Test
	void shouldSignUpPassengerSuccessfully() {
		Object input = new Object[] { "John Doe", true, "john@example.com", "32421438098", null, true, false };
		Object objectWithAccountId = signUpUseCase.signUp(input);
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
		Object input = new Object[] { "John Doe", true, "john@example.com", "32421438098", "ABC1234", false, true };
		Object objectWithAccountId = signUpUseCase.signUp(input);
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
		signUpUseCase.signUp(input);
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			signUpUseCase.signUp(input);
		});

		assertEquals(
				"An account with the e-mail john@example.com already exists! Please, type another e-mail for creating a new account.",
				exception.getMessage());
	}

	@Test
	void shouldNotSignUpWhenEmailIsInvalid() {
		Object input = new Object[] { "John Doe", true, "john@", "12345678901", null, true, false };

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			signUpUseCase.signUp(input);
		});

		assertEquals("Invalid e-mail! Please, type a valid e-mail for signing up.", exception.getMessage());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "12345678910", "11111111111", "22222222222", "234bc" })
	void shouldNotSignUpWhenCpfIsInvalid(String cpf) {
		Object input = new Object[] { "John Doe", true, "john@example.com", cpf, null, true, false };
		Object signUpResult = signUpUseCase.signUp(input);

		assertEquals(-1, signUpResult);
	}

	@Test
	void shouldNotSignUpWhenNameIsInvalid() {
		Object input = new Object[] { "", true, "john@example.com", "12345678901", null, true, false };
		Object signUpResult = signUpUseCase.signUp(input);

		assertEquals(-3, signUpResult);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "AA", "ABC-234", "AB-1234", "7896-ABC", "5462", "ABC" })
	void shouldNotSignUpDriverWhenCarPlateIsInvalid(String carPlate) {
		Object input = new Object[] { "John Doe", true, "john@example.com", "32421438098", carPlate, false, true };
		Object signUpResult = signUpUseCase.signUp(input);

		assertEquals(-5, signUpResult);
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
