package com.fastride.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class SignupUseCase {

	public Object signup(Object input) {
		String url = "jdbc:postgresql://localhost:5432/fast_ride_db";
		String user = "postgres";
		String password = "root";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, user, password);
			UUID id = UUID.randomUUID();
			String selectQuery = "SELECT * FROM fast_ride.account WHERE email = ?";
			try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
				selectStatement.setString(1, (String) ((Object[]) input)[2]);
				ResultSet resultSet = selectStatement.executeQuery();
				if (!resultSet.next()) {
					Pattern namePattern = Pattern.compile("^((?=.{1,29}$)[A-Z]\\w*(\\s[A-Z]\\w*)*)$");
					Matcher nameMatcher = namePattern.matcher((String) ((Object[]) input)[0]);
					if (nameMatcher.matches()) {
						Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");
						Matcher emailMatcher = emailPattern.matcher((String) ((Object[]) input)[2]);
						if (emailMatcher.matches()) {
							if (CpfValidator.isValid((String) ((Object[]) input)[3])) {
								if ((boolean) ((Object[]) input)[6]) {
									String carPlate = (String) ((Object[]) input)[4];
									if (!Objects.isNull(carPlate) && Pattern.matches("[A-Z]{3}[0-9]{4}", carPlate)) {
										String insertQuery = "INSERT INTO fast_ride.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver) VALUES (?, ?, ?, ?, ?, ?, ?)";
										try (PreparedStatement insertStatement = connection
												.prepareStatement(insertQuery)) {
											insertStatement.setObject(1, id);
											insertStatement.setString(2, (String) ((Object[]) input)[0]);
											insertStatement.setString(3, (String) ((Object[]) input)[2]);
											insertStatement.setString(4, (String) ((Object[]) input)[3]);
											insertStatement.setString(5, (String) ((Object[]) input)[4]);
											insertStatement.setBoolean(6, (boolean) ((Object[]) input)[5]);
											insertStatement.setBoolean(7, (boolean) ((Object[]) input)[6]);
											insertStatement.executeUpdate();
											return new Object[] { "accountId", id };
										} catch (SQLException e) {
											e.printStackTrace();
										}
									} else {
										// invalid car plate
										return -5;
									}
								} else {
									String insertQuery = "INSERT INTO fast_ride.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver) VALUES (?, ?, ?, ?, ?, ?, ?)";
									try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
										insertStatement.setObject(1, id);
										insertStatement.setString(2, (String) ((Object[]) input)[0]);
										insertStatement.setString(3, (String) ((Object[]) input)[2]);
										insertStatement.setString(4, (String) ((Object[]) input)[3]);
										insertStatement.setString(5, (String) ((Object[]) input)[4]);
										insertStatement.setBoolean(6, (boolean) ((Object[]) input)[5]);
										insertStatement.setBoolean(7, (boolean) ((Object[]) input)[6]);
										insertStatement.executeUpdate();
										return new Object[] { "accountId", id };
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}
							} else {
								// invalid cpf
								return -1;
							}
						} else {
							// invalid email
							return -2;
						}
					} else {
						// invalid name
						return -3;
					}
				} else {
					// already exists
					return -4;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
