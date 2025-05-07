package com.fastride.infrastructure.api.account.v1;

import org.springframework.stereotype.Component;

import com.fastride.domain.account.model.Account;

@Component
public class AccountConverter {

	public AccountOutputDto toOutputDto(Account account) {
		AccountOutputDto accountOutputDto = new AccountOutputDto();
		accountOutputDto.setAccountId(account.getAccountId().toString());
		accountOutputDto.setName(account.getName().getContent());
		accountOutputDto.setEmail(account.getEmail().getContent());
		accountOutputDto.setCpf(account.getCpf().getContent());
		accountOutputDto.setCarPlate(account.getCarPlateContent());
		accountOutputDto.setPassenger(account.isPassenger());
		accountOutputDto.setDriver(account.isDriver());
		return accountOutputDto;
	}

}
