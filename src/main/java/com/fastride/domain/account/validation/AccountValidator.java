package com.fastride.domain.account.validation;

import org.springframework.stereotype.Component;

import com.fastride.domain.account.model.Account;

@Component
public class AccountValidator {

	private ExistingAccountValidator existingAccountValidator;

	public AccountValidator(ExistingAccountValidator existingAccountValidator) {
		this.existingAccountValidator = existingAccountValidator;
	}

	public void validate(Account account) {
		// TODO: create validation for account type. If both driver and passenger are
		// false, throw an exception
		this.existingAccountValidator.validate(account.getEmail().getContent());
	}

}
