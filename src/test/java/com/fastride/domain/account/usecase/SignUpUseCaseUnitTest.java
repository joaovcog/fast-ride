package com.fastride.domain.account.usecase;

import static com.fastride.domain.shared.EntityId.VALID_ID_PATTERN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

		Account account = AccountBuilder.getInstance().name("John Doe").email("john@example.com").cpf("32421438098")
				.carPlate(null).passenger().build();
		Account createdAccount = this.signUpUseCase.execute(account);
		Account retrievedAccount = this.getAccountUseCase.execute(createdAccount.getAccountId());

		assertTrue(!Objects.isNull(createdAccount));
		assertTrue(!Objects.isNull(createdAccount.getAccountId()));
		assertTrue(Pattern.matches(VALID_ID_PATTERN, createdAccount.getAccountId().toString()));
		assertAccount(createdAccount, retrievedAccount);
	}

	@Test
	void shouldSignUpPassengerSuccessfullyWithSpy() {
		this.signUpUseCase = new SignUpUseCase(this.accountRepositorySpy);
		this.getAccountUseCase = new GetAccountUseCase(this.accountRepositorySpy);

		Account account = AccountBuilder.getInstance().name("John Doe").email("john@example.com").cpf("32421438098")
				.carPlate(null).passenger().build();
		Account createdAccount = this.signUpUseCase.execute(account);
		Account retrievedAccount = this.getAccountUseCase.execute(createdAccount.getAccountId());

		assertTrue(!Objects.isNull(createdAccount));
		assertTrue(!Objects.isNull(createdAccount.getAccountId()));
		assertTrue(Pattern.matches(VALID_ID_PATTERN, createdAccount.getAccountId().toString()));
		assertAccount(createdAccount, retrievedAccount);
		verify(this.accountRepositorySpy, times(1)).create(any(Account.class));
		verify(this.accountRepositorySpy, times(1)).findById(any(EntityId.class));
	}

	@Test
	void shouldSignUpDriverSuccessfullyWithMock() {
		this.signUpUseCase = new SignUpUseCase(this.accountRepositoryMock);
		Account account = AccountBuilder.getInstance().accountId(UUID.randomUUID()).name("John Doe")
				.email("john@example.com").cpf("32421438098").carPlate("ABC1234").driver().build();
		when(this.accountRepositoryMock.create(any(Account.class))).thenReturn(account);

		Account createdAccount = this.signUpUseCase.execute(account);

		assertTrue(!Objects.isNull(createdAccount));
		assertTrue(!Objects.isNull(createdAccount.getAccountId()));
		assertTrue(Pattern.matches(VALID_ID_PATTERN, createdAccount.getAccountId().toString()));
		verify(this.accountRepositoryMock, times(1)).create(any(Account.class));
	}

	private void assertAccount(Account createdAccount, Account retrievedAccount) {
		assertEquals(createdAccount.getAccountId(), retrievedAccount.getAccountId());
		assertEquals(createdAccount.getName(), retrievedAccount.getName());
		assertEquals(createdAccount.getEmail(), retrievedAccount.getEmail());
		assertEquals(createdAccount.getCpf(), retrievedAccount.getCpf());
		assertEquals(createdAccount.getCarPlate(), retrievedAccount.getCarPlate());
		assertEquals(createdAccount.isPassenger(), retrievedAccount.isPassenger());
		assertEquals(createdAccount.isDriver(), retrievedAccount.isDriver());
	}

	private class AccountRepositoryFakeImpl implements AccountRepository {

		private List<Account> accounts;

		public AccountRepositoryFakeImpl() {
			this.accounts = new ArrayList<>();
		}

		@Override
		public Account create(Account account) {
			this.accounts.add(account);
			return account;
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
