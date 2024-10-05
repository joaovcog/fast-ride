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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fastride.PostgresTestContainer;

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = { PostgresTestContainer.Initializer.class })
@Transactional
class SignUpUseCaseTest {

	private static final Pattern UUID_PATTERN = Pattern
			.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

	@Autowired
	private SignUpUseCase signUpUseCase;

	@Test
	void shouldSignUpPassengerSuccessfully() {
		Object input = new Object[] { "John Doe", true, "john@example.com", "32421438098", null, true, false };
		Object objectWithAccountId = this.signUpUseCase.signUp(input);
		Object[] arrayFromObject = (Object[]) objectWithAccountId;

		assertTrue(!Objects.isNull(objectWithAccountId));
		assertTrue(arrayFromObject.length == 2);
		assertEquals("accountId", arrayFromObject[0]);
		assertTrue(UUID_PATTERN.matcher(((UUID) arrayFromObject[1]).toString()).matches());
	}

	@Test
	void shouldSignUpDriverSuccessfully() {
		Object input = new Object[] { "John Doe", true, "john@example.com", "32421438098", "ABC1234", false, true };
		Object objectWithAccountId = this.signUpUseCase.signUp(input);
		Object[] arrayFromObject = (Object[]) objectWithAccountId;

		assertTrue(!Objects.isNull(objectWithAccountId));
		assertTrue(arrayFromObject.length == 2);
		assertEquals("accountId", arrayFromObject[0]);
		assertTrue(UUID_PATTERN.matcher(((UUID) arrayFromObject[1]).toString()).matches());
	}

	@Test
	void shouldNotSignUpWhenAccountAlreadyExists() {
		Object input = new Object[] { "John Doe", true, "john@example.com", "32421438098", null, true, false };
		this.signUpUseCase.signUp(input);
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.signUpUseCase.signUp(input);
		});

		assertEquals(
				"An account with the e-mail john@example.com already exists! Please, type another e-mail for creating a new account.",
				exception.getMessage());
	}

	@Test
	void shouldNotSignUpWhenEmailIsInvalid() {
		Object input = new Object[] { "John Doe", true, "john@", "12345678901", null, true, false };

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.signUpUseCase.signUp(input);
		});

		assertEquals("Invalid e-mail! Please, type a valid e-mail for signing up.", exception.getMessage());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "12345678910", "11111111111", "22222222222", "234bc" })
	void shouldNotSignUpWhenCpfIsInvalid(String cpf) {
		Object input = new Object[] { "John Doe", true, "john@example.com", cpf, null, true, false };

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.signUpUseCase.signUp(input);
		});

		assertEquals("Invalid CPF! Please, type a valid CPF for signing up.", exception.getMessage());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "John Smith$", "John 5", "4553" })
	void shouldNotSignUpWhenNameIsInvalid(String name) {
		Object input = new Object[] { name, true, "john@example.com", "12345678901", null, true, false };

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.signUpUseCase.signUp(input);
		});

		assertEquals("Invalid name! The name should have only letters.", exception.getMessage());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "AA", "ABC-234", "AB-1234", "7896-ABC", "5462", "ABC" })
	void shouldNotSignUpDriverWhenCarPlateIsInvalid(String carPlate) {
		Object input = new Object[] { "John Doe", true, "john@example.com", "32421438098", carPlate, false, true };

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.signUpUseCase.signUp(input);
		});

		assertEquals("Invalid car plate! Please, type a valid car plate with 3 letters and 4 numbers for signing up.",
				exception.getMessage());
	}

}
