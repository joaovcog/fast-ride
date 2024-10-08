ALTER TABLE fast_ride.account DROP CONSTRAINT IF EXISTS account_email_unique_key;
ALTER TABLE fast_ride.account ADD CONSTRAINT account_email_unique_key UNIQUE (email);

ALTER TABLE fast_ride.account DROP CONSTRAINT IF EXISTS account_cpf_unique_key;
ALTER TABLE fast_ride.account ADD CONSTRAINT account_cpf_unique_key UNIQUE (cpf);