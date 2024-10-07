package com.fastride.domain.account.usecase;

import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.AccountRepository;
import com.fastride.domain.account.validation.CpfValidator;
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

		if (!Pattern.matches("^(.+)@(.+)$", account.getEmail()))
			throw new ValidationException("Invalid e-mail! Please, type a valid e-mail for signing up.");

		if (!CpfValidator.isValid(account.getCpf()))
			throw new ValidationException("Invalid CPF! Please, type a valid CPF for signing up.");

		if (account.isDriver() && (!StringUtils.hasText(account.getCarPlate())
				|| !Pattern.matches("[A-Z]{3}[0-9]{4}", account.getCarPlate()))) {
			throw new ValidationException(
					"Invalid car plate! Please, type a valid car plate with 3 letters and 4 numbers for signing up.");
		}
		account = new Account(UUID.randomUUID(), account);
		return this.accountRepository.create(account);
	}

}
