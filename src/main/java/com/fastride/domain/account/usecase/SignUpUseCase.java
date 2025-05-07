package com.fastride.domain.account.usecase;

import org.springframework.stereotype.Component;

import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.Account.AccountBuilder;
import com.fastride.domain.account.model.AccountRepository;
import com.fastride.domain.shared.EntityId;
import com.fastride.domain.shared.ValidationException;

@Component
public class SignUpUseCase {

	private AccountRepository accountRepository;

	public SignUpUseCase(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public EntityId execute(SignUpInput signUpInput) {
		this.validateExistingAccount(signUpInput.email());
		Account account = AccountBuilder.getInstance().name(signUpInput.name()).email(signUpInput.email())
				.cpf(signUpInput.cpf()).passenger(signUpInput.passenger()).driver(signUpInput.driver())
				.carPlate(signUpInput.carPlate()).build();
		this.accountRepository.create(account);
		return account.getAccountId();
	}

	private void validateExistingAccount(String email) {
		if (accountRepository.findByEmail(email).isPresent())
			throw new ValidationException(String.format("An account with the e-mail %s already exists! "
					+ "Please, type another e-mail for creating a new account.", email));
	}

}
