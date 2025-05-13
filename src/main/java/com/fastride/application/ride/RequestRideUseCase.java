package com.fastride.application.ride;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.AccountRepository;
import com.fastride.domain.ride.model.Ride;
import com.fastride.domain.ride.model.Ride.RideBuilder;
import com.fastride.domain.ride.model.RideRepository;
import com.fastride.domain.shared.EntityId;
import com.fastride.domain.shared.ValidationException;

@Component
public class RequestRideUseCase {

	private final AccountRepository accountRepository;
	private final RideRepository rideRepository;

	public RequestRideUseCase(AccountRepository accountRepository, RideRepository rideRepository) {
		this.accountRepository = accountRepository;
		this.rideRepository = rideRepository;
	}

	public EntityId execute(RequestRideInput requestRideInput) {
		EntityId passengerId = new EntityId(requestRideInput.passengerId());
		Optional<Account> account = this.accountRepository.findById(passengerId);
		if (account.isEmpty())
			throw new ValidationException("The account ID provided is invalid. Please, enter a valid one.");
		if (!account.get().isPassenger())
			throw new ValidationException("The account type is not passenger. Please, check the account data.");
		if (this.rideRepository.hasRequestedRideByAccountId(passengerId))
			throw new ValidationException(
					"A ride has already been requested for the passenger. You must complete or cancel the existing ride before requesting another one.");
		Ride ride = RideBuilder.getInstance().request().passengerId(account.get().getAccountId().toString())
				.startLatitude(requestRideInput.startLatitude()).startLongitude(requestRideInput.startLongitude())
				.destinationLatitude(requestRideInput.destinationLatitude())
				.destinationLongitude(requestRideInput.destinationLongitude()).build();
		this.rideRepository.create(ride);
		return ride.getRideId();
	}

}
