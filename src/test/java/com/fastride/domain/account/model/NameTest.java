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

public class NameTest {

	private static final String NAME_CONTENT_JOHN_DOE = "John Doe";
	private static final String NAME_CONTENT_MARY_JANE = "Mary Jane";

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "John Smith$", "John 5", "4553" })
	void shouldNotCreateNameValueObjectWhenNameContentIsInvalidByContainingNumbersOrSpecialCharacters(String name) {
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			new Name(name);
		});

		assertEquals("Invalid name! The name should have only letters.", exception.getMessage());
	}

	@Test
	void shouldCreateNameValueObjectWhenNameContentContainsOnlyLetters() {
		Name createdName = null;
		try {
			createdName = new Name(NAME_CONTENT_JOHN_DOE);
		} catch (Exception ex) {
			fail("Exception should not be thrown here, but got a: " + ex.getMessage());
		}

		assertNotNull(createdName);
		assertNotNull(createdName.getContent());
		assertEquals(NAME_CONTENT_JOHN_DOE, createdName.getContent());
	}

	@Test
	void shouldResolveToTrueWhenComparingTwoNameValueObjectsWithTheSameContentUsingEquals() {
		Name nameOne = new Name(NAME_CONTENT_JOHN_DOE);
		Name nameTwo = new Name(NAME_CONTENT_JOHN_DOE);

		assertTrue(nameOne.equals(nameTwo));
	}

	@Test
	void shouldResolveToFalseWhenComparingTwoNameValueObjectsWithDifferentContentUsingEquals() {
		Name nameOne = new Name(NAME_CONTENT_JOHN_DOE);
		Name nameTwo = new Name(NAME_CONTENT_MARY_JANE);

		assertFalse(nameOne.equals(nameTwo));
	}

}
