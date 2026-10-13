package com.api.alquilaya.services;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.alquilaya.exceptions.BusinessException;
import com.api.alquilaya.exceptions.NotFoundException;
import com.api.alquilaya.models.MaintenanceModel;
import com.api.alquilaya.repositories.IMaintenanceRepository;

@Service
public class MaintenanceService {
    @Autowired
    IMaintenanceRepository maintenanceRepository;

    @Autowired
    VehicleService vehicleService;

    public ArrayList<MaintenanceModel> obtenerMantenimientos() {
        return (ArrayList<MaintenanceModel>) maintenanceRepository.findAll();
    }

    public MaintenanceModel obtenerPorId(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe el mantenimiento con id: " + id));
    }

    public MaintenanceModel guardarMantenimiento(MaintenanceModel mantenimiento) {
        validar(mantenimiento);
        mantenimiento.setId(null);
        mantenimiento.setVehicle(vehicleService.obtenerPorId(mantenimiento.getVehicle().getId()));
        return maintenanceRepository.save(mantenimiento);
    }

    public MaintenanceModel actualizarMantenimiento(MaintenanceModel request, Long id) {
        validar(request);
        MaintenanceModel mantenimiento = obtenerPorId(id);
        mantenimiento.setVehicle(vehicleService.obtenerPorId(request.getVehicle().getId()));
        mantenimiento.setMaintenanceDate(request.getMaintenanceDate());
        mantenimiento.setDescription(request.getDescription());
        mantenimiento.setCost(request.getCost());
        mantenimiento.setMileage(request.getMileage());
        return maintenanceRepository.save(mantenimiento);
    }

    public void eliminarMantenimiento(Long id) {
        obtenerPorId(id);
        maintenanceRepository.deleteById(id);
    }

    // Historial de reparaciones de un vehículo, el más reciente primero
    public ArrayList<MaintenanceModel> historialPorVehiculo(Long vehicleId) {
        vehicleService.obtenerPorId(vehicleId);
        return maintenanceRepository.findByVehicleIdOrderByMaintenanceDateDesc(vehicleId);
    }

    private void validar(MaintenanceModel m) {
        if (m.getVehicle() == null || m.getVehicle().getId() == null) {
            throw new BusinessException("Envía \"vehicle\": {\"id\": X}");
        }
        if (m.getMaintenanceDate() == null) {
            throw new BusinessException("maintenanceDate es obligatorio (formato yyyy-MM-dd)");
        }
        if (m.getDescription() == null || m.getDescription().isBlank()) {
            throw new BusinessException("description es obligatorio");
        }
        if (m.getCost() == null || m.getCost().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("cost no puede ser negativo");
        }
        if (m.getMileage() != null && m.getMileage() < 0) {
            throw new BusinessException("mileage no puede ser negativo");
        }
    }
}
