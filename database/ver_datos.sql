-- Ver los datos de AlquilaYa
-- Ejecuta todo con el rayo (Ctrl+Shift+Enter): cada SELECT abre su propia pestaña de resultados abajo.
USE alquilaya;

SELECT * FROM rental_offices;

SELECT v.id, v.make, v.model, v.year, v.license_plate, v.daily_rate, v.status, o.office_name
FROM vehicles v
JOIN rental_offices o ON o.id = v.office_id;

SELECT * FROM customers;

SELECT r.id, CONCAT(c.first_name, ' ', c.last_name) AS cliente, v.license_plate,
       r.rental_date, r.return_date, r.total_price, r.status
FROM rentals r
JOIN customers c ON c.id = r.customer_id
JOIN vehicles v ON v.id = r.vehicle_id;

SELECT m.id, v.license_plate, m.maintenance_date, m.description, m.cost, m.mileage
FROM maintenances m
JOIN vehicles v ON v.id = m.vehicle_id;
