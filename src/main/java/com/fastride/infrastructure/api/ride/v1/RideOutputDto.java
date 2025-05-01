package com.fastride.infrastructure.api.ride.v1;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record RideOutputDto(String rideId, String passengerId, String passengerName, String driverId, String driverName,
		BigDecimal fare, BigDecimal distance, BigDecimal startLatitude, BigDecimal startLongitude,
		BigDecimal destinationLatitude, BigDecimal destinationLongitude, String status, OffsetDateTime date) {
}
