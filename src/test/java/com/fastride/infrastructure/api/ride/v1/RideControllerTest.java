package com.fastride.infrastructure.api.ride.v1;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.regex.Pattern;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fastride.IntegrationTest;
import com.fastride.domain.account.usecase.SignUpInput;
import com.fastride.domain.account.usecase.SignUpUseCase;
import com.fastride.domain.ride.usecase.RequestRideInput;
import com.fastride.domain.ride.usecase.RequestRideUseCase;
import com.fastride.domain.shared.EntityId;

@IntegrationTest
@AutoConfigureMockMvc
class RideControllerTest {

	private static final String VALID_ID_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
	private static final BigDecimal START_LAT_LONG = new BigDecimal("1.0");
	private static final BigDecimal DESTINATION_LAT_LONG = new BigDecimal("2.0");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SignUpUseCase signUpUseCase;

	@Autowired
	private RequestRideUseCase requestRideUseCase;

	@Test
	void shouldCallPostEndpointForRideAndRequestARideForAPassengerSuccessfullyWithStatusCodeCreated() throws Exception {
		SignUpInput signUpInput = new SignUpInput("John Doe", "john@example.com", "32421438098", null, true, false);
		String accountId = this.signUpUseCase.execute(signUpInput).toString();
		String inputJson = """
				{
					"passengerId": "%s",
					"startLatitude": 2.5,
					"startLongitude": 5.3,
					"destinationLatitude": 7.8,
					"destinationLongitude": 1.2
				}
				""".formatted(accountId);
		ResultActions result = mockMvc
				.perform(post("/rides").contentType(MediaType.APPLICATION_JSON).content(inputJson));
		String responseContent = result.andReturn().getResponse().getContentAsString();
		result.andExpect(status().isCreated()).andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
				.andExpect(content().string(Matchers.not(Matchers.emptyString())));
		assertTrue(Pattern.matches(VALID_ID_PATTERN, responseContent));
	}

	@Test
	void shouldCallGetEndpointForRideAndReturnAnExistingRide() throws Exception {
		SignUpInput signUpInput = new SignUpInput("John Doe", "john@example.com", "32421438098", null, true, false);
		EntityId accountId = this.signUpUseCase.execute(signUpInput);
		RequestRideInput requestRideInput = new RequestRideInput(accountId.toString(), START_LAT_LONG, START_LAT_LONG,
				DESTINATION_LAT_LONG, DESTINATION_LAT_LONG);
		EntityId rideId = this.requestRideUseCase.execute(requestRideInput);

		ResultActions result = mockMvc
				.perform(get("/rides/{rideId}", rideId.toString()).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.rideId").value(rideId.toString()))
				.andExpect(jsonPath("$.passengerId").value(accountId.toString()))
				// TODO: complete test assertion after refactoring RequestRideUseCase
//				.andExpect(jsonPath("$.passengerName").value(createdAccount.getName().getContent()))
				.andExpect(jsonPath("$.driverId").value((Object) null))
//				.andExpect(jsonPath("$.driverName").value((Object) null))
				.andExpect(jsonPath("$.fare").value((Object) null))
				.andExpect(jsonPath("$.distance").value((Object) null))
				.andExpect(jsonPath("$.startLatitude").value(requestRideInput.startLatitude()))
				.andExpect(jsonPath("$.startLongitude").value(requestRideInput.startLongitude()))
				.andExpect(jsonPath("$.destinationLatitude").value(requestRideInput.destinationLatitude()))
				.andExpect(jsonPath("$.destinationLongitude").value(requestRideInput.destinationLongitude()))
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

}
