package com.fastride.domain.account.model;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.fastride.domain.account.model.Account.AccountBuilder;
import com.fastride.domain.shared.ValidationException;

class AccountBuilderTest {

	private static final String NAME = "John Doe";
	private static final String EMAIL = "john@email.com";
	private static final String CPF = "32421438098";
	private static final String CAR_PLATE = "ABC1234";
	private static final String INVALID_CAR_PLATE_EXCEPTION_MESSAGE = "Invalid car plate! Please, type a valid car plate with 3 letters and 4 numbers for signing up.";

	@Test
	void shouldCreateAccountObjectForPassengerSuccessfully() {
		UUID uuidForAccountId = UUID.randomUUID();
		Account account = AccountBuilder.getInstance().accountId(uuidForAccountId.toString()).name(NAME).email(EMAIL)
				.cpf(CPF).passenger(true).build();
		assertAccountInfo(uuidForAccountId, account);
		assertPassenger(account);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "A", "AAA-1AB2" })
	void shouldCreateAccountObjectForPassengerSuccessfullyEvenIfProvidedNullOrInvalidCarPlate(String invalidCarPlate) {
		UUID uuidForAccountId = UUID.randomUUID();
		Account account = AccountBuilder.getInstance().accountId(uuidForAccountId.toString()).name(NAME).email(EMAIL)
				.cpf(CPF).passenger(true).carPlate(invalidCarPlate).build();
		assertAccountInfo(uuidForAccountId, account);
		assertPassenger(account);
	}

	@Test
	void shouldCreateAccountObjectForDriverSuccessfully() {
		UUID uuidForAccountId = UUID.randomUUID();
		Account account = AccountBuilder.getInstance().accountId(uuidForAccountId.toString()).name(NAME).email(EMAIL)
				.cpf(CPF).driver(true).carPlate(CAR_PLATE).build();
		assertAccountInfo(uuidForAccountId, account);
		assertDriver(account);
	}

	@Test
	void shouldNotCreateAccountObjectForDriverWhenCarPlateNotProvided() {
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			UUID uuidForAccountId = UUID.randomUUID();
			AccountBuilder.getInstance().accountId(uuidForAccountId.toString()).name(NAME).email(EMAIL).cpf(CPF)
					.driver(true).build();
		});
		assertEquals(INVALID_CAR_PLATE_EXCEPTION_MESSAGE, exception.getMessage());
	}

	private void assertAccountInfo(UUID uuidForAccountId, Account account) {
		assertTrue(!Objects.isNull(account));
		assertTrue(!Objects.isNull(account.getAccountId()));
		assertEquals(uuidForAccountId, account.getAccountId().toUUID());
		assertEquals(NAME, account.getName().getContent());
		assertEquals(EMAIL, account.getEmail().getContent());
		assertEquals(CPF, account.getCpf().getContent());
	}

	private void assertPassenger(Account account) {
		assertNull(account.getCarPlate());
		assertTrue(account.isPassenger());
		assertFalse(account.isDriver());
	}

	private void assertDriver(Account account) {
		assertEquals(CAR_PLATE, account.getCarPlate().getContent());
		assertFalse(account.isPassenger());
		assertTrue(account.isDriver());
	}

}
