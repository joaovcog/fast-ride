package com.fastride.domain;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {

	Account create(Account account);

	Optional<Account> findById(UUID accountId);

	Optional<Account> findByEmail(String email);

}
