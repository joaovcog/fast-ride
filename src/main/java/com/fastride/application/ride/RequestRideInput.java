package com.fastride.application.ride;

import java.math.BigDecimal;

public record RequestRideInput(String passengerId, BigDecimal startLatitude, BigDecimal startLongitude,
		BigDecimal destinationLatitude, BigDecimal destinationLongitude) {
}
