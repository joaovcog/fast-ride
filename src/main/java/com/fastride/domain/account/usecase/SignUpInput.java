package com.fastride.domain.account.usecase;

public record SignUpInput(String name, String email, String cpf, String carPlate, boolean passenger, boolean driver) {
}
