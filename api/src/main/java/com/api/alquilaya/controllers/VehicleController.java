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

import com.api.alquilaya.models.VehicleModel;
import com.api.alquilaya.models.VehicleStatus;
import com.api.alquilaya.services.VehicleService;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public ArrayList<VehicleModel> obtenerVehiculos() {
        return this.vehicleService.obtenerVehiculos();
    }

    @GetMapping(path = "/{id}")
    public VehicleModel obtenerPorId(@PathVariable("id") Long id) {
        return this.vehicleService.obtenerPorId(id);
    }

    // Método obtenerDetalle() del diagrama UML
    @GetMapping(path = "/{id}/detalle")
    public String obtenerDetalle(@PathVariable("id") Long id) {
        return this.vehicleService.obtenerPorId(id).obtenerDetalle();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleModel guardarVehiculo(@RequestBody VehicleModel vehiculo) {
        return this.vehicleService.guardarVehiculo(vehiculo);
    }

    @PutMapping(path = "/{id}")
    public VehicleModel actualizarVehiculo(@RequestBody VehicleModel request, @PathVariable("id") Long id) {
        return this.vehicleService.actualizarVehiculo(request, id);
    }

    // Ej: PUT /vehicles/1/status/Mantenimiento
    @PutMapping(path = "/{id}/status/{status}")
    public VehicleModel actualizarEstado(@PathVariable("id") Long id, @PathVariable("status") VehicleStatus status) {
        return this.vehicleService.actualizarEstado(id, status);
    }

    @DeleteMapping(path = "/{id}")
    public String eliminarPorId(@PathVariable("id") Long id) {
        this.vehicleService.eliminarVehiculo(id);
        return "Se eliminó el vehículo con id: " + id;
    }

    // Ej: GET /vehicles/status/Disponible
    @GetMapping(path = "/status/{status}")
    public ArrayList<VehicleModel> buscarPorEstado(@PathVariable("status") VehicleStatus status) {
        return this.vehicleService.buscarPorEstado(status);
    }

    @GetMapping(path = "/office/{officeId}")
    public ArrayList<VehicleModel> buscarPorOficina(@PathVariable("officeId") Long officeId) {
        return this.vehicleService.buscarPorOficina(officeId);
    }
}
