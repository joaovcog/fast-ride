package com.fastride.domain.ride.model;

import java.util.Optional;

import com.fastride.domain.shared.EntityId;

public interface RideRepository {

	void create(Ride ride);

	boolean hasRequestedRideByAccountId(EntityId accountId);

	Optional<Ride> findById(EntityId rideId);

}
