package com.api.alquilaya.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.alquilaya.models.MaintenanceModel;
import com.api.alquilaya.services.MaintenanceService;

@RestController
@RequestMapping("/maintenances")
public class MaintenanceController {
    @Autowired
    private MaintenanceService maintenanceService;

    @GetMapping
    public ArrayList<MaintenanceModel> obtenerMantenimientos() {
        return this.maintenanceService.obtenerMantenimientos();
    }

    @GetMapping(path = "/{id}")
    public MaintenanceModel obtenerPorId(@PathVariable("id") Long id) {
        return this.maintenanceService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MaintenanceModel guardarMantenimiento(@RequestBody MaintenanceModel mantenimiento) {
        return this.maintenanceService.guardarMantenimiento(mantenimiento);
    }

    @PutMapping(path = "/{id}")
    public MaintenanceModel actualizarMantenimiento(@RequestBody MaintenanceModel request, @PathVariable("id") Long id) {
        return this.maintenanceService.actualizarMantenimiento(request, id);
    }

    @DeleteMapping(path = "/{id}")
    public String eliminarPorId(@PathVariable("id") Long id) {
        this.maintenanceService.eliminarMantenimiento(id);
        return "Se eliminó el mantenimiento con id: " + id;
    }

    @GetMapping(path = "/vehicle/{vehicleId}")
    public ArrayList<MaintenanceModel> historialPorVehiculo(@PathVariable("vehicleId") Long vehicleId) {
        return this.maintenanceService.historialPorVehiculo(vehicleId);
    }
}
