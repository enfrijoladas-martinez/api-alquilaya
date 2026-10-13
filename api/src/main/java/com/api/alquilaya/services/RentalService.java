package com.api.alquilaya.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.alquilaya.exceptions.BusinessException;
import com.api.alquilaya.exceptions.NotFoundException;
import com.api.alquilaya.models.CustomerModel;
import com.api.alquilaya.models.RentalModel;
import com.api.alquilaya.models.RentalStatus;
import com.api.alquilaya.models.VehicleModel;
import com.api.alquilaya.models.VehicleStatus;
import com.api.alquilaya.repositories.IRentalRepository;

// Ciclo de vida del alquiler: Pendiente -> Activo -> Completado
@Service
public class RentalService {
    private static final List<RentalStatus> ESTADOS_QUE_OCUPAN = List.of(RentalStatus.Pendiente, RentalStatus.Activo);

    @Autowired
    IRentalRepository rentalRepository;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    CustomerService customerService;

    public ArrayList<RentalModel> obtenerAlquileres() {
        return (ArrayList<RentalModel>) rentalRepository.findAll();
    }

    public RentalModel obtenerPorId(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe el alquiler con id: " + id));
    }

    public RentalModel crearAlquiler(RentalModel request) {
        RentalModel alquiler = new RentalModel();
        prepararAlquiler(alquiler, request, null);
        alquiler.setStatus(RentalStatus.Pendiente);
        return rentalRepository.save(alquiler);
    }

    // Solo se pueden modificar alquileres que todavía no han empezado
    public RentalModel actualizarAlquiler(RentalModel request, Long id) {
        RentalModel alquiler = obtenerPorId(id);
        if (alquiler.getStatus() != RentalStatus.Pendiente) {
            throw new BusinessException("Solo se puede modificar un alquiler Pendiente (estado actual: " + alquiler.getStatus() + ")");
        }
        prepararAlquiler(alquiler, request, id);
        return rentalRepository.save(alquiler);
    }

    // Pendiente -> Activo: el cliente recoge el vehículo
    @Transactional
    public RentalModel iniciarAlquiler(Long id) {
        RentalModel alquiler = obtenerPorId(id);
        if (alquiler.getStatus() != RentalStatus.Pendiente) {
            throw new BusinessException("Solo se puede iniciar un alquiler Pendiente (estado actual: " + alquiler.getStatus() + ")");
        }
        VehicleModel vehiculo = alquiler.getVehicle();
        if (vehiculo.getStatus() != VehicleStatus.Disponible) {
            throw new BusinessException("El vehículo " + vehiculo.getLicensePlate() + " no está disponible (estado: " + vehiculo.getStatus() + ")");
        }
        vehiculo.actualizarEstado(VehicleStatus.Alquilado);
        vehicleService.guardar(vehiculo);
        alquiler.setStatus(RentalStatus.Activo);
        return rentalRepository.save(alquiler);
    }

    // Activo -> Completado: el cliente devuelve el vehículo
    @Transactional
    public RentalModel completarAlquiler(Long id) {
        RentalModel alquiler = obtenerPorId(id);
        if (alquiler.getStatus() != RentalStatus.Activo) {
            throw new BusinessException("Solo se puede completar un alquiler Activo (estado actual: " + alquiler.getStatus() + ")");
        }
        VehicleModel vehiculo = alquiler.getVehicle();
        vehiculo.actualizarEstado(VehicleStatus.Disponible);
        vehicleService.guardar(vehiculo);
        alquiler.setStatus(RentalStatus.Completado);
        return rentalRepository.save(alquiler);
    }

    public void eliminarAlquiler(Long id) {
        RentalModel alquiler = obtenerPorId(id);
        if (alquiler.getStatus() == RentalStatus.Activo) {
            throw new BusinessException("No se puede eliminar un alquiler Activo; complétalo primero");
        }
        rentalRepository.deleteById(id);
    }

    public ArrayList<RentalModel> buscarPorCliente(Long customerId) {
        customerService.obtenerPorId(customerId);
        return rentalRepository.findByCustomerId(customerId);
    }

    public ArrayList<RentalModel> buscarPorVehiculo(Long vehicleId) {
        vehicleService.obtenerPorId(vehicleId);
        return rentalRepository.findByVehicleId(vehicleId);
    }

    public ArrayList<RentalModel> buscarPorEstado(RentalStatus status) {
        return rentalRepository.findByStatus(status);
    }

    private void prepararAlquiler(RentalModel alquiler, RentalModel request, Long idActual) {
        if (request.getVehicle() == null || request.getVehicle().getId() == null
                || request.getCustomer() == null || request.getCustomer().getId() == null) {
            throw new BusinessException("Envía \"vehicle\": {\"id\": X} y \"customer\": {\"id\": Y}");
        }
        if (request.getRentalDate() == null || request.getReturnDate() == null) {
            throw new BusinessException("rentalDate y returnDate son obligatorios (formato yyyy-MM-dd)");
        }
        if (request.getReturnDate().isBefore(request.getRentalDate())) {
            throw new BusinessException("returnDate no puede ser anterior a rentalDate");
        }

        VehicleModel vehiculo = vehicleService.obtenerPorId(request.getVehicle().getId());
        CustomerModel cliente = customerService.obtenerPorId(request.getCustomer().getId());

        if (vehiculo.getStatus() == VehicleStatus.Mantenimiento) {
            throw new BusinessException("El vehículo " + vehiculo.getLicensePlate() + " está en mantenimiento");
        }
        if (rentalRepository.existsOverlap(vehiculo.getId(), ESTADOS_QUE_OCUPAN,
                request.getRentalDate(), request.getReturnDate(), idActual)) {
            throw new BusinessException("El vehículo " + vehiculo.getLicensePlate() + " ya tiene un alquiler en esas fechas");
        }

        alquiler.setVehicle(vehiculo);
        alquiler.setCustomer(cliente);
        alquiler.setRentalDate(request.getRentalDate());
        alquiler.setReturnDate(request.getReturnDate());
        alquiler.calcularPrecioTotal();
    }
}
