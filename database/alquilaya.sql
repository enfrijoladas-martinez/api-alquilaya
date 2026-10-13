-- =====================================================================
--  AlquilaYa S.A. - Reto 1 SOA
--  Script para MySQL Workbench: crea la base de datos, las 5 tablas
--  de la línea base y datos de prueba.
--  Ejecutar completo con el rayo (Execute) en Workbench.
-- =====================================================================

DROP DATABASE IF EXISTS alquilaya;
CREATE DATABASE alquilaya CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE alquilaya;

-- ---------------------------------------------------------------------
-- RentalOffice (Oficina de Alquiler)
-- ---------------------------------------------------------------------
CREATE TABLE rental_offices (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    office_name VARCHAR(255) NOT NULL,
    address     VARCHAR(255) NOT NULL,
    city        VARCHAR(255) NOT NULL,
    region      VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

-- ---------------------------------------------------------------------
-- Vehicle (Vehículo) - cada vehículo pertenece a una oficina
-- ---------------------------------------------------------------------
CREATE TABLE vehicles (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    make          VARCHAR(255) NOT NULL,
    model         VARCHAR(255) NOT NULL,
    year          INT          NOT NULL,
    license_plate VARCHAR(255) NOT NULL,
    daily_rate    DECIMAL(10,2) NOT NULL,
    status        ENUM('Disponible','Alquilado','Mantenimiento') NOT NULL DEFAULT 'Disponible',
    office_id     BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_vehicles_license_plate UNIQUE (license_plate),
    CONSTRAINT fk_vehicles_office FOREIGN KEY (office_id) REFERENCES rental_offices (id)
);

-- ---------------------------------------------------------------------
-- Customer (Cliente)
-- ---------------------------------------------------------------------
CREATE TABLE customers (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    first_name      VARCHAR(255) NOT NULL,
    last_name       VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    drivers_license VARCHAR(255) NOT NULL,
    phone           VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT uk_customers_email UNIQUE (email),
    CONSTRAINT uk_customers_drivers_license UNIQUE (drivers_license)
);

-- ---------------------------------------------------------------------
-- Rental (Alquiler) - relaciona un Vehicle con un Customer
-- ---------------------------------------------------------------------
CREATE TABLE rentals (
    id          BIGINT NOT NULL AUTO_INCREMENT,
    vehicle_id  BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    rental_date DATE   NOT NULL,
    return_date DATE   NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    status      ENUM('Pendiente','Activo','Completado') NOT NULL DEFAULT 'Pendiente',
    PRIMARY KEY (id),
    CONSTRAINT fk_rentals_vehicle  FOREIGN KEY (vehicle_id)  REFERENCES vehicles (id),
    CONSTRAINT fk_rentals_customer FOREIGN KEY (customer_id) REFERENCES customers (id),
    CONSTRAINT ck_rentals_dates CHECK (return_date >= rental_date)
);

-- ---------------------------------------------------------------------
-- Maintenance (Mantenimiento) - historial de cada vehículo
-- ---------------------------------------------------------------------
CREATE TABLE maintenances (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    vehicle_id       BIGINT       NOT NULL,
    maintenance_date DATE         NOT NULL,
    description      VARCHAR(255) NOT NULL,
    cost             DECIMAL(10,2) NOT NULL,
    mileage          INT,
    PRIMARY KEY (id),
    CONSTRAINT fk_maintenances_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles (id)
);

-- =====================================================================
--  Datos de prueba
-- =====================================================================
INSERT INTO rental_offices (office_name, address, city, region) VALUES
('AlquilaYa Centro',     'Calle 10 # 5-20',       'Bogotá',   'Cundinamarca'),
('AlquilaYa Aeropuerto', 'Av. El Dorado # 103-09', 'Bogotá',   'Cundinamarca'),
('AlquilaYa Poblado',    'Carrera 43A # 1-50',    'Medellín', 'Antioquia');

INSERT INTO vehicles (make, model, year, license_plate, daily_rate, status, office_id) VALUES
('Seat',    'León',    2023, '1234ABC', 40, 'Disponible',    1),
('Honda',   'CB650R',  2022, '5678DEF', 30, 'Disponible',    1),
('Renault', 'Master',  2021, '9012GHI', 60, 'Disponible',    2),
('Tesla',   'Model 3', 2024, '3456JKL', 70, 'Disponible',    3),
('Kia',     'Rio',     2020, '7777XYZ', 35, 'Mantenimiento', 3);

INSERT INTO customers (first_name, last_name, email, drivers_license, phone) VALUES
('Ana',   'García',    'ana@correo.es',   'LIC-0001', '3001234567'),
('Luis',  'Martínez',  'luis@correo.es',  'LIC-0002', '3109876543'),
('María', 'Rodríguez', 'maria@correo.es', 'LIC-0003', '3204567890');

-- totalPrice = daily_rate x días
INSERT INTO rentals (vehicle_id, customer_id, rental_date, return_date, total_price, status) VALUES
(1, 1, '2026-09-01', '2026-09-08', 280.00, 'Completado'),
(3, 2, '2026-10-10', '2026-10-13', 180.00, 'Pendiente');

INSERT INTO maintenances (vehicle_id, maintenance_date, description, cost, mileage) VALUES
(1, '2026-08-15', 'Cambio de aceite y filtros',     85.50, 15200),
(4, '2026-08-20', 'Revisión de batería y frenos',  120.00,  8300),
(5, '2026-09-28', 'Reparación de transmisión',     450.00, 62750);

-- Consultas útiles para verificar
-- SELECT * FROM rental_offices;
-- SELECT v.*, o.office_name FROM vehicles v JOIN rental_offices o ON o.id = v.office_id;
-- SELECT r.id, c.first_name, v.license_plate, r.rental_date, r.return_date, r.total_price, r.status
--   FROM rentals r JOIN customers c ON c.id = r.customer_id JOIN vehicles v ON v.id = r.vehicle_id;
