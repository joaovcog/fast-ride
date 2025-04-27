package com.fastride.domain.account.validation;

import org.springframework.stereotype.Component;

import com.fastride.domain.account.model.Account;

@Component
public class AccountValidator {

	private ExistingAccountValidator existingAccountValidator;
	private CarPlateValidator carPlateValidator;

	public AccountValidator(ExistingAccountValidator existingAccountValidator, CarPlateValidator carPlateValidator) {
		this.existingAccountValidator = existingAccountValidator;
		this.carPlateValidator = carPlateValidator;
	}

	public void validate(Account account) {
		// TODO: create validation for account type. If both driver and passenger are
		// false, throw an exception
		this.existingAccountValidator.validate(account.getEmail().getContent());
		if (account.isDriver()) {
			this.carPlateValidator.validate(account.getCarPlate());
		}
	}

}
