package com.fastride.domain.shared;

import static com.fastride.domain.shared.EntityId.VALID_ID_PATTERN;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EntityIdTest {

	private static final String ID_AS_STRING = "9627ef53-8a51-4104-be8b-121eef607851";

	@Test
	void shouldCreateEntityIdObjectWithTheRandomId() {
		List<EntityId> entityIds = new ArrayList<>();
		assertDoesNotThrow(() -> {
			entityIds.add(new EntityId());
		});
		assertFalse(entityIds.isEmpty());
		assertNotNull(entityIds.get(0));
		assertTrue(Pattern.matches(VALID_ID_PATTERN, entityIds.get(0).toString()));
	}

	@Test
	void shouldCreateEntityIdObjectWithTheProvidedIdInString() {
		List<EntityId> entityIds = new ArrayList<>();
		assertDoesNotThrow(() -> {
			entityIds.add(new EntityId(ID_AS_STRING));
		});
		assertFalse(entityIds.isEmpty());
		assertNotNull(entityIds.get(0));
		assertEquals(ID_AS_STRING, entityIds.get(0).toUUID().toString());
	}

	@Test
	void shouldCreateEntityIdObjectWithTheProvidedIdInUUID() {
		List<EntityId> entityIds = new ArrayList<>();
		UUID uuid = UUID.randomUUID();
		assertDoesNotThrow(() -> {
			entityIds.add(new EntityId(uuid));
		});
		assertFalse(entityIds.isEmpty());
		assertNotNull(entityIds.get(0));
		assertEquals(uuid, entityIds.get(0).toUUID());
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "213465465", "aaaaaabb", "aaa-852-b", "*-564", "1122334455",
			"b67ec8c0-dc0f-416e-8e1f-3c9c3f3**" })
	void shouldThrowAndExceptionWhenInvalidUUIDPatternProvided(String id) {
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			new EntityId(id);
		});
		assertEquals("Invalid pattern for ID! The ID should follow the UUID pattern.", exception.getMessage());
	}

	@Test
	void shouldThrowAndExceptionWhenNullIdUUIDProvidedAsString() {
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			String id = null;
			new EntityId(id);
		});
		assertEquals("The ID is required and was not filled.", exception.getMessage());
	}

	@Test
	void shouldThrowAndExceptionWhenNullIdProvidedAsUUID() {
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			String id = null;
			new EntityId(id);
		});
		assertEquals("The ID is required and was not filled.", exception.getMessage());
	}

}
