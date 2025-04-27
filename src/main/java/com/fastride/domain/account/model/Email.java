package com.fastride.domain.account.model;

import java.util.Objects;
import java.util.regex.Pattern;

import org.springframework.util.ObjectUtils;

import com.fastride.domain.shared.ValidationException;

public class Email {

	private static final String VALID_EMAIL_PATTERN = "^(.+)@(.+)$";

	private final String emailContent;

	public Email(String emailContent) {
		this.validate(emailContent);
		this.emailContent = emailContent;
	}

	private void validate(String emailContent) {
		this.validateNotNullOrEmpty(emailContent);
		this.validateContent(emailContent);
	}

	private void validateNotNullOrEmpty(String emailContent) {
		if (ObjectUtils.isEmpty(emailContent))
			throw new ValidationException("E-mail is required.");
	}

	private void validateContent(String emailContent) {
		if (!Pattern.matches(VALID_EMAIL_PATTERN, emailContent))
			throw new ValidationException("Invalid e-mail! Please, type a valid e-mail for signing up.");
	}

	public String getContent() {
		return emailContent;
	}

	@Override
	public int hashCode() {
		return Objects.hash(emailContent);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Email other = (Email) obj;
		return Objects.equals(emailContent, other.emailContent);
	}

}
