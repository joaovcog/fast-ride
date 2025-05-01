package com.fastride.domain.account.exception;

import com.fastride.domain.shared.ResourceNotFoundException;

public class AccountNotFoundException extends ResourceNotFoundException {

	private static final long serialVersionUID = 1L;

	public AccountNotFoundException(String message) {
		super(message);
	}

}
