package com.fastride.domain.account.validation;

import java.util.regex.Pattern;

import com.fastride.domain.shared.ValidationException;

public class EmailValidator implements Validator {

	private static final String VALID_EMAIL_PATTERN = "^(.+)@(.+)$";

	@Override
	public void validate(String email) {
		if (!Pattern.matches(VALID_EMAIL_PATTERN, email))
			throw new ValidationException("Invalid e-mail! Please, type a valid e-mail for signing up.");
	}

}
