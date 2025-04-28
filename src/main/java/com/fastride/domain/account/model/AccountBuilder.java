package com.fastride.domain.account.model;

import java.util.UUID;

import com.fastride.domain.shared.EntityId;

public class AccountBuilder {

	private EntityId accountId;
	private String name;
	private String email;
	private String cpf;
	private String carPlate;
	private boolean passenger;
	private boolean driver;

	public static AccountBuilder getInstance() {
		return new AccountBuilder();
	}

	public AccountBuilder accountId(UUID accountId) {
		this.accountId = new EntityId(accountId);
		return this;
	}

	public AccountBuilder name(String name) {
		this.name = name;
		return this;
	}

	public AccountBuilder email(String email) {
		this.email = email;
		return this;
	}

	public AccountBuilder cpf(String cpf) {
		this.cpf = cpf;
		return this;
	}

	public AccountBuilder carPlate(String carPlate) {
		this.carPlate = carPlate;
		return this;
	}

	public AccountBuilder passenger(boolean passenger) {
		this.passenger = passenger;
		return this;
	}

	public AccountBuilder driver(boolean driver) {
		this.driver = driver;
		return this;
	}

	public AccountBuilder passenger() {
		this.passenger = true;
		this.driver = false;
		return this;
	}

	public AccountBuilder driver() {
		this.driver = true;
		this.passenger = false;
		return this;
	}

	public Account build() {
		Account account = new Account();
		account.setAccountId(this.accountId);
		account.setName(new Name(this.name));
		account.setEmail(new Email(this.email));
		account.setCpf(new Cpf(this.cpf));
		if (this.driver) {
			account.setCarPlate(new CarPlate(this.carPlate));
		}
		account.setPassenger(this.passenger);
		account.setDriver(this.driver);
		return account;
	}

}
