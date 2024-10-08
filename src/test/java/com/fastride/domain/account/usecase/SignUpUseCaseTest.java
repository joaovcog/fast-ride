package com.fastride.domain.account.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fastride.PostgresTestContainer;
import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.AccountBuilder;
import com.fastride.domain.shared.ValidationException;

@Testcontainers
@SpringBootTest
@Transactional
class SignUpUseCaseTest extends PostgresTestContainer {

	private static final Pattern UUID_PATTERN = Pattern
			.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

	@Autowired
	private SignUpUseCase signUpUseCase;

	@BeforeAll
	static void beforeAll() {
		postgreSqlContainer.start();
	}

	@AfterAll
	static void afterAll() {
		postgreSqlContainer.stop();
	}

	@Test
	void shouldSignUpPassengerSuccessfully() {
		Account account = AccountBuilder.getInstance().name("John Doe").email("john@example.com").cpf("32421438098")
				.carPlate(null).passenger().build();
		Account createdAccount = this.signUpUseCase.signUp(account);

		assertTrue(!Objects.isNull(createdAccount));
		assertTrue(!Objects.isNull(createdAccount.getAccountId()));
		assertTrue(UUID_PATTERN.matcher(createdAccount.getAccountId().toString()).matches());
	}

	@Test
	void shouldSignUpDriverSuccessfully() {
		Account account = AccountBuilder.getInstance().name("John Doe").email("john@example.com").cpf("32421438098")
				.carPlate("ABC1234").driver().build();
		Account createdAccount = this.signUpUseCase.signUp(account);

		assertTrue(!Objects.isNull(createdAccount));
		assertTrue(!Objects.isNull(createdAccount.getAccountId()));
		assertTrue(UUID_PATTERN.matcher(createdAccount.getAccountId().toString()).matches());
	}

	@Test
	void shouldNotSignUpWhenAccountAlreadyExists() {
		Account account = AccountBuilder.getInstance().name("John Doe").email("john@example.com").cpf("32421438098")
				.carPlate(null).passenger().build();
		this.signUpUseCase.signUp(account);
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.signUpUseCase.signUp(account);
		});

		assertEquals(
				"An account with the e-mail john@example.com already exists! Please, type another e-mail for creating a new account.",
				exception.getMessage());
	}

	@Test
	void shouldNotSignUpWhenEmailIsInvalid() {
		Account account = AccountBuilder.getInstance().name("John Doe").email("john@").cpf("32421438098").carPlate(null)
				.passenger().build();

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.signUpUseCase.signUp(account);
		});

		assertEquals("Invalid e-mail! Please, type a valid e-mail for signing up.", exception.getMessage());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "12345678910", "11111111111", "22222222222", "234bc" })
	void shouldNotSignUpWhenCpfIsInvalid(String cpf) {
		Account account = AccountBuilder.getInstance().name("John Doe").email("john@example.com").cpf(cpf)
				.carPlate(null).passenger().build();

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.signUpUseCase.signUp(account);
		});

		assertEquals("Invalid CPF! Please, type a valid CPF for signing up.", exception.getMessage());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "John Smith$", "John 5", "4553" })
	void shouldNotSignUpWhenNameIsInvalid(String name) {
		Account account = AccountBuilder.getInstance().name(name).email("john@example.com").cpf("32421438098")
				.carPlate(null).passenger().build();

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.signUpUseCase.signUp(account);
		});

		assertEquals("Invalid name! The name should have only letters.", exception.getMessage());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "AA", "ABC-234", "AB-1234", "7896-ABC", "5462", "ABC" })
	void shouldNotSignUpDriverWhenCarPlateIsInvalid(String carPlate) {
		Account account = AccountBuilder.getInstance().name("John Doe").email("john@example.com").cpf("32421438098")
				.carPlate(carPlate).driver().build();

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.signUpUseCase.signUp(account);
		});

		assertEquals("Invalid car plate! Please, type a valid car plate with 3 letters and 4 numbers for signing up.",
				exception.getMessage());
	}

}
