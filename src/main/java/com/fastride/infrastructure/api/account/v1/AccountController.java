package com.fastride.infrastructure.api.account.v1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.usecase.GetAccountUseCase;
import com.fastride.domain.account.usecase.SignUpUseCase;
import com.fastride.domain.shared.EntityId;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	private SignUpUseCase signUpUseCase;
	private GetAccountUseCase getAccountUseCase;
	private AccountConverter accountConverter;

	public AccountController(SignUpUseCase signUpUseCase, GetAccountUseCase getAccountUseCase,
			AccountConverter accountConverter) {
		this.signUpUseCase = signUpUseCase;
		this.getAccountUseCase = getAccountUseCase;
		this.accountConverter = accountConverter;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AccountOutputDto signUp(@RequestBody @Valid AccountInputDto accountInput) {
		Account account = this.accountConverter.toEntity(accountInput);
		account = this.signUpUseCase.execute(account);
		return this.accountConverter.toOutputDto(account);
	}

	@GetMapping("/{accountId}")
	@ResponseStatus(HttpStatus.OK)
	public AccountOutputDto getAccountById(@PathVariable String accountId) {
		return this.accountConverter.toOutputDto(this.getAccountUseCase.execute(new EntityId(accountId)));
	}

}
