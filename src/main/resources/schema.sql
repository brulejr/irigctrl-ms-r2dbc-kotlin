CREATE TABLE IF NOT EXISTS t_schedule (
    sc_id SERIAL PRIMARY KEY,
    sc_name VARCHAR(64) NOT NULL,
    sc_friendly_name VARCHAR(64) NOT NULL,
    sc_created_on TIMESTAMP,
    sc_modified_on TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_sensor (
    se_id SERIAL PRIMARY KEY,
    se_name VARCHAR(64) NOT NULL,
    se_friendly_name VARCHAR(64) NOT NULL,
    se_created_on TIMESTAMP,
    se_modified_on TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_measurement (
    me_id SERIAL PRIMARY KEY,
    me_se_id NUMBER NOT NULL,
    me_timestamp TIMESTAMP NOT NULL,
    me_type VARCHAR(64) NOT NULL,
    me_value DOUBLE NOT NULL,
    me_units VARCHAR(64)
);
