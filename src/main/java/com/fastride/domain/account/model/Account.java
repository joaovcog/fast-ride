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

	private Account(AccountBuilder builder) {
		this.accountId = this.createId(builder.accountId);
		this.name = new Name(builder.name);
		this.email = new Email(builder.email);
		this.cpf = new Cpf(builder.cpf);
		this.passenger = builder.passenger;
		this.driver = builder.driver;
		if (this.driver) {
			this.carPlate = new CarPlate(builder.carPlate);
		}
	}

	private EntityId createId(String accountId) {
		if (Objects.isNull(accountId))
			return new EntityId();
		return new EntityId(accountId);
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

	public static class AccountBuilder {
		private String accountId;
		private String name;
		private String email;
		private String cpf;
		private String carPlate;
		private boolean passenger;
		private boolean driver;

		private AccountBuilder() {
		}

		public static AccountBuilder getInstance() {
			return new AccountBuilder();
		}

		public AccountBuilder accountId(String accountId) {
			this.accountId = accountId;
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

		public Account build() {
			return new Account(this);
		}

	}

}
