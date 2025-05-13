package com.fastride.domain.account.usecase;

public record GetAccountOutput(String accountId, String name, String email, String cpf, String carPlate,
		boolean passenger, boolean driver) {
}
