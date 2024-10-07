package com.fastride.domain;

import java.util.UUID;

public class Account {

	private UUID accountId;
	private String name;
	private String email;
	private String cpf;
	private String carPlate;
	private boolean passengerAccount;
	private boolean driverAccount;

	public Account() {
	}

	public Account(UUID accountId, Account account) {
		this.accountId = accountId;
		this.name = account.getName();
		this.email = account.getEmail();
		this.cpf = account.getCpf();
		this.carPlate = account.getCarPlate();
		this.passengerAccount = account.isPassengerAccount();
		this.driverAccount = account.isDriverAccount();
	}

	public UUID getAccountId() {
		return accountId;
	}

	void setAccountId(UUID accountId) {
		this.accountId = accountId;
	}

	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCarPlate() {
		return carPlate;
	}

	void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}

	public boolean isPassengerAccount() {
		return passengerAccount;
	}

	void setPassengerAccount(boolean passengerAccount) {
		this.passengerAccount = passengerAccount;
	}

	public boolean isDriverAccount() {
		return driverAccount;
	}

	void setDriverAccount(boolean driverAccount) {
		this.driverAccount = driverAccount;
	}

}
