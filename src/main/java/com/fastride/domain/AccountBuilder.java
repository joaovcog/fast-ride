package com.fastride.domain;

import java.util.UUID;

public class AccountBuilder {

	private final Account account;

	private AccountBuilder() {
		this.account = new Account();
	}

	public static AccountBuilder getInstance() {
		return new AccountBuilder();
	}

	public AccountBuilder accountId(UUID accountId) {
		this.account.setAccountId(accountId);
		return this;
	}

	public AccountBuilder name(String name) {
		this.account.setName(name);
		return this;
	}

	public AccountBuilder email(String email) {
		this.account.setEmail(email);
		return this;
	}

	public AccountBuilder cpf(String cpf) {
		this.account.setCpf(cpf);
		return this;
	}

	public AccountBuilder carPlate(String carPlate) {
		this.account.setCarPlate(carPlate);
		return this;
	}

	public AccountBuilder passenger() {
		this.account.setPassengerAccount(true);
		this.account.setDriverAccount(false);
		return this;
	}

	public AccountBuilder driver() {
		this.account.setDriverAccount(true);
		this.account.setPassengerAccount(false);
		return this;
	}

	public Account build() {
		return account;
	}

}
