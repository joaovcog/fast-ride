package com.fastride.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CpfValidatorTest {

	@Test
	void shouldValidateCpfSuccesfullyWithElevenNumbersAndNoMask() {
		String cpf = "96311015099";
		assertTrue(CpfValidator.isValid(cpf));
	}

	@Test
	void shouldValidateCpfSuccesfullyWithElevenNumbersAndMask() {
		String cpf = "963.110.150-99";
		assertTrue(CpfValidator.isValid(cpf));
	}

	@ParameterizedTest
	@ValueSource(strings = { "12345678910", "11111111111", "22222222222" })
	void shouldFailValidatingCpfWithInvalidDigits(String cpf) {
		assertFalse(CpfValidator.isValid(cpf));
	}

	@Test
	void shouldFailValidatingCpfWithTenNumbers() {
		String cpf = "9631101509";
		assertFalse(CpfValidator.isValid(cpf));
	}

	@Test
	void shouldFailValidatingCpfWithTwelveNumbers() {
		String cpf = "963110150992";
		assertFalse(CpfValidator.isValid(cpf));
	}

	@Test
	void shouldFailValidatingNullValueForCpf() {
		String cpf = null;
		assertFalse(CpfValidator.isValid(cpf));
	}

	@Test
	void shouldFailValidatingEmptyStringForCpf() {
		String cpf = "";
		assertFalse(CpfValidator.isValid(cpf));
	}
}
