package com.fastride.domain.account.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.fastride.domain.shared.ValidationException;

class CpfTest {

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "12345678910", "11111111111", "22222222222", "234bc", "1122334455", "112233445566" })
	void shouldNotCreateCpfValueObjectWhenCpfIsInvalid(String cpfContent) {
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			new Cpf(cpfContent);
		});

		assertEquals("Invalid CPF! Please, type a valid CPF for signing up.", exception.getMessage());
	}

	@ParameterizedTest
	@ValueSource(strings = { "96311015099", "963.110.150-99" })
	void shouldCreateCpfValueObjectSuccesfullyWithElevenNumbersWithAndWithoutMask(String cpfContent) {
		List<Cpf> createdCpf = new ArrayList<>();
		assertDoesNotThrow(() -> {
			Cpf cpf = new Cpf(cpfContent);
			createdCpf.add(cpf);
		});
		assertEquals(1, createdCpf.size());
		assertNotNull(createdCpf.get(0));
		assertNotNull(createdCpf.get(0).getContent());
	}

}
