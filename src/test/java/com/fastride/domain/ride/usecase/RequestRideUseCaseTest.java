package com.fastride.domain.ride.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fastride.IntegrationTest;
import com.fastride.domain.account.usecase.GetAccountOutput;
import com.fastride.domain.account.usecase.GetAccountUseCase;
import com.fastride.domain.account.usecase.SignUpInput;
import com.fastride.domain.account.usecase.SignUpUseCase;
import com.fastride.domain.ride.model.Position;
import com.fastride.domain.ride.model.Ride;
import com.fastride.domain.ride.model.RideStatus;
import com.fastride.domain.shared.EntityId;
import com.fastride.domain.shared.ValidationException;

@IntegrationTest
class RequestRideUseCaseTest {

	private static final String ACCOUNT_NAME = "John Doe";
	private static final String ACCOUNT_EMAIL = "john@example.com";
	private static final String ACCOUNT_CPF = "32421438098";
	private static final String DRIVER_ACCOUNT_CAR_PLATE = "ABC1234";

	@Autowired
	private RequestRideUseCase requestRideUseCase;

	@Autowired
	private GetRideUseCase getRideUseCase;

	@Autowired
	private SignUpUseCase signUpUseCase;

	@Autowired
	private GetAccountUseCase getAccountUseCase;

	@Test
	void shouldRequestARideForThePassengerSuccessfully() {
		SignUpInput signUpInput = new SignUpInput(ACCOUNT_NAME, ACCOUNT_EMAIL, ACCOUNT_CPF, null, true, false);
		EntityId accountId = this.signUpUseCase.execute(signUpInput);
		GetAccountOutput createdAccount = this.getAccountUseCase.execute(accountId.toString());

		Ride ride = this.requestRideUseCase.execute(new EntityId(createdAccount.accountId()), getStart(),
				getDestination());
		ride = this.getRideUseCase.execute(ride.getRideId());

		assertNotNull(ride);
		assertNotNull(ride.getRideId());
		assertEquals(createdAccount.accountId(), ride.getPassenger().getAccountId().toString());
		assertEquals(getStart(), ride.getStart());
		assertEquals(getDestination(), ride.getDestination());
		assertNotNull(ride.getDate());
		assertEquals(RideStatus.REQUESTED, ride.getStatus());
	}

	@Test
	void shouldNotRequestARideWhenPassengerNotFound() {
		EntityId invalidPassengerId = new EntityId();

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.requestRideUseCase.execute(invalidPassengerId, getStart(), getDestination());
		});

		assertEquals("The account ID provided is invalid. Please, enter a valid one.", exception.getMessage());
	}

	@Test
	void shouldNotRequestARideForDriverAccount() {
		SignUpInput signUpInput = new SignUpInput(ACCOUNT_NAME, ACCOUNT_EMAIL, ACCOUNT_CPF, DRIVER_ACCOUNT_CAR_PLATE,
				false, true);
		EntityId accountId = this.signUpUseCase.execute(signUpInput);
		GetAccountOutput createdAccount = this.getAccountUseCase.execute(accountId.toString());

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.requestRideUseCase.execute(new EntityId(createdAccount.accountId()), getStart(), getDestination());
		});

		assertEquals("The account type is not passenger. Please, check the account data.", exception.getMessage());
	}

	@Test
	void shouldNotRequestARideWhenPassengerHasAnExistingRequestedRide() {
		SignUpInput signUpInput = new SignUpInput(ACCOUNT_NAME, ACCOUNT_EMAIL, ACCOUNT_CPF, null, true, false);
		EntityId accountId = this.signUpUseCase.execute(signUpInput);
		GetAccountOutput createdAccount = this.getAccountUseCase.execute(accountId.toString());

		this.requestRideUseCase.execute(new EntityId(createdAccount.accountId()), getStart(), getDestination());

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.requestRideUseCase.execute(new EntityId(createdAccount.accountId()), getStart(), getDestination());
		});

		assertEquals(
				"A ride has already been requested for the passenger. You must complete or cancel the existing ride before requesting another one.",
				exception.getMessage());
	}

	private Position getStart() {
		return new Position(new BigDecimal("1.0"), new BigDecimal("1.0"));
	}

	private Position getDestination() {
		return new Position(new BigDecimal("2.0"), new BigDecimal("2.0"));
	}

}
