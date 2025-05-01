package com.fastride.infrastructure.api.ride.v1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fastride.IntegrationTest;
import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.AccountBuilder;
import com.fastride.domain.account.usecase.SignUpUseCase;
import com.fastride.domain.ride.model.Position;
import com.fastride.domain.ride.model.Ride;
import com.fastride.domain.ride.usecase.RequestRideUseCase;

@IntegrationTest
@AutoConfigureMockMvc
class RideControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SignUpUseCase signUpUseCase;

	@Autowired
	private RequestRideUseCase requestRideUseCase;

	@Test
	void shouldCallPostEndpointForRideAndRequestARideForAPassengerSuccessfullyWithStatusCodeCreated() throws Exception {
		Account createdAccount = this.signUpUseCase.execute(AccountBuilder.getInstance().name("John Doe")
				.email("john@example.com").cpf("32421438098").passenger().build());
		String inputJson = """
				{
					"passengerId": "%s",
					"startLatitude": 2.5,
					"startLongitude": 5.3,
					"destinationLatitude": 7.8,
					"destinationLongitude": 1.2
				}
				""".formatted(createdAccount.getAccountId().toString());
		ResultActions result = mockMvc
				.perform(post("/rides").contentType(MediaType.APPLICATION_JSON).content(inputJson));
		result.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.rideId").isNotEmpty()).andExpect(jsonPath("$.rideStatus").value("REQUESTED"));
	}

	@Test
	void shouldCallGetEndpointForRideAndReturnAnExistingRide() throws Exception {
		Account createdAccount = this.signUpUseCase.execute(AccountBuilder.getInstance().name("John Doe")
				.email("john@example.com").cpf("32421438098").passenger().build());
		Ride ride = this.requestRideUseCase.execute(createdAccount.getAccountId(), getStart(), getDestination());

		ResultActions result = mockMvc
				.perform(get("/rides/{rideId}", ride.getRideId().toString()).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.rideId").value(ride.getRideId().toString()))
				.andExpect(jsonPath("$.passengerId").value(createdAccount.getAccountId().toString()))
				.andExpect(jsonPath("$.passengerName").value(createdAccount.getName().getContent()))
				.andExpect(jsonPath("$.driverId").value((Object) null))
				.andExpect(jsonPath("$.driverName").value((Object) null))
				.andExpect(jsonPath("$.fare").value((Object) null))
				.andExpect(jsonPath("$.distance").value((Object) null))
				.andExpect(jsonPath("$.startLatitude").value(ride.getStart().latitude()))
				.andExpect(jsonPath("$.startLongitude").value(ride.getStart().longitude()))
				.andExpect(jsonPath("$.destinationLatitude").value(ride.getDestination().latitude()))
				.andExpect(jsonPath("$.destinationLongitude").value(ride.getDestination().longitude()))
				.andExpect(jsonPath("$.status").value("REQUESTED")).andExpect(jsonPath("$.date").isNotEmpty());
	}

	@Test
	void shouldReturnStatusNotFoundWhenRideIdDoesNotExist() throws Exception {
		String nonExistentRideId = UUID.randomUUID().toString();

		ResultActions result = mockMvc
				.perform(get("/rides/{rideId}", nonExistentRideId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound()).andExpect(
				jsonPath("$.userMessage").value(String.format("Ride not found with the ID %s.", nonExistentRideId)));
	}

	private Position getStart() {
		return new Position(new BigDecimal("1.0"), new BigDecimal("1.0"));
	}

	private Position getDestination() {
		return new Position(new BigDecimal("2.0"), new BigDecimal("2.0"));
	}

}
