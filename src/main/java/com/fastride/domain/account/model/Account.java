package com.fastride.domain.account.model;

import java.util.Objects;

import com.fastride.domain.shared.EntityId;

public class Account {

	private EntityId accountId;
	private String name;
	private String email;
	private Cpf cpf;
	private String carPlate;
	private boolean passenger;
	private boolean driver;

	public Account() {
	}

	public Account(EntityId accountId, Account account) {
		this.accountId = accountId;
		this.name = account.getName();
		this.email = account.getEmail();
		this.cpf = account.getCpf();
		this.carPlate = account.getCarPlate();
		this.passenger = account.isPassenger();
		this.driver = account.isDriver();
	}

	public EntityId getAccountId() {
		return accountId;
	}

	void setAccountId(EntityId accountId) {
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

	public Cpf getCpf() {
		return cpf;
	}

	void setCpf(Cpf cpf) {
		this.cpf = cpf;
	}

	public String getCarPlate() {
		return carPlate;
	}

	void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}

	public boolean isPassenger() {
		return passenger;
	}

	void setPassenger(boolean passengerAccount) {
		this.passenger = passengerAccount;
	}

	public boolean isDriver() {
		return driver;
	}

	void setDriver(boolean driverAccount) {
		this.driver = driverAccount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		return Objects.equals(accountId, other.accountId);
	}

}
