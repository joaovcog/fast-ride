package com.fastride.domain.account.model;

import java.util.Objects;

import com.fastride.domain.shared.EntityId;

public class Account {

	private EntityId accountId;
	private Name name;
	private Email email;
	private Cpf cpf;
	private CarPlate carPlate;
	private boolean passenger;
	private boolean driver;

	Account() {
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

	public Name getName() {
		return name;
	}

	void setName(Name name) {
		this.name = name;
	}

	public Email getEmail() {
		return email;
	}

	void setEmail(Email email) {
		this.email = email;
	}

	public Cpf getCpf() {
		return cpf;
	}

	void setCpf(Cpf cpf) {
		this.cpf = cpf;
	}

	public CarPlate getCarPlate() {
		return carPlate;
	}

	public String getCarPlateContent() {
		if (Objects.isNull(carPlate)) {
			return null;
		}
		return carPlate.getContent();
	}

	void setCarPlate(CarPlate carPlate) {
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
