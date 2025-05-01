package com.fastride.domain.ride;

import com.fastride.domain.shared.ResourceNotFoundException;

public class RideNotFoundException extends ResourceNotFoundException {

	private static final long serialVersionUID = 1L;

	public RideNotFoundException(String message) {
		super(message);
	}

}
