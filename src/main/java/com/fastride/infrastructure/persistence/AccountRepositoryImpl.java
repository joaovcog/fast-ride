package com.fastride.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.AccountBuilder;
import com.fastride.domain.account.model.AccountRepository;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

	private JdbcTemplate jdbcTemplate;

	public AccountRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Account create(Account account) {
		String insertQuery = "INSERT INTO fast_ride.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver) VALUES (?, ?, ?, ?, ?, ?, ?)";
		this.jdbcTemplate.update(insertQuery, account.getAccountId(), account.getName(), account.getEmail(),
				account.getCpf(), account.getCarPlate(), account.isPassenger(), account.isDriver());
		return account;
	}

	@Override
	public Optional<Account> findById(UUID accountId) {
		return Optional.empty();
	}

	@Override
	public Optional<Account> findByEmail(String email) {
		String selectQuery = "SELECT * FROM fast_ride.account WHERE email = ?";
		List<Account> existingAccount = this.jdbcTemplate.query(selectQuery, (resultSet, rowNumber) -> {
			return AccountBuilder.getInstance().accountId(resultSet.getObject("account_id", UUID.class))
					.name(resultSet.getString("name")).email(resultSet.getString("email"))
					.cpf(resultSet.getString("cpf")).carPlate(resultSet.getString("car_plate"))
					.passenger(resultSet.getBoolean("is_passenger")).driver(resultSet.getBoolean("is_driver")).build();
		}, email);
		return existingAccount.stream().findFirst();
	}

}
