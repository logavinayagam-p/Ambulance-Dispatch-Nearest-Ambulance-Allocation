CREATE DATABASE IF NOT EXISTS ambulance_dispatch_db;
USE ambulance_dispatch_db;

DROP TABLE IF EXISTS dispatches;
DROP TABLE IF EXISTS emergency_calls;
DROP TABLE IF EXISTS ambulances;
DROP TABLE IF EXISTS zones;

CREATE TABLE zones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    zone_name VARCHAR(100) NOT NULL,
    zone_code VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE ambulances (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ambulance_number VARCHAR(50) NOT NULL UNIQUE,
    driver_name VARCHAR(100) NOT NULL,
    driver_phone VARCHAR(20) NOT NULL,
    ambulance_type VARCHAR(50) NOT NULL,
    zone_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    FOREIGN KEY (zone_id) REFERENCES zones(id) ON DELETE CASCADE
);

CREATE TABLE emergency_calls (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    caller_name VARCHAR(100) NOT NULL,
    caller_phone VARCHAR(20) NOT NULL,
    emergency_type VARCHAR(50) NOT NULL,
    emergency_location VARCHAR(255) NOT NULL,
    zone_id BIGINT NOT NULL,
    call_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    assigned_ambulance_id BIGINT,
    FOREIGN KEY (zone_id) REFERENCES zones(id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_ambulance_id) REFERENCES ambulances(id) ON DELETE SET NULL
);

CREATE TABLE dispatches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    emergency_call_id BIGINT NOT NULL,
    ambulance_id BIGINT NOT NULL,
    dispatch_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    response_time VARCHAR(50) DEFAULT 'Immediate',
    status VARCHAR(20) NOT NULL DEFAULT 'DISPATCHED',
    FOREIGN KEY (emergency_call_id) REFERENCES emergency_calls(id) ON DELETE CASCADE,
    FOREIGN KEY (ambulance_id) REFERENCES ambulances(id) ON DELETE CASCADE
);