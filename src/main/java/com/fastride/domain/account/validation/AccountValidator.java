package com.fastride.domain.account.validation;

import org.springframework.stereotype.Component;

import com.fastride.domain.account.model.Account;

@Component
public class AccountValidator {

	private ExistingAccountValidator existingAccountValidator;
	private EmailValidator emailValidator;
	private CarPlateValidator carPlateValidator;

	public AccountValidator(ExistingAccountValidator existingAccountValidator, EmailValidator emailValidator,
			CarPlateValidator carPlateValidator) {
		this.existingAccountValidator = existingAccountValidator;
		this.emailValidator = emailValidator;
		this.carPlateValidator = carPlateValidator;
	}

	public void validate(Account account) {
		// TODO: create validation for account type. If both driver and passenger are
		// false, throw an exception
		this.existingAccountValidator.validate(account.getEmail());
		this.emailValidator.validate(account.getEmail());
		if (account.isDriver()) {
			this.carPlateValidator.validate(account.getCarPlate());
		}
	}

}
