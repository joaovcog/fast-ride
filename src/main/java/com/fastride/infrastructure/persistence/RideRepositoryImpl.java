package com.fastride.infrastructure.persistence;

import static java.lang.Boolean.TRUE;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.AccountBuilder;
import com.fastride.domain.ride.model.Ride;
import com.fastride.domain.ride.model.Ride.RideBuilder;
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

	@Override
	public Optional<Ride> findById(EntityId rideId) {
		String selectQuery = "SELECT r.*, a.* FROM fast_ride.ride r inner join fast_ride.account a on a.account_id = r.passenger_id WHERE ride_id = ?";
		List<Ride> existingRide = this.jdbcTemplate.query(selectQuery, rideRowMapper(), rideId.toUUID());
		return existingRide.stream().findFirst();
	}

	private RowMapper<Ride> rideRowMapper() {
		return (resultSet, rowNumber) -> {
			Account passengerAccount = AccountBuilder.getInstance()
					.accountId(resultSet.getObject("account_id", UUID.class)).name(resultSet.getString("name"))
					.email(resultSet.getString("email")).cpf(resultSet.getString("cpf"))
					.carPlate(resultSet.getString("car_plate")).passenger(resultSet.getBoolean("is_passenger"))
					.driver(resultSet.getBoolean("is_driver")).build();

			return RideBuilder.getInstance().rideId(resultSet.getObject("ride_id", UUID.class).toString())
					.passenger(passengerAccount).fare(resultSet.getBigDecimal("fare"))
					.distance(resultSet.getBigDecimal("distance"))
					.startLatitude(resultSet.getBigDecimal("start_latitude"))
					.startLongitude(resultSet.getBigDecimal("start_longitude"))
					.destinationLatitude(resultSet.getBigDecimal("destination_latitude"))
					.destinationLongitude(resultSet.getBigDecimal("destination_longitude"))
					.status(RideStatus.valueOf(resultSet.getString("status")))
					.date(resultSet.getObject("date", OffsetDateTime.class)).build();

		};
	}

}
