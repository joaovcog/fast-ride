package com.fastride.domain.account.model;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {

	Account create(Account account);

	Optional<Account> findById(UUID accountId);

	Optional<Account> findByEmail(String email);

}
