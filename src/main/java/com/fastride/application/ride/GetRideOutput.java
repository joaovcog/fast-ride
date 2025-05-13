package com.fastride.application.ride;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record GetRideOutput(String rideId, String passengerId, String driverId, BigDecimal fare, BigDecimal distance,
		BigDecimal startLatitude, BigDecimal startLongitude, BigDecimal destinationLatitude,
		BigDecimal destinationLongitude, String status, OffsetDateTime date) {
}