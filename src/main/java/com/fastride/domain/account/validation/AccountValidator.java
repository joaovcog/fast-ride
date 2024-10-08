package com.fastride.domain.account.validation;

import org.springframework.stereotype.Component;

import com.fastride.domain.account.model.Account;

@Component
public class AccountValidator {

	private ExistingAccountValidator existingAccountValidator;
	private NameValidator nameValidator;
	private EmailValidator emailValidator;
	private CpfValidator cpfValidator;
	private CarPlateValidator carPlateValidator;

	public AccountValidator(ExistingAccountValidator existingAccountValidator, NameValidator nameValidator,
			EmailValidator emailValidator, CpfValidator cpfValidator, CarPlateValidator carPlateValidator) {
		this.existingAccountValidator = existingAccountValidator;
		this.nameValidator = nameValidator;
		this.emailValidator = emailValidator;
		this.cpfValidator = cpfValidator;
		this.carPlateValidator = carPlateValidator;
	}

	public void validate(Account account) {
		this.existingAccountValidator.validate(account.getEmail());
		this.nameValidator.validate(account.getName());
		this.emailValidator.validate(account.getEmail());
		this.cpfValidator.validate(account.getCpf());
		if (account.isDriver()) {
			this.carPlateValidator.validate(account.getCarPlate());
		}
	}

}