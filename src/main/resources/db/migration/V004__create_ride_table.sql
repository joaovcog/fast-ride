create table fast_ride.ride (
	ride_id uuid not null,
	passenger_id uuid not null,
	driver_id uuid,
	status text,
	fare numeric,
	distance numeric,
	start_latitude numeric,
	start_longitude numeric,
	destination_latitude numeric,
	destination_longitude numeric,
	date timestamp not null,
	CONSTRAINT ride_primary_key PRIMARY KEY (ride_id)
);