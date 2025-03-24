CREATE SCHEMA IF NOT EXISTS profile;
CREATE SCHEMA IF NOT EXISTS device;

CREATE TABLE IF NOT EXISTS profile.roles
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT
);

CREATE EXTENSION IF NOT EXISTS citext;


CREATE TABLE IF NOT EXISTS profile.users
(
    id SERIAL PRIMARY KEY,
    username CITEXT NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email CITEXT NOT NULL UNIQUE,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS profile.user_roles
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES profile.users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES profile.roles (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS device.device_entity
(
    id SERIAL PRIMARY KEY,
    device_idx INT NOT NULL UNIQUE,
    location VARCHAR(255),
    address VARCHAR(1000),
    last_heartbeat TIMESTAMP,
    user_id INT NOT NULL,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES profile.users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS device.control_point
(
    id SERIAL PRIMARY KEY,
    control_point_idx INT NOT NULL UNIQUE,
    state VARCHAR(255),
    alarmed BOOLEAN,
    shelter BOOLEAN NOT NULL,
    device_entity_id INT NOT NULL,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (device_entity_id) REFERENCES device.device_entity (id) ON DELETE CASCADE
);





