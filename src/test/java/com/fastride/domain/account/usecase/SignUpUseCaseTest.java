package com.fastride.domain.account.usecase;

import static com.fastride.domain.shared.EntityId.VALID_ID_PATTERN;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.fastride.IntegrationTest;
import com.fastride.domain.shared.EntityId;
import com.fastride.domain.shared.ValidationException;

@IntegrationTest
class SignUpUseCaseTest {

	private static final String ACCOUNT_NAME = "John Doe";
	private static final String ACCOUNT_EMAIL = "john@example.com";
	private static final String ACCOUNT_CPF = "32421438098";
	private static final String DRIVER_ACCOUNT_CAR_PLATE = "ABC1234";

	@Autowired
	private SignUpUseCase signUpUseCase;

	@Autowired
	private GetAccountUseCase getAccountUseCase;

	@Test
	void shouldSignUpPassengerSuccessfully() {
		SignUpInput signUpInput = new SignUpInput(ACCOUNT_NAME, ACCOUNT_EMAIL, ACCOUNT_CPF, null, true, false);
		EntityId accountId = this.signUpUseCase.execute(signUpInput);
		GetAccountOutput retrievedAccount = this.getAccountUseCase.execute(accountId.toString());

		assertTrue(!Objects.isNull(accountId));
		assertTrue(Pattern.matches(VALID_ID_PATTERN, accountId.toString()));
		assertEquals(accountId.toString(), retrievedAccount.accountId());
		assertEquals(ACCOUNT_NAME, retrievedAccount.name());
		assertEquals(ACCOUNT_EMAIL, retrievedAccount.email());
		assertEquals(ACCOUNT_CPF, retrievedAccount.cpf());
		assertNull(retrievedAccount.carPlate());
		assertTrue(retrievedAccount.passenger());
		assertFalse(retrievedAccount.driver());
	}

	@Test
	void shouldSignUpDriverSuccessfully() {
		SignUpInput signUpInput = new SignUpInput(ACCOUNT_NAME, ACCOUNT_EMAIL, ACCOUNT_CPF, DRIVER_ACCOUNT_CAR_PLATE,
				false, true);
		EntityId accountId = this.signUpUseCase.execute(signUpInput);
		GetAccountOutput retrievedAccount = this.getAccountUseCase.execute(accountId.toString());

		assertTrue(!Objects.isNull(accountId));
		assertTrue(Pattern.matches(VALID_ID_PATTERN, accountId.toString()));
		assertEquals(accountId.toString(), retrievedAccount.accountId());
		assertEquals(ACCOUNT_NAME, retrievedAccount.name());
		assertEquals(ACCOUNT_EMAIL, retrievedAccount.email());
		assertEquals(ACCOUNT_CPF, retrievedAccount.cpf());
		assertEquals(DRIVER_ACCOUNT_CAR_PLATE, retrievedAccount.carPlate());
		assertFalse(retrievedAccount.passenger());
		assertTrue(retrievedAccount.driver());
	}

	@Test
	void shouldNotSignUpWhenAccountAlreadyExists() {
		SignUpInput signUpInput = new SignUpInput(ACCOUNT_NAME, ACCOUNT_EMAIL, ACCOUNT_CPF, null, true, false);
		this.signUpUseCase.execute(signUpInput);
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.signUpUseCase.execute(signUpInput);
		});

		assertEquals(
				"An account with the e-mail john@example.com already exists! Please, type another e-mail for creating a new account.",
				exception.getMessage());
	}

	@Test
	void shouldNotSignUpWhenEmailIsInvalid() {
		SignUpInput signUpInput = new SignUpInput(ACCOUNT_NAME, "john@", ACCOUNT_CPF, null, true, false);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.signUpUseCase.execute(signUpInput);
		});

		assertEquals("Invalid e-mail! Please, type a valid e-mail for signing up.", exception.getMessage());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "12345678910", "11111111111", "22222222222", "234bc" })
	void shouldNotSignUpWhenCpfIsInvalid(String cpf) {
		SignUpInput signUpInput = new SignUpInput(ACCOUNT_NAME, ACCOUNT_EMAIL, cpf, null, true, false);
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.signUpUseCase.execute(signUpInput);
		});

		assertEquals("Invalid CPF! Please, type a valid CPF for signing up.", exception.getMessage());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "John Smith$", "John 5", "4553" })
	void shouldNotSignUpWhenNameIsInvalid(String name) {
		SignUpInput signUpInput = new SignUpInput(name, ACCOUNT_EMAIL, ACCOUNT_CPF, null, true, false);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.signUpUseCase.execute(signUpInput);
		});

		assertEquals("Invalid name! The name should have only letters.", exception.getMessage());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "AA", "ABC-234", "AB-1234", "7896-ABC", "5462", "ABC" })
	void shouldNotSignUpDriverWhenCarPlateIsInvalid(String carPlate) {
		SignUpInput signUpInput = new SignUpInput(ACCOUNT_NAME, ACCOUNT_EMAIL, ACCOUNT_CPF, carPlate, false, true);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.signUpUseCase.execute(signUpInput);
		});

		assertEquals("Invalid car plate! Please, type a valid car plate with 3 letters and 4 numbers for signing up.",
				exception.getMessage());
	}

}
