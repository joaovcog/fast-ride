package com.fastride.domain.ride.model;

import com.fastride.domain.shared.EntityId;

public interface RideRepository {

	Ride create(Ride ride);

	boolean hasRequestedRideByAccountId(EntityId accountId);

}
