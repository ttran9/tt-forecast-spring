-- create the user (step 1)
CREATE USER sample_user WITH PASSWORD 'testpw';

-- This will prevent default users from creating tables (step 1)
REVOKE CREATE ON SCHEMA public FROM public;

-- Grant the user the necessary permissions. (step 1)
ALTER DEFAULT PRIVILEGES in SCHEMA public GRANT SELECT ON TABLES TO sample_user;
ALTER DEFAULT PRIVILEGES in SCHEMA public GRANT DELETE ON TABLES TO sample_user;
ALTER DEFAULT PRIVILEGES in SCHEMA public GRANT UPDATE ON TABLES TO sample_user;
ALTER DEFAULT PRIVILEGES in SCHEMA public GRANT INSERT ON TABLES TO sample_user;

-- after tables have been generated this should be run. (step 3)
GRANT USAGE, SELECT ON SEQUENCE search_id_seq TO sample_user;
GRANT USAGE, SELECT ON SEQUENCE daily_forecast_id_seq TO sample_user;
GRANT USAGE, SELECT ON SEQUENCE hourly_forecast_id_seq TO sample_user;
GRANT USAGE, SELECT ON SEQUENCE custom_user_id_seq TO sample_user;
GRANT USAGE, SELECT ON SEQUENCE role_id_seq TO sample_user;
GRANT USAGE, SELECT ON SEQUENCE search_id_seq TO sample_user;