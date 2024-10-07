package com.fastride.domain.account.usecase;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.AccountRepository;
import com.fastride.domain.account.validation.CarPlateValidator;
import com.fastride.domain.account.validation.CpfValidator;
import com.fastride.domain.account.validation.EmailValidator;
import com.fastride.domain.account.validation.NameValidator;
import com.fastride.domain.shared.ValidationException;

@Component
public class SignUpUseCase {

	private AccountRepository accountRepository;

	public SignUpUseCase(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public Account signUp(Account account) {
		if (accountRepository.findByEmail(account.getEmail()).isPresent())
			throw new ValidationException(String.format("An account with the e-mail %s already exists! "
					+ "Please, type another e-mail for creating a new account.", account.getEmail()));

		new NameValidator().validate(account.getName());
		new EmailValidator().validate(account.getEmail());
		new CpfValidator().validate(account.getCpf());
		if (account.isDriver()) {
			new CarPlateValidator().validate(account.getCarPlate());
		}
		account = new Account(UUID.randomUUID(), account);
		return this.accountRepository.create(account);
	}

}
