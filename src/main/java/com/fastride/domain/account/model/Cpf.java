package com.fastride.domain.account.model;

import java.util.Objects;
import java.util.Optional;

import com.fastride.domain.shared.ValidationException;

public class Cpf {

	private final String cpfContent;

	public Cpf(String cpfContent) {
		this.validate(cpfContent);
		this.cpfContent = cpfContent;
	}

	public String getContent() {
		return this.cpfContent;
	}

	private void validate(String rawCpf) {
		String cpf = removeNonDigits(rawCpf);
		if (isInvalidLength(cpf) || hasAllDigitsEqual(cpf) || hasInvalidDigits(cpf))
			throw new ValidationException("Invalid CPF! Please, type a valid CPF for signing up.");
	}

	private String removeNonDigits(String cpf) {
		return Optional.ofNullable(cpf).orElse("").replaceAll("\\D", "");
	}

	private boolean isInvalidLength(String cpf) {
		final int CPF_LENGTH = 11;
		return cpf.length() != CPF_LENGTH;
	}

	private boolean hasAllDigitsEqual(String cpf) {
		char firstCpfDigit = cpf.charAt(0);
		return cpf.chars().allMatch(digit -> digit == firstCpfDigit);
	}

	private boolean hasInvalidDigits(String cpf) {
		return !extractDigit(cpf).equals(getCalculatedVerifierDigit(cpf));
	}

	private String getCalculatedVerifierDigit(String cpf) {
		int digit1 = calculateDigit(cpf, 10);
		int digit2 = calculateDigit(cpf, 11);
		return String.format("%s%s", digit1, digit2);
	}

	private int calculateDigit(String cpf, int factor) {
		int total = 0;
		for (char digit : cpf.toCharArray()) {
			if (factor > 1) {
				total += Character.getNumericValue(digit) * factor--;
			}
		}
		int rest = total % 11;
		return (rest < 2) ? 0 : 11 - rest;
	}

	private String extractDigit(String cpf) {
		return cpf.substring(9);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cpfContent);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cpf other = (Cpf) obj;
		return Objects.equals(cpfContent, other.cpfContent);
	}

}
