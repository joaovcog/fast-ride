package com.fastride.infrastructure.persistence;

import static java.lang.Boolean.TRUE;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fastride.domain.ride.model.Ride;
import com.fastride.domain.ride.model.RideRepository;
import com.fastride.domain.ride.model.RideStatus;
import com.fastride.domain.shared.EntityId;

@Repository
public class RideRepositoryImpl implements RideRepository {

	private JdbcTemplate jdbcTemplate;

	public RideRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Ride create(Ride ride) {
		String insertQuery = "INSERT INTO fast_ride.ride (ride_id, passenger_id, status, start_latitude, start_longitude, destination_latitude, destination_longitude, date) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		this.jdbcTemplate.update(insertQuery, ride.getRideId().toUUID(), ride.getPassenger().getAccountId().toUUID(),
				ride.getStatus().name(), ride.getStart().latitude(), ride.getStart().longitude(),
				ride.getDestination().latitude(), ride.getDestination().longitude(), ride.getDate());
		return ride;
	}

	@Override
	public boolean hasRequestedRideByAccountId(EntityId accountId) {
		String selectQuery = "SELECT EXISTS (SELECT 1 FROM fast_ride.ride WHERE passenger_id = ? and status = ?)";
		Boolean existsRequestedRide = this.jdbcTemplate.queryForObject(selectQuery, Boolean.class, accountId.toUUID(),
				RideStatus.REQUESTED.name());
		return TRUE.equals(existsRequestedRide);
	}

}
