create table fast_ride.account (
	account_id uuid,
	name text,
	email text,
	cpf text,
	car_plate text,
	is_passenger boolean,
	is_driver boolean,
	password text,
	password_algorithm text,
	salt text
);