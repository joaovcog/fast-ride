package com.fastride.application.account;

import org.springframework.stereotype.Component;

import com.fastride.domain.account.AccountNotFoundException;
import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.AccountRepository;
import com.fastride.domain.shared.EntityId;

@Component
public class GetAccountUseCase {

	private AccountRepository accountRepository;

	public GetAccountUseCase(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public GetAccountOutput execute(String accountId) {
		Account account = this.accountRepository.findById(new EntityId(accountId)).orElseThrow(
				() -> new AccountNotFoundException(String.format("No account found for ID: %s", accountId)));
		return new GetAccountOutput(account.getAccountId().toString(), account.getName().getContent(),
				account.getEmail().getContent(), account.getCpf().getContent(), account.getCarPlateContent(),
				account.isPassenger(), account.isDriver());
	}

}
