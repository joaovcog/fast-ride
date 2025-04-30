package com.fastride.domain.ride.usecase;

import java.util.Optional;

import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.AccountRepository;
import com.fastride.domain.ride.model.Position;
import com.fastride.domain.ride.model.Ride;
import com.fastride.domain.ride.model.RideRepository;
import com.fastride.domain.shared.EntityId;
import com.fastride.domain.shared.ValidationException;

public class RequestRideUseCase {

	private final AccountRepository accountRepository;
	private final RideRepository rideRepository;

	public RequestRideUseCase(AccountRepository accountRepository, RideRepository rideRepository) {
		this.accountRepository = accountRepository;
		this.rideRepository = rideRepository;
	}

	public Ride execute(EntityId passengerId, Position start, Position destination) {
		Optional<Account> account = this.accountRepository.findById(passengerId);
		if (account.isEmpty())
			throw new ValidationException("The account ID provided is invalid. Please, enter a valid one.");
		if (!account.get().isPassenger())
			throw new ValidationException("The account type is not passenger. Please, check the account data.");
		if (this.rideRepository.hasRequestedRideByAccountId(passengerId))
			throw new ValidationException(
					"A ride has already been requested for the passenger. You must complete or cancel the existing ride before requesting another one.");
		return this.rideRepository.create(new Ride(account.get(), start, destination));
	}

}
