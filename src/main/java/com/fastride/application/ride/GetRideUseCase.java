package com.fastride.application.ride;

import org.springframework.stereotype.Component;

import com.fastride.domain.account.AccountNotFoundException;
import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.AccountRepository;
import com.fastride.domain.ride.RideNotFoundException;
import com.fastride.domain.ride.model.Ride;
import com.fastride.domain.ride.model.RideRepository;
import com.fastride.domain.shared.EntityId;

@Component
public class GetRideUseCase {

	private RideRepository rideRepository;
	private AccountRepository accountRepository;

	public GetRideUseCase(RideRepository rideRepository, AccountRepository accountRepository) {
		this.rideRepository = rideRepository;
		this.accountRepository = accountRepository;
	}

	public GetRideOutput execute(String rideId) {
		Ride ride = this.rideRepository.findById(new EntityId(rideId)).orElseThrow(
				() -> new RideNotFoundException(String.format("Ride not found with the ID %s.", rideId.toString())));
		Account account = this.accountRepository.findById(ride.getPassengerId())
				.orElseThrow(() -> new AccountNotFoundException(
						String.format("No account found for ID: %s", ride.getPassengerId().toString())));

		return new GetRideOutput(ride.getRideId().toString(), ride.getPassengerId().toString(),
				account.getName().getContent(), null, null, ride.getFare(), ride.getDistance(),
				ride.getStart().latitude(), ride.getStart().longitude(), ride.getDestination().latitude(),
				ride.getDestination().longitude(), ride.getStatus().name(), ride.getDate());
	}

}