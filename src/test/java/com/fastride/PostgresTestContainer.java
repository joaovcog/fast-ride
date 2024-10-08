package com.fastride;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class PostgresTestContainer {

	@Container
	@ServiceConnection
	public static PostgreSQLContainer<?> postgreSqlContainer = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("fast_ride_integration_tests_db");

}
