package com.fastride.domain.shared;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class EntityId {

	private static final String VALID_ID_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

	private final UUID id;

	public EntityId(String id) {
		validateNull(id);
		validatePattern(id);
		this.id = UUID.fromString(id);
	}

	public EntityId(UUID id) {
		validateNull(id);
		this.id = id;
	}

	private void validateNull(Object id) {
		if (Objects.isNull(id))
			throw new ValidationException("The ID is required and was not filled.");
	}

	private void validatePattern(String id) {
		if (!Pattern.matches(VALID_ID_PATTERN, id))
			throw new ValidationException("Invalid pattern for ID! The ID should follow the UUID pattern.");
	}

	public UUID toUUID() {
		return id;
	}

	@Override
	public String toString() {
		return id.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityId other = (EntityId) obj;
		return Objects.equals(id, other.id);
	}

}
