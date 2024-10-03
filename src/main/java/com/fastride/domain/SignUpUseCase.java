package com.fastride.domain;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SignUpUseCase {

	private JdbcTemplate jdbcTemplate;

	public SignUpUseCase(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Object signUp(Object input) {
		UUID id = UUID.randomUUID();
		String selectQuery = "SELECT * FROM fast_ride.account WHERE email = ?";
		List<Object> existantAccountIds = jdbcTemplate.query(selectQuery,
				(resultSet, rowNumber) -> new String(resultSet.getString("account_id")),
				(String) ((Object[]) input)[2]);
		if (!existantAccountIds.isEmpty())
			return -4;

		Pattern namePattern = Pattern.compile("^((?=.{1,29}$)[A-Z]\\w*(\\s[A-Z]\\w*)*)$");
		Matcher nameMatcher = namePattern.matcher((String) ((Object[]) input)[0]);
		if (!nameMatcher.matches())
			return -3;

		Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");
		Matcher emailMatcher = emailPattern.matcher((String) ((Object[]) input)[2]);
		if (!emailMatcher.matches())
			return -2;

		if (!CpfValidator.isValid((String) ((Object[]) input)[3]))
			return -1;

		if ((boolean) ((Object[]) input)[6]) {
			String carPlate = (String) ((Object[]) input)[4];
			if (Objects.isNull(carPlate) || !Pattern.matches("[A-Z]{3}[0-9]{4}", carPlate))
				return -5;
		}

		String insertQuery = "INSERT INTO fast_ride.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver) VALUES (?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(insertQuery, id, (String) ((Object[]) input)[0], (String) ((Object[]) input)[2],
				(String) ((Object[]) input)[3], (String) ((Object[]) input)[4], (boolean) ((Object[]) input)[5],
				(boolean) ((Object[]) input)[6]);
		return new Object[] { "accountId", id };
	}

}
