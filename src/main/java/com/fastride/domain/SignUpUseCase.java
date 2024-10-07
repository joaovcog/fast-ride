package com.fastride.domain;

import java.util.List;
import java.util.UUID;
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

	public Account signUp(Account account) {
		String selectQuery = "SELECT * FROM fast_ride.account WHERE email = ?";
		List<String> existingAccountIds = this.jdbcTemplate.query(selectQuery,
				(resultSet, rowNumber) -> new String(resultSet.getString("account_id")), account.getEmail());
		if (!existingAccountIds.isEmpty())
			throw new ValidationException(String.format("An account with the e-mail %s already exists! "
					+ "Please, type another e-mail for creating a new account.", account.getEmail()));

		if (!StringUtils.hasText(account.getName())
				|| !Pattern.matches("^((?=.{1,29}$)[A-Z]\\w*(\\s[A-Z]\\w*)*)$", account.getName()))
			throw new ValidationException("Invalid name! The name should have only letters.");

		if (!Pattern.matches("^(.+)@(.+)$", account.getEmail()))
			throw new ValidationException("Invalid e-mail! Please, type a valid e-mail for signing up.");

		if (!CpfValidator.isValid(account.getCpf()))
			throw new ValidationException("Invalid CPF! Please, type a valid CPF for signing up.");

		if (account.isDriver() && (!StringUtils.hasText(account.getCarPlate())
				|| !Pattern.matches("[A-Z]{3}[0-9]{4}", account.getCarPlate()))) {
			throw new ValidationException(
					"Invalid car plate! Please, type a valid car plate with 3 letters and 4 numbers for signing up.");
		}
		String insertQuery = "INSERT INTO fast_ride.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver) VALUES (?, ?, ?, ?, ?, ?, ?)";
		UUID accountId = UUID.randomUUID();
		this.jdbcTemplate.update(insertQuery, accountId, account.getName(), account.getEmail(), account.getCpf(),
				account.getCarPlate(), account.isPassenger(), account.isDriver());
		return new Account(accountId, account);
	}

}
