package com.fastride.infrastructure.api.account.v1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fastride.application.account.GetAccountOutput;
import com.fastride.application.account.GetAccountUseCase;
import com.fastride.application.account.SignUpInput;
import com.fastride.application.account.SignUpUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	private SignUpUseCase signUpUseCase;
	private GetAccountUseCase getAccountUseCase;

	public AccountController(SignUpUseCase signUpUseCase, GetAccountUseCase getAccountUseCase) {
		this.signUpUseCase = signUpUseCase;
		this.getAccountUseCase = getAccountUseCase;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public String signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
		SignUpInput signUpInput = new SignUpInput(signUpRequest.getName(), signUpRequest.getEmail(),
				signUpRequest.getCpf(), signUpRequest.getCarPlate(), signUpRequest.isPassenger(),
				signUpRequest.isDriver());
		return this.signUpUseCase.execute(signUpInput).toString();
	}

	@GetMapping("/{accountId}")
	@ResponseStatus(HttpStatus.OK)
	public GetAccountOutput getAccountById(@PathVariable String accountId) {
		return this.getAccountUseCase.execute(accountId);
	}

}
