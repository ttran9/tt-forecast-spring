-- after tables have been generated this should be run. (step 3)
GRANT USAGE, SELECT ON SEQUENCE search_id_seq TO sample_user;
GRANT USAGE, SELECT ON SEQUENCE daily_forecast_id_seq TO sample_user;
GRANT USAGE, SELECT ON SEQUENCE hourly_forecast_id_seq TO sample_user;
GRANT USAGE, SELECT ON SEQUENCE custom_user_id_seq TO sample_user;
GRANT USAGE, SELECT ON SEQUENCE role_id_seq TO sample_user;
GRANT USAGE, SELECT ON SEQUENCE search_id_seq TO sample_user;