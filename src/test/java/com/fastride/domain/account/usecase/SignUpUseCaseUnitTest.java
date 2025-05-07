package com.fastride.domain.account.usecase;

import static com.fastride.domain.shared.EntityId.VALID_ID_PATTERN;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.AccountBuilder;
import com.fastride.domain.account.model.AccountRepository;
import com.fastride.domain.shared.EntityId;

@ExtendWith(MockitoExtension.class)
class SignUpUseCaseUnitTest {

	private static final String ACCOUNT_NAME = "John Doe";
	private static final String ACCOUNT_EMAIL = "john@example.com";
	private static final String ACCOUNT_CPF = "32421438098";
	private static final String DRIVER_ACCOUNT_CAR_PLATE = "ABC1234";

	private SignUpUseCase signUpUseCase;
	private GetAccountUseCase getAccountUseCase;

	@Mock
	private AccountRepository accountRepositoryMock;

	@Spy
	private AccountRepositoryFakeImpl accountRepositorySpy;

	private AccountRepository accountRepositoryFake;

	@BeforeEach
	public void setup() {
		this.accountRepositoryFake = new AccountRepositoryFakeImpl();
	}

	@Test
	void shouldSignUpPassengerSuccessfullyWithFake() {
		this.signUpUseCase = new SignUpUseCase(this.accountRepositoryFake);
		this.getAccountUseCase = new GetAccountUseCase(this.accountRepositoryFake);

		Account account = AccountBuilder.getInstance().name(ACCOUNT_NAME).email(ACCOUNT_EMAIL).cpf(ACCOUNT_CPF)
				.carPlate(null).passenger().build();
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
	void shouldSignUpPassengerSuccessfullyWithSpy() {
		this.signUpUseCase = new SignUpUseCase(this.accountRepositorySpy);
		this.getAccountUseCase = new GetAccountUseCase(this.accountRepositorySpy);

		Account account = AccountBuilder.getInstance().name(ACCOUNT_NAME).email(ACCOUNT_EMAIL).cpf(ACCOUNT_CPF)
				.carPlate(null).passenger().build();
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
		verify(this.accountRepositorySpy, times(1)).create(any(Account.class));
		verify(this.accountRepositorySpy, times(1)).findById(any(EntityId.class));
	}

	@Test
	void shouldCreateAccountForDriverSuccessfullyWithMock() {
		this.signUpUseCase = new SignUpUseCase(this.accountRepositoryMock);
		Account account = AccountBuilder.getInstance().accountId(UUID.randomUUID()).name(ACCOUNT_NAME)
				.email(ACCOUNT_EMAIL).cpf(ACCOUNT_CPF).carPlate(DRIVER_ACCOUNT_CAR_PLATE).driver().build();

		EntityId accountId = this.signUpUseCase.execute(account);

		assertTrue(!Objects.isNull(accountId));
		assertTrue(Pattern.matches(VALID_ID_PATTERN, accountId.toString()));
		verify(this.accountRepositoryMock, times(1)).create(any(Account.class));
	}

	private class AccountRepositoryFakeImpl implements AccountRepository {

		private List<Account> accounts;

		public AccountRepositoryFakeImpl() {
			this.accounts = new ArrayList<>();
		}

		@Override
		public void create(Account account) {
			this.accounts.add(account);
		}

		@Override
		public Optional<Account> findById(EntityId accountId) {
			return this.accounts.stream().filter(account -> accountId.equals(account.getAccountId())).findFirst();
		}

		@Override
		public Optional<Account> findByEmail(String email) {
			return this.accounts.stream().filter(account -> email.equals(account.getEmail())).findFirst();
		}

	}

}
