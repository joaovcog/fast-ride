package com.fastride.domain.ride.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

import com.fastride.domain.shared.EntityId;

public class Ride {

	private EntityId rideId;
	private EntityId passengerId;
	private EntityId driverId;
	private BigDecimal fare;
	private BigDecimal distance;
	private Position start;
	private Position destination;
	private RideStatus status;
	private OffsetDateTime date;

	private Ride(RideBuilder builder) {
		this.rideId = new EntityId(builder.rideId);
		this.passengerId = new EntityId(builder.passengerId);
		if (builder.driverId != null) {
			this.driverId = new EntityId(builder.driverId);
		}
		this.fare = builder.fare;
		this.distance = builder.distance;
		this.start = new Position(builder.startLatitude, builder.startLongitude);
		this.destination = new Position(builder.destinationLatitude, builder.destinationLongitude);
		this.status = builder.status;
		this.date = builder.date;
	}

	public EntityId getRideId() {
		return rideId;
	}

	public EntityId getPassengerId() {
		return passengerId;
	}

	public EntityId getDriverId() {
		return driverId;
	}

	public BigDecimal getFare() {
		return fare;
	}

	public BigDecimal getDistance() {
		return distance;
	}

	public Position getStart() {
		return start;
	}

	public Position getDestination() {
		return destination;
	}

	public RideStatus getStatus() {
		return status;
	}

	public OffsetDateTime getDate() {
		return date;
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

	public static class RideBuilder {

		private String rideId;
		private String passengerId;
		private String driverId;
		private BigDecimal fare;
		private BigDecimal distance;
		private BigDecimal startLatitude;
		private BigDecimal startLongitude;
		private BigDecimal destinationLatitude;
		private BigDecimal destinationLongitude;
		private RideStatus status;
		private OffsetDateTime date;

		public static RideBuilder getInstance() {
			return new RideBuilder();
		}

		public RideBuilder rideId(String rideId) {
			this.rideId = rideId;
			return this;
		}

		public RideBuilder passengerId(String passengerId) {
			this.passengerId = passengerId;
			return this;
		}

		public RideBuilder driverId(String driverId) {
			this.driverId = driverId;
			return this;
		}

		public RideBuilder fare(BigDecimal fare) {
			this.fare = fare;
			return this;
		}

		public RideBuilder distance(BigDecimal distance) {
			this.distance = distance;
			return this;
		}

		public RideBuilder startLatitude(BigDecimal startLatitude) {
			this.startLatitude = startLatitude;
			return this;
		}

		public RideBuilder startLongitude(BigDecimal startLongitude) {
			this.startLongitude = startLongitude;
			return this;
		}

		public RideBuilder destinationLatitude(BigDecimal destinationLatitude) {
			this.destinationLatitude = destinationLatitude;
			return this;
		}

		public RideBuilder destinationLongitude(BigDecimal destinationLongitude) {
			this.destinationLongitude = destinationLongitude;
			return this;
		}

		public RideBuilder status(RideStatus status) {
			this.status = status;
			return this;
		}

		public RideBuilder date(OffsetDateTime date) {
			this.date = date;
			return this;
		}

		public RideBuilder request() {
			this.rideId = new EntityId().toString();
			this.status = RideStatus.REQUESTED;
			this.date = OffsetDateTime.now();
			return this;
		}

		public Ride build() {
			return new Ride(this);
		}

	}

}
