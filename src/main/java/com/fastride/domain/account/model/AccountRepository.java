package com.fastride.domain.account.model;

import java.util.Optional;

import com.fastride.domain.shared.EntityId;

public interface AccountRepository {

	void create(Account account);

	Optional<Account> findById(EntityId accountId);

	Optional<Account> findByEmail(String email);

}
