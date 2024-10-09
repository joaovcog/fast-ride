package com.fastride.infrastructure.api;

import org.springframework.stereotype.Component;

import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.AccountBuilder;

@Component
public class AccountConverter {

	public Account toEntity(AccountInputDto accountInputDto) {
		return AccountBuilder.getInstance().name(accountInputDto.getName()).email(accountInputDto.getEmail())
				.cpf(accountInputDto.getCpf()).carPlate(accountInputDto.getCarPlate())
				.passenger(accountInputDto.isPassenger()).driver(accountInputDto.isDriver()).build();
	}

	public AccountOutputDto toOutputDto(Account account) {
		AccountOutputDto accountOutputDto = new AccountOutputDto();
		accountOutputDto.setAccountId(account.getAccountId().toString());
		accountOutputDto.setName(account.getName());
		accountOutputDto.setEmail(account.getEmail());
		accountOutputDto.setCpf(account.getCpf());
		accountOutputDto.setCarPlate(account.getCarPlate());
		accountOutputDto.setPassenger(account.isPassenger());
		accountOutputDto.setDriver(account.isDriver());
		return accountOutputDto;
	}

}
