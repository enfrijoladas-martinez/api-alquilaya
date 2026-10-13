package com.api.alquilaya.services;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.alquilaya.exceptions.BusinessException;
import com.api.alquilaya.exceptions.NotFoundException;
import com.api.alquilaya.models.VehicleModel;
import com.api.alquilaya.models.VehicleStatus;
import com.api.alquilaya.repositories.IMaintenanceRepository;
import com.api.alquilaya.repositories.IRentalRepository;
import com.api.alquilaya.repositories.IVehicleRepository;

@Service
public class VehicleService {
    @Autowired
    IVehicleRepository vehicleRepository;

    @Autowired
    IRentalRepository rentalRepository;

    @Autowired
    IMaintenanceRepository maintenanceRepository;

    @Autowired
    RentalOfficeService officeService;

    public ArrayList<VehicleModel> obtenerVehiculos() {
        return (ArrayList<VehicleModel>) vehicleRepository.findAll();
    }

    public VehicleModel obtenerPorId(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe el vehículo con id: " + id));
    }

    public VehicleModel guardarVehiculo(VehicleModel vehiculo) {
        validar(vehiculo);
        if (vehicleRepository.existsByLicensePlate(vehiculo.getLicensePlate())) {
            throw new BusinessException("Ya existe un vehículo con la placa " + vehiculo.getLicensePlate());
        }
        vehiculo.setId(null);
        vehiculo.setOffice(officeService.obtenerPorId(vehiculo.getOffice().getId()));
        if (vehiculo.getStatus() == null) {
            vehiculo.actualizarEstado(VehicleStatus.Disponible);
        }
        return vehicleRepository.save(vehiculo);
    }

    public VehicleModel actualizarVehiculo(VehicleModel request, Long id) {
        validar(request);
        VehicleModel vehiculo = obtenerPorId(id);
        if (vehicleRepository.existsByLicensePlateAndIdNot(request.getLicensePlate(), id)) {
            throw new BusinessException("Ya existe otro vehículo con la placa " + request.getLicensePlate());
        }
        vehiculo.setMake(request.getMake());
        vehiculo.setModel(request.getModel());
        vehiculo.setYear(request.getYear());
        vehiculo.setLicensePlate(request.getLicensePlate());
        vehiculo.setDailyRate(request.getDailyRate());
        vehiculo.setOffice(officeService.obtenerPorId(request.getOffice().getId()));
        if (request.getStatus() != null) {
            cambiarEstado(vehiculo, request.getStatus());
        }
        return vehicleRepository.save(vehiculo);
    }

    public VehicleModel actualizarEstado(Long id, VehicleStatus status) {
        VehicleModel vehiculo = obtenerPorId(id);
        cambiarEstado(vehiculo, status);
        return vehicleRepository.save(vehiculo);
    }

    public void eliminarVehiculo(Long id) {
        obtenerPorId(id);
        if (rentalRepository.existsByVehicleId(id) || maintenanceRepository.existsByVehicleId(id)) {
            throw new BusinessException("No se puede eliminar el vehículo " + id + " porque tiene alquileres o mantenimientos registrados");
        }
        vehicleRepository.deleteById(id);
    }

    public ArrayList<VehicleModel> buscarPorEstado(VehicleStatus status) {
        return vehicleRepository.findByStatus(status);
    }

    public ArrayList<VehicleModel> buscarPorOficina(Long officeId) {
        officeService.obtenerPorId(officeId);
        return vehicleRepository.findByOfficeId(officeId);
    }

    // Usado por RentalService para marcar el vehículo como Alquilado / Disponible
    VehicleModel guardar(VehicleModel vehiculo) {
        return vehicleRepository.save(vehiculo);
    }

    // Un vehículo alquilado solo vuelve a estar disponible completando su alquiler
    private void cambiarEstado(VehicleModel vehiculo, VehicleStatus nuevo) {
        if (vehiculo.getStatus() == nuevo) {
            return;
        }
        if (vehiculo.getStatus() == VehicleStatus.Alquilado || nuevo == VehicleStatus.Alquilado) {
            throw new BusinessException("El estado Alquilado solo se gestiona iniciando o completando un alquiler (/rentals)");
        }
        vehiculo.actualizarEstado(nuevo);
    }

    private void validar(VehicleModel v) {
        if (v.getMake() == null || v.getMake().isBlank() || v.getModel() == null || v.getModel().isBlank()
                || v.getLicensePlate() == null || v.getLicensePlate().isBlank()) {
            throw new BusinessException("make, model y licensePlate son obligatorios");
        }
        if (v.getYear() == null || v.getYear() < 1900) {
            throw new BusinessException("year no es válido");
        }
        if (v.getDailyRate() == null || v.getDailyRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("dailyRate debe ser mayor que 0");
        }
        if (v.getOffice() == null || v.getOffice().getId() == null) {
            throw new BusinessException("Cada vehículo debe estar asociado a una oficina: envía \"office\": {\"id\": X}");
        }
    }
}
