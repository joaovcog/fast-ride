package com.fastride.domain.account.model;

import java.util.Objects;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.fastride.domain.shared.ValidationException;

public class Name {

	private static final String VALID_NAME_PATTERN = "^((?=.{1,29}$)[A-Z]\\w*(\\s[A-Z]\\w*)*)$";

	private final String nameContent;

	public Name(String nameContent) {
		this.validate(nameContent);
		this.nameContent = nameContent;
	}

	public String getContent() {
		return nameContent;
	}

	private void validate(String name) {
		if (!StringUtils.hasText(name) || !Pattern.matches(VALID_NAME_PATTERN, name))
			throw new ValidationException("Invalid name! The name should have only letters.");
	}

	@Override
	public int hashCode() {
		return Objects.hash(nameContent);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Name other = (Name) obj;
		return Objects.equals(nameContent, other.nameContent);
	}

}
