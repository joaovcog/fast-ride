package com.fastride.domain;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SignUpUseCase {

	private JdbcTemplate jdbcTemplate;

	public SignUpUseCase(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Object signUp(Object input) {
		String selectQuery = "SELECT * FROM fast_ride.account WHERE email = ?";
		String email = (String) ((Object[]) input)[2];
		List<Object> existantAccountIds = this.jdbcTemplate.query(selectQuery,
				(resultSet, rowNumber) -> new String(resultSet.getString("account_id")), email);
		if (!existantAccountIds.isEmpty())
			throw new ValidationException(String.format("An account with the e-mail %s already exists! "
					+ "Please, type another e-mail for creating a new account.", email));

		String name = (String) ((Object[]) input)[0];
		if (!StringUtils.hasText(name) || !Pattern.matches("^((?=.{1,29}$)[A-Z]\\w*(\\s[A-Z]\\w*)*)$", name))
			throw new ValidationException("Invalid name! The name should have only letters.");

		Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");
		Matcher emailMatcher = emailPattern.matcher(email);
		if (!emailMatcher.matches())
			throw new ValidationException("Invalid e-mail! Please, type a valid e-mail for signing up.");

		String cpf = (String) ((Object[]) input)[3];
		if (!CpfValidator.isValid(cpf))
			throw new ValidationException("Invalid CPF! Please, type a valid CPF for signing up.");

		boolean isDriver = (boolean) ((Object[]) input)[6];
		String carPlate = (String) ((Object[]) input)[4];
		if (isDriver && (!StringUtils.hasText(carPlate) || !Pattern.matches("[A-Z]{3}[0-9]{4}", carPlate))) {
			throw new ValidationException(
					"Invalid car plate! Please, type a valid car plate with 3 letters and 4 numbers for signing up.");
		}

		String insertQuery = "INSERT INTO fast_ride.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver) VALUES (?, ?, ?, ?, ?, ?, ?)";
		UUID id = UUID.randomUUID();
		this.jdbcTemplate.update(insertQuery, id, name, email, cpf, (String) ((Object[]) input)[4],
				(boolean) ((Object[]) input)[5], isDriver);
		return new Object[] { "accountId", id };
	}

}
