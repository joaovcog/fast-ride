package com.fastride.domain.ride.usecase;

import java.math.BigDecimal;

public record RequestRideInput(String passengerId, BigDecimal startLatitude, BigDecimal startLongitude,
		BigDecimal destinationLatitude, BigDecimal destinationLongitude) {
}
