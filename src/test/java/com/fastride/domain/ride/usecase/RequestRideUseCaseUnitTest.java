package com.fastride.domain.ride.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fastride.domain.account.model.Account;
import com.fastride.domain.account.model.AccountBuilder;
import com.fastride.domain.account.model.AccountRepository;
import com.fastride.domain.ride.model.Position;
import com.fastride.domain.ride.model.Ride;
import com.fastride.domain.ride.model.RideRepository;
import com.fastride.domain.ride.model.RideStatus;
import com.fastride.domain.shared.EntityId;
import com.fastride.domain.shared.ValidationException;

@ExtendWith(MockitoExtension.class)
class RequestRideUseCaseUnitTest {

	private static final String ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE = "The account ID provided is invalid. Please, enter a valid one.";
	private static final String INVALID_PASSENGER_ACCOUNT_EXCEPTION_MESSAGE = "The account type is not passenger. Please, check the account data.";

	private RequestRideUseCase requestRideUseCase;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private RideRepository rideRepository;

	@Captor
	private ArgumentCaptor<Ride> rideCaptor;

	@BeforeEach
	public void setup() {
		this.requestRideUseCase = new RequestRideUseCase(accountRepository, rideRepository);
	}

	@Test
	void shouldRequestARideSuccessfully() {
		Account passengerAccount = getValidPassengerAccount();
		EntityId passengerId = passengerAccount.getAccountId();
		when(this.accountRepository.findById(passengerId)).thenReturn(Optional.of(passengerAccount));
		when(this.rideRepository.hasRequestedRideByAccountId(passengerId)).thenReturn(false);

		this.requestRideUseCase.execute(passengerId, getStart(), getDestination());
		verify(rideRepository).create(rideCaptor.capture());
		Ride ride = rideCaptor.getValue();
		assertNotNull(ride);
		assertNotNull(ride.getRideId());
		assertEquals(passengerId, ride.getPassenger().getAccountId());
		assertEquals(getStart(), ride.getStart());
		assertEquals(getDestination(), ride.getDestination());
		assertNotNull(ride.getDate());
		assertEquals(RideStatus.REQUESTED, ride.getStatus());
	}

	@ParameterizedTest
	@MethodSource("invalidAccountGenerator")
	void shouldNotRequestARideWhenAccountNotFoundForProvidedIdOrAccountNotFlaggedAsPassenger(
			InvalidAccountScenarioWrapper invalidAccountScenario) {
		when(accountRepository.findById(any(EntityId.class))).thenReturn(invalidAccountScenario.getAccountForInput());

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.requestRideUseCase.execute(new EntityId(), getStart(), getDestination());
		});

		assertEquals(invalidAccountScenario.getExpectedExceptionMessage(), exception.getMessage());
		verify(this.rideRepository, never()).create(any(Ride.class));
	}

	@Test
	void shouldNotRequestARideWhenPassengerHasAnExistingRequestedRide() {
		Account account = getValidPassengerAccount();
		EntityId accountId = account.getAccountId();
		when(this.accountRepository.findById(accountId)).thenReturn(Optional.of(account));
		when(this.rideRepository.hasRequestedRideByAccountId(accountId)).thenReturn(true);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			this.requestRideUseCase.execute(accountId, getStart(), getDestination());
		});

		assertEquals(
				"A ride has already been requested for the passenger. You must complete or cancel the existing ride before requesting another one.",
				exception.getMessage());
		verify(this.rideRepository, never()).create(any(Ride.class));
	}

	private Position getStart() {
		return new Position(new BigDecimal("1.0"), new BigDecimal("1.0"));
	}

	private Position getDestination() {
		return new Position(new BigDecimal("2.0"), new BigDecimal("2.0"));
	}

	private Account getValidPassengerAccount() {
		return AccountBuilder.getInstance().accountId(UUID.randomUUID()).name("John Doe").email("john@example.com")
				.cpf("32421438098").passenger().build();
	}

	private static Stream<Arguments> invalidAccountGenerator() {
		return Stream.of(
				Arguments.of(new InvalidAccountScenarioWrapper(Optional.empty(), ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE)),
				Arguments.of(new InvalidAccountScenarioWrapper(Optional.of(getInvalidPassengerAccount()),
						INVALID_PASSENGER_ACCOUNT_EXCEPTION_MESSAGE)));
	}

	private static Account getInvalidPassengerAccount() {
		return AccountBuilder.getInstance().accountId(UUID.randomUUID()).name("John Doe").email("john@example.com")
				.cpf("32421438098").build();
	}

	static class InvalidAccountScenarioWrapper {
		private Optional<Account> accountForInput;
		private String expectedExceptionMessage;

		public InvalidAccountScenarioWrapper(Optional<Account> accountForInput, String expectedExceptionMessage) {
			this.accountForInput = accountForInput;
			this.expectedExceptionMessage = expectedExceptionMessage;
		}

		public Optional<Account> getAccountForInput() {
			return accountForInput;
		}

		public String getExpectedExceptionMessage() {
			return expectedExceptionMessage;
		}
	}

}
