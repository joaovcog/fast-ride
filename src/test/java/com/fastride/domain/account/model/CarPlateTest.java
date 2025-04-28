package com.fastride.domain.account.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.fastride.domain.shared.ValidationException;

public class CarPlateTest {

	private static final String CAR_PLATE_CONTENT_ONE = "ABC1234";
	private static final String CAR_PLATE_CONTENT_TWO = "ABC5678";

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "AA", "ABC-234", "AB-1234", "7896-ABC", "5462", "ABC" })
	void shouldNotCreateCarPlateValueObjectWhenCarPlateContentDoesNotContainTheValidCarPlateFormat(String email) {
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			new CarPlate(email);
		});

		assertEquals("Invalid car plate! Please, type a valid car plate with 3 letters and 4 numbers for signing up.",
				exception.getMessage());
	}

	@Test
	void shouldCreateCarPlateValueObjectWhenCarPlateContentContainsValidCarPlateFormat() {
		CarPlate createdCarPlate = null;
		try {
			createdCarPlate = new CarPlate(CAR_PLATE_CONTENT_ONE);
		} catch (Exception ex) {
			fail("Exception should not be thrown here, but got a: " + ex.getMessage());
		}

		assertNotNull(createdCarPlate);
		assertNotNull(createdCarPlate.getContent());
		assertEquals(CAR_PLATE_CONTENT_ONE, createdCarPlate.getContent());
	}

	@Test
	void shouldResolveToTrueWhenComparingTwoCarPlateValueObjectsWithTheSameContentUsingEquals() {
		CarPlate carPlateOne = new CarPlate(CAR_PLATE_CONTENT_ONE);
		CarPlate carPlateTwo = new CarPlate(CAR_PLATE_CONTENT_ONE);

		assertTrue(carPlateOne.equals(carPlateTwo));
	}

	@Test
	void shouldResolveToFalseWhenComparingTwoCarPlateValueObjectsWithDifferentContentUsingEquals() {
		CarPlate carPlateOne = new CarPlate(CAR_PLATE_CONTENT_ONE);
		CarPlate carPlateTwo = new CarPlate(CAR_PLATE_CONTENT_TWO);

		assertFalse(carPlateOne.equals(carPlateTwo));
	}

}
