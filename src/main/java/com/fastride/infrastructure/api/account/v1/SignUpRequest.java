package com.fastride.infrastructure.api.account.v1;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SignUpRequest {

	@NotBlank(message = "Name is required.")
	private String name;
	@NotBlank(message = "E-mail is required.")
	@Email(message = "Invalid E-mail.")
	private String email;
	@NotBlank(message = "CPF is required.")
	@CPF(message = "Invalid CPF.")
	private String cpf;
	private String carPlate;
	private boolean driver;
	private boolean passenger;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCarPlate() {
		return carPlate;
	}

	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}

	public boolean isDriver() {
		return driver;
	}

	public void setDriver(boolean driver) {
		this.driver = driver;
	}

	public boolean isPassenger() {
		return passenger;
	}

	public void setPassenger(boolean passenger) {
		this.passenger = passenger;
	}

}
