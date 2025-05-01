package com.fastride.domain.ride.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fastride.IntegrationTest;
import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.AccountBuilder;
import com.fastride.domain.account.usecase.SignUpUseCase;
import com.fastride.domain.ride.model.Position;
import com.fastride.domain.ride.model.Ride;
import com.fastride.domain.ride.model.RideStatus;
import com.fastride.domain.shared.EntityId;
import com.fastride.domain.shared.ValidationException;

@IntegrationTest
class RequestRideUseCaseTest {

	@Autowired
	private RequestRideUseCase requestRideUseCase;

	@Autowired
	private GetRideUseCase getRideUseCase;

	@Autowired
	private SignUpUseCase signUpUseCase;

	@Test
	void shouldRequestARideForThePassengerSuccessfully() {
		Account account = AccountBuilder.getInstance().name("John Doe").email("john@example.com").cpf("32421438098")
				.passenger().build();
		Account createdAccount = this.signUpUseCase.execute(account);

		Ride ride = this.requestRideUseCase.execute(createdAccount.getAccountId(), getStart(), getDestination());
		ride = this.getRideUseCase.execute(ride.getRideId());

		assertNotNull(ride);
		assertNotNull(ride.getRideId());
		assertEquals(createdAccount.getAccountId(), ride.getPassenger().getAccountId());
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
		Account account = AccountBuilder.getInstance().name("John Doe").email("john@example.com").cpf("32421438098")
				.driver().carPlate("AAA1234").build();
		Account createdAccount = this.signUpUseCase.execute(account);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.requestRideUseCase.execute(createdAccount.getAccountId(), getStart(), getDestination());
		});

		assertEquals("The account type is not passenger. Please, check the account data.", exception.getMessage());
	}

	@Test
	void shouldNotRequestARideWhenPassengerHasAnExistingRequestedRide() {
		Account account = AccountBuilder.getInstance().name("John Doe").email("john@example.com").cpf("32421438098")
				.passenger().build();
		Account createdAccount = this.signUpUseCase.execute(account);

		this.requestRideUseCase.execute(createdAccount.getAccountId(), getStart(), getDestination());

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.requestRideUseCase.execute(createdAccount.getAccountId(), getStart(), getDestination());
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
