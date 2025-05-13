package com.fastride.infrastructure.api.ride.v1;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record RequestRideRequest(@NotNull(message = "Passenger ID is required.") String passengerId,
		@NotNull BigDecimal startLatitude, @NotNull BigDecimal startLongitude, @NotNull BigDecimal destinationLatitude,
		@NotNull BigDecimal destinationLongitude) {
}
