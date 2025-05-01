package com.fastride.domain.ride.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

import com.fastride.domain.account.model.Account;
import com.fastride.domain.shared.EntityId;

public class Ride {

	private EntityId rideId;
	private Account passenger;
	private Account driver;
	private BigDecimal fare;
	private BigDecimal distance;
	private Position start;
	private Position destination;
	private RideStatus status;
	private OffsetDateTime date;

	public Ride(Account passenger, Position start, Position destination) {
		this.passenger = passenger;
		this.start = start;
		this.destination = destination;
		this.rideId = new EntityId();
		this.status = RideStatus.REQUESTED;
		this.date = OffsetDateTime.now();
	}

	public EntityId getRideId() {
		return rideId;
	}

	void setRideId(EntityId rideId) {
		this.rideId = rideId;
	}

	public Account getPassenger() {
		return passenger;
	}

	void setPassenger(Account passenger) {
		this.passenger = passenger;
	}

	public Account getDriver() {
		return driver;
	}

	void setDriver(Account driver) {
		this.driver = driver;
	}

	public BigDecimal getFare() {
		return fare;
	}

	void setFare(BigDecimal fare) {
		this.fare = fare;
	}

	public BigDecimal getDistance() {
		return distance;
	}

	void setDistance(BigDecimal distance) {
		this.distance = distance;
	}

	public Position getStart() {
		return start;
	}

	void setStart(Position start) {
		this.start = start;
	}

	public Position getDestination() {
		return destination;
	}

	void setDestination(Position destination) {
		this.destination = destination;
	}

	public RideStatus getStatus() {
		return status;
	}

	void setStatus(RideStatus status) {
		this.status = status;
	}

	public OffsetDateTime getDate() {
		return date;
	}

	void setDate(OffsetDateTime date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		return Objects.hash(rideId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ride other = (Ride) obj;
		return Objects.equals(rideId, other.rideId);
	}

}
