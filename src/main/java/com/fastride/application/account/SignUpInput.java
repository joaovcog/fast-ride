package com.fastride.application.account;

public record SignUpInput(String name, String email, String cpf, String carPlate, boolean passenger, boolean driver) {
}
