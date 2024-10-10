package com.fastride.domain.account.usecase;

import org.springframework.stereotype.Component;

import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.AccountRepository;
import com.fastride.domain.account.validation.AccountValidator;
import com.fastride.domain.shared.EntityId;

@Component
public class SignUpUseCase {

	private AccountRepository accountRepository;
	private AccountValidator accountValidator;

	public SignUpUseCase(AccountRepository accountRepository, AccountValidator accountValidator) {
		this.accountRepository = accountRepository;
		this.accountValidator = accountValidator;
	}

	public Account execute(Account account) {
		this.accountValidator.validate(account);
		account = new Account(new EntityId(), account);
		return this.accountRepository.create(account);
	}

}
