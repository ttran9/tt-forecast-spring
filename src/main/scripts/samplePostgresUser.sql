-- This will prevent default users from creating tables
REVOKE CREATE ON SCHEMA public FROM public;

-- create the user
CREATE USER sample_user WITH PASSWORD 'testpw';

-- Grant the user the necessary permissions.
ALTER DEFAULT PRIVILEGES in SCHEMA public GRANT SELECT ON TABLES TO sample_user;
ALTER DEFAULT PRIVILEGES in SCHEMA public GRANT DELETE ON TABLES TO sample_user;
ALTER DEFAULT PRIVILEGES in SCHEMA public GRANT UPDATE ON TABLES TO sample_user;
ALTER DEFAULT PRIVILEGES in SCHEMA public GRANT INSERT ON TABLES TO sample_user;
