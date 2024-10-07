package com.fastride.domain.account.validation;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.fastride.domain.shared.ValidationException;

public class CarPlateValidator implements Validator {

	private static final String VALID_CAR_PLATE_PATTERN = "[A-Z]{3}[0-9]{4}";

	@Override
	public void validate(String carPlate) {
		if (!StringUtils.hasText(carPlate) || !Pattern.matches(VALID_CAR_PLATE_PATTERN, carPlate)) {
			throw new ValidationException(
					"Invalid car plate! Please, type a valid car plate with 3 letters and 4 numbers for signing up.");
		}
	}

}
