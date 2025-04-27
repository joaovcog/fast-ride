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

public class EmailTest {

	private static final String EMAIL_CONTENT_JOHN_DOE = "john.doe@email.com";
	private static final String EMAIL_CONTENT_MARY_JANE = "mary.jane@email.com";

	@ParameterizedTest
	@NullAndEmptySource
	void shouldNotCreateEmailValueObjectWhenEmailContentIsEmptyOrNull(String email) {
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			new Email(email);
		});

		assertEquals("E-mail is required.", exception.getMessage());
	}

	@ParameterizedTest
	@ValueSource(strings = { "john", "john@", "@email.com", "555" })
	void shouldNotCreateEmailValueObjectWhenEmailContentIsInvalidByNotContainingTheValidEmailFormat(String email) {
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			new Email(email);
		});

		assertEquals("Invalid e-mail! Please, type a valid e-mail for signing up.", exception.getMessage());
	}

	@Test
	void shouldCreateEmailValueObjectWhenEmailContentContainsValidEmailFormat() {
		Email createdEmail = null;
		try {
			createdEmail = new Email(EMAIL_CONTENT_JOHN_DOE);
		} catch (Exception ex) {
			fail("Exception should not be thrown here, but got a: " + ex.getMessage());
		}

		assertNotNull(createdEmail);
		assertNotNull(createdEmail.getContent());
		assertEquals(EMAIL_CONTENT_JOHN_DOE, createdEmail.getContent());
	}

	@Test
	void shouldResolveToTrueWhenComparingTwoEmailValueObjectsWithTheSameContentUsingEquals() {
		Email emailOne = new Email(EMAIL_CONTENT_JOHN_DOE);
		Email emailTwo = new Email(EMAIL_CONTENT_JOHN_DOE);

		assertTrue(emailOne.equals(emailTwo));
	}

	@Test
	void shouldResolveToFalseWhenComparingTwoEmailValueObjectsWithDifferentContentUsingEquals() {
		Email emailOne = new Email(EMAIL_CONTENT_JOHN_DOE);
		Email emailTwo = new Email(EMAIL_CONTENT_MARY_JANE);

		assertFalse(emailOne.equals(emailTwo));
	}

}
