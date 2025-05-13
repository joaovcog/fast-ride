package com.fastride.infrastructure.api.ride.v1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fastride.domain.ride.usecase.GetRideOutput;
import com.fastride.domain.ride.usecase.GetRideUseCase;
import com.fastride.domain.ride.usecase.RequestRideInput;
import com.fastride.domain.ride.usecase.RequestRideUseCase;

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
	public String requestRide(@RequestBody @Valid RequestRideRequest requestRideRequest) {
		RequestRideInput requestRideInput = new RequestRideInput(requestRideRequest.passengerId(),
				requestRideRequest.startLatitude(), requestRideRequest.startLongitude(),
				requestRideRequest.destinationLatitude(), requestRideRequest.destinationLongitude());
		return this.requestRideUseCase.execute(requestRideInput).toString();
	}

	@GetMapping("/{rideId}")
	@ResponseStatus(HttpStatus.OK)
	public GetRideOutput getRide(@PathVariable String rideId) {
		return this.getRideUseCase.execute(rideId);
	}

}
