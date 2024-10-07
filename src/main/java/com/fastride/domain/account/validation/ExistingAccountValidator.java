package com.fastride.domain.account.validation;

import com.fastride.domain.account.model.AccountRepository;
import com.fastride.domain.shared.ValidationException;

public class ExistingAccountValidator implements Validator {

	private AccountRepository accountRepository;

	public ExistingAccountValidator(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public void validate(String email) {
		if (accountRepository.findByEmail(email).isPresent())
			throw new ValidationException(String.format("An account with the e-mail %s already exists! "
					+ "Please, type another e-mail for creating a new account.", email));
	}

}
