package com.fastride.infrastructure.api.ride.v1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fastride.domain.ride.model.Position;
import com.fastride.domain.ride.model.Ride;
import com.fastride.domain.ride.usecase.RequestRideUseCase;
import com.fastride.domain.shared.EntityId;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rides")
public class RideController {

	private RequestRideUseCase requestRideUseCase;

	public RideController(RequestRideUseCase requestRideUseCase) {
		this.requestRideUseCase = requestRideUseCase;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RideOutputDto requestRide(@RequestBody @Valid RideInputDto rideInput) {
		Position start = new Position(rideInput.startLatitude(), rideInput.startLongitude());
		Position destination = new Position(rideInput.destinationLatitude(), rideInput.destinationLongitude());
		Ride ride = this.requestRideUseCase.execute(new EntityId(rideInput.passengerId()), start, destination);
		return new RideOutputDto(ride.getRideId().toString(), ride.getStatus().name());
	}

}
