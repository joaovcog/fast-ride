package com.fastride.domain.account.usecase;

import org.springframework.stereotype.Component;

import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.AccountRepository;
import com.fastride.domain.account.model.Email;
import com.fastride.domain.shared.EntityId;
import com.fastride.domain.shared.ValidationException;

@Component
public class SignUpUseCase {

	private AccountRepository accountRepository;

	public SignUpUseCase(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public EntityId execute(Account account) {
		this.validateExistingAccount(account.getEmail());
		account = new Account(new EntityId(), account);
		this.accountRepository.create(account);
		return account.getAccountId();
	}

	private void validateExistingAccount(Email email) {
		if (accountRepository.findByEmail(email.getContent()).isPresent())
			throw new ValidationException(String.format("An account with the e-mail %s already exists! "
					+ "Please, type another e-mail for creating a new account.", email.getContent()));
	}

}
