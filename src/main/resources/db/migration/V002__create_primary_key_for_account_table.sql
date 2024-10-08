ALTER TABLE fast_ride.account DROP CONSTRAINT IF EXISTS account_primary_key;
ALTER TABLE fast_ride.account ADD CONSTRAINT account_primary_key PRIMARY KEY (account_id);