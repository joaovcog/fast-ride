package com.fastride.domain.account.model;

import java.util.Objects;
import java.util.regex.Pattern;

import org.springframework.util.ObjectUtils;

import com.fastride.domain.shared.ValidationException;

public class CarPlate {

	private static final String VALID_CAR_PLATE_PATTERN = "[A-Z]{3}[0-9]{4}";

	private String carPlateContent;

	public CarPlate(String carPlateContent) {
		this.validate(carPlateContent);
		this.carPlateContent = carPlateContent;
	}

	private void validate(String carPlateContent) {
		if (ObjectUtils.isEmpty(carPlateContent) || !Pattern.matches(VALID_CAR_PLATE_PATTERN, carPlateContent)) {
			throw new ValidationException(
					"Invalid car plate! Please, type a valid car plate with 3 letters and 4 numbers for signing up.");
		}
	}

	public String getContent() {
		return carPlateContent;
	}

	@Override
	public int hashCode() {
		return Objects.hash(carPlateContent);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CarPlate other = (CarPlate) obj;
		return Objects.equals(carPlateContent, other.carPlateContent);
	}

}
