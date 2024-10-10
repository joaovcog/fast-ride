package com.fastride.domain.account.usecase;

import org.springframework.stereotype.Component;

import com.fastride.domain.account.exception.AccountNotFoundException;
import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.AccountRepository;
import com.fastride.domain.shared.EntityId;

@Component
public class GetAccountUseCase {

	private AccountRepository accountRepository;

	public GetAccountUseCase(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public Account execute(EntityId accountId) {
		return this.accountRepository.findById(accountId).orElseThrow(
				() -> new AccountNotFoundException(String.format("No account found for ID: %s", accountId)));
	}

}
