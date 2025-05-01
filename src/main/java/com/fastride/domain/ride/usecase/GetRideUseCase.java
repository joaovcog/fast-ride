package com.fastride.domain.ride.usecase;

import org.springframework.stereotype.Component;

import com.fastride.domain.ride.RideNotFoundException;
import com.fastride.domain.ride.model.Ride;
import com.fastride.domain.ride.model.RideRepository;
import com.fastride.domain.shared.EntityId;

@Component
public class GetRideUseCase {

	private RideRepository rideRepository;

	public GetRideUseCase(RideRepository rideRepository) {
		this.rideRepository = rideRepository;
	}

	public Ride execute(EntityId rideId) {
		return this.rideRepository.findById(rideId).orElseThrow(
				() -> new RideNotFoundException(String.format("Ride not found with the ID %s.", rideId.toString())));
	}

}
