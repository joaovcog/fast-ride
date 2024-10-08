package com.fastride.domain.account.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.fastride.domain.shared.ValidationException;

class CpfValidatorTest {

	private CpfValidator cpfValidator;

	@BeforeEach
	void setup() {
		this.cpfValidator = new CpfValidator();
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "12345678910", "11111111111", "22222222222", "234bc", "1122334455", "112233445566" })
	void shouldNotSignUpWhenCpfIsInvalid(String cpf) {
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.cpfValidator.validate(cpf);
		});

		assertEquals("Invalid CPF! Please, type a valid CPF for signing up.", exception.getMessage());
	}

	@ParameterizedTest
	@ValueSource(strings = { "96311015099", "963.110.150-99" })
	void shouldValidateCpfSuccesfullyWithElevenNumbersWithAndWithoutMask(String cpf) {
		assertDoesNotThrow(() -> {
			this.cpfValidator.validate(cpf);
		});
	}

}
