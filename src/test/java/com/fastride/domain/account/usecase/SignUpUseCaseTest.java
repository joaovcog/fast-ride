package com.fastride.domain.account.usecase;

import static com.fastride.domain.shared.EntityId.VALID_ID_PATTERN;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Objects;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.fastride.IntegrationTest;
import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.AccountBuilder;
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

	private SignUpUseCase signUpUseCaseSpy;

	@Autowired
	private GetAccountUseCase getAccountUseCase;

	@Test
	void shouldSignUpPassengerSuccessfully() {
		Account account = AccountBuilder.getInstance().name(ACCOUNT_NAME).email(ACCOUNT_EMAIL).cpf(ACCOUNT_CPF)
				.passenger().build();
		EntityId accountId = this.signUpUseCase.execute(account);
		Account retrievedAccount = this.getAccountUseCase.execute(accountId);

		assertTrue(!Objects.isNull(accountId));
		assertTrue(Pattern.matches(VALID_ID_PATTERN, accountId.toString()));
		assertEquals(accountId, retrievedAccount.getAccountId());
		assertEquals(ACCOUNT_NAME, retrievedAccount.getName().getContent());
		assertEquals(ACCOUNT_EMAIL, retrievedAccount.getEmail().getContent());
		assertEquals(ACCOUNT_CPF, retrievedAccount.getCpf().getContent());
		assertNull(retrievedAccount.getCarPlate());
		assertTrue(retrievedAccount.isPassenger());
		assertFalse(retrievedAccount.isDriver());
	}

	@Test
	void shouldSignUpDriverSuccessfully() {
		Account account = AccountBuilder.getInstance().name(ACCOUNT_NAME).email(ACCOUNT_EMAIL).cpf(ACCOUNT_CPF)
				.carPlate(DRIVER_ACCOUNT_CAR_PLATE).driver().build();
		EntityId accountId = this.signUpUseCase.execute(account);
		Account retrievedAccount = this.getAccountUseCase.execute(accountId);

		assertTrue(!Objects.isNull(accountId));
		assertTrue(Pattern.matches(VALID_ID_PATTERN, accountId.toString()));
		assertEquals(accountId, retrievedAccount.getAccountId());
		assertEquals(ACCOUNT_NAME, retrievedAccount.getName().getContent());
		assertEquals(ACCOUNT_EMAIL, retrievedAccount.getEmail().getContent());
		assertEquals(ACCOUNT_CPF, retrievedAccount.getCpf().getContent());
		assertEquals(DRIVER_ACCOUNT_CAR_PLATE, retrievedAccount.getCarPlate().getContent());
		assertFalse(retrievedAccount.isPassenger());
		assertTrue(retrievedAccount.isDriver());
	}

	@Test
	void shouldNotSignUpWhenAccountAlreadyExists() {
		Account account = AccountBuilder.getInstance().name(ACCOUNT_NAME).email(ACCOUNT_EMAIL).cpf(ACCOUNT_CPF)
				.passenger().build();
		this.signUpUseCase.execute(account);
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.signUpUseCase.execute(account);
		});

		assertEquals(
				"An account with the e-mail john@example.com already exists! Please, type another e-mail for creating a new account.",
				exception.getMessage());
	}

	@Test
	void shouldNotSignUpWhenEmailIsInvalid() {
		this.signUpUseCaseSpy = Mockito.spy(this.signUpUseCase);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			Account account = AccountBuilder.getInstance().name(ACCOUNT_NAME).email("john@").cpf(ACCOUNT_CPF)
					.carPlate(null).passenger().build();
			this.signUpUseCase.execute(account);
		});

		assertEquals("Invalid e-mail! Please, type a valid e-mail for signing up.", exception.getMessage());
		verify(this.signUpUseCaseSpy, never()).execute(any(Account.class));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "12345678910", "11111111111", "22222222222", "234bc" })
	void shouldNotSignUpWhenCpfIsInvalid(String cpf) {
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			Account account = AccountBuilder.getInstance().name(ACCOUNT_NAME).email(ACCOUNT_EMAIL).cpf(cpf)
					.carPlate(null).passenger().build();
			this.signUpUseCase.execute(account);
		});

		assertEquals("Invalid CPF! Please, type a valid CPF for signing up.", exception.getMessage());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "John Smith$", "John 5", "4553" })
	void shouldNotSignUpWhenNameIsInvalid(String name) {
		this.signUpUseCaseSpy = Mockito.spy(this.signUpUseCase);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			Account account = AccountBuilder.getInstance().name(name).email(ACCOUNT_EMAIL).cpf(ACCOUNT_CPF)
					.carPlate(null).passenger().build();
			this.signUpUseCaseSpy.execute(account);
		});

		assertEquals("Invalid name! The name should have only letters.", exception.getMessage());
		verify(this.signUpUseCaseSpy, never()).execute(any(Account.class));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "AA", "ABC-234", "AB-1234", "7896-ABC", "5462", "ABC" })
	void shouldNotSignUpDriverWhenCarPlateIsInvalid(String carPlate) {
		this.signUpUseCaseSpy = Mockito.spy(this.signUpUseCase);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			Account account = AccountBuilder.getInstance().name(ACCOUNT_NAME).email(ACCOUNT_EMAIL).cpf(ACCOUNT_CPF)
					.carPlate(carPlate).driver().build();
			this.signUpUseCase.execute(account);
		});

		assertEquals("Invalid car plate! Please, type a valid car plate with 3 letters and 4 numbers for signing up.",
				exception.getMessage());
		verify(this.signUpUseCaseSpy, never()).execute(any(Account.class));
	}

}
