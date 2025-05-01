package com.fastride.infrastructure.api.ride.v1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fastride.domain.ride.model.Position;
import com.fastride.domain.ride.model.Ride;
import com.fastride.domain.ride.usecase.GetRideUseCase;
import com.fastride.domain.ride.usecase.RequestRideUseCase;
import com.fastride.domain.shared.EntityId;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rides")
public class RideController {

	private RequestRideUseCase requestRideUseCase;
	private GetRideUseCase getRideUseCase;

	public RideController(RequestRideUseCase requestRideUseCase, GetRideUseCase getRideUseCase) {
		this.requestRideUseCase = requestRideUseCase;
		this.getRideUseCase = getRideUseCase;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RideSummaryOutputDto requestRide(@RequestBody @Valid RideInputDto rideInput) {
		Position start = new Position(rideInput.startLatitude(), rideInput.startLongitude());
		Position destination = new Position(rideInput.destinationLatitude(), rideInput.destinationLongitude());
		Ride ride = this.requestRideUseCase.execute(new EntityId(rideInput.passengerId()), start, destination);
		return new RideSummaryOutputDto(ride.getRideId().toString(), ride.getStatus().name());
	}

	@GetMapping("/{rideId}")
	@ResponseStatus(HttpStatus.OK)
	public RideOutputDto getRide(@PathVariable String rideId) {
		Ride ride = this.getRideUseCase.execute(new EntityId(rideId));
		return new RideOutputDto(ride.getRideId().toString(), ride.getPassenger().getAccountId().toString(),
				ride.getPassenger().getName().getContent(), null, null, ride.getFare(), ride.getDistance(),
				ride.getStart().latitude(), ride.getStart().longitude(), ride.getDestination().latitude(),
				ride.getDestination().longitude(), ride.getStatus().name(), ride.getDate());
	}

}
