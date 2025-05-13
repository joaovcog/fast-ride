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

	public GetRideOutput execute(String rideId) {
		Ride ride = this.rideRepository.findById(new EntityId(rideId)).orElseThrow(
				() -> new RideNotFoundException(String.format("Ride not found with the ID %s.", rideId.toString())));

		return new GetRideOutput(ride.getRideId().toString(), ride.getPassenger().getAccountId().toString(), null,
				ride.getFare(), ride.getDistance(), ride.getStart().latitude(), ride.getStart().longitude(),
				ride.getDestination().latitude(), ride.getDestination().longitude(), ride.getStatus().name(),
				ride.getDate());
	}

}