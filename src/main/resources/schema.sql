CREATE TABLE IF NOT EXISTS t_sensor (
    se_id SERIAL PRIMARY KEY,
    se_name VARCHAR(64) NOT NULL,
    se_friendly_name VARCHAR(64) NOT NULL,
    se_created_by VARCHAR(64),
    se_created_on TIMESTAMP,
    se_modified_by VARCHAR(64),
    se_modified_on TIMESTAMP
);
