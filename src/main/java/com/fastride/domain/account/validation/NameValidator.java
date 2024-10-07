package com.fastride.domain.account.validation;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fastride.domain.shared.ValidationException;

@Component
public class NameValidator implements Validator {

	private static final String VALID_NAME_PATTERN = "^((?=.{1,29}$)[A-Z]\\w*(\\s[A-Z]\\w*)*)$";

	@Override
	public void validate(String name) {
		if (!StringUtils.hasText(name) || !Pattern.matches(VALID_NAME_PATTERN, name))
			throw new ValidationException("Invalid name! The name should have only letters.");
	}

}
