package com.fastride.domain.account.model;

import java.util.UUID;

import com.fastride.domain.shared.EntityId;

public class AccountBuilder {

	private final Account account;

	private AccountBuilder() {
		this.account = new Account();
	}

	public static AccountBuilder getInstance() {
		return new AccountBuilder();
	}

	public AccountBuilder accountId(UUID accountId) {
		this.account.setAccountId(new EntityId(accountId));
		return this;
	}

	public AccountBuilder accountId(EntityId accountId) {
		this.account.setAccountId(accountId);
		return this;
	}

	public AccountBuilder name(String name) {
		this.account.setName(new Name(name));
		return this;
	}

	public AccountBuilder email(String email) {
		this.account.setEmail(new Email(email));
		return this;
	}

	public AccountBuilder cpf(String cpf) {
		this.account.setCpf(new Cpf(cpf));
		return this;
	}

	public AccountBuilder carPlate(String carPlate) {
		this.account.setCarPlate(carPlate);
		return this;
	}

	public AccountBuilder passenger(boolean passenger) {
		this.account.setPassenger(passenger);
		return this;
	}

	public AccountBuilder driver(boolean driver) {
		this.account.setDriver(driver);
		return this;
	}

	public AccountBuilder passenger() {
		this.account.setPassenger(true);
		this.account.setDriver(false);
		return this;
	}

	public AccountBuilder driver() {
		this.account.setDriver(true);
		this.account.setPassenger(false);
		return this;
	}

	public Account build() {
		return account;
	}

}
