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

import com.api.alquilaya.models.RentalModel;
import com.api.alquilaya.models.RentalStatus;
import com.api.alquilaya.services.RentalService;

@RestController
@RequestMapping("/rentals")
public class RentalController {
    @Autowired
    private RentalService rentalService;

    @GetMapping
    public ArrayList<RentalModel> obtenerAlquileres() {
        return this.rentalService.obtenerAlquileres();
    }

    @GetMapping(path = "/{id}")
    public RentalModel obtenerPorId(@PathVariable("id") Long id) {
        return this.rentalService.obtenerPorId(id);
    }

    // Método obtenerDetalle() del diagrama UML
    @GetMapping(path = "/{id}/detalle")
    public String obtenerDetalle(@PathVariable("id") Long id) {
        return this.rentalService.obtenerPorId(id).obtenerDetalle();
    }

    // Crea la reserva en estado Pendiente y calcula totalPrice
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RentalModel crearAlquiler(@RequestBody RentalModel alquiler) {
        return this.rentalService.crearAlquiler(alquiler);
    }

    @PutMapping(path = "/{id}")
    public RentalModel actualizarAlquiler(@RequestBody RentalModel request, @PathVariable("id") Long id) {
        return this.rentalService.actualizarAlquiler(request, id);
    }

    // Pendiente -> Activo (vehículo pasa a Alquilado)
    @PutMapping(path = "/{id}/start")
    public RentalModel iniciarAlquiler(@PathVariable("id") Long id) {
        return this.rentalService.iniciarAlquiler(id);
    }

    // Activo -> Completado (vehículo vuelve a Disponible)
    @PutMapping(path = "/{id}/complete")
    public RentalModel completarAlquiler(@PathVariable("id") Long id) {
        return this.rentalService.completarAlquiler(id);
    }

    @DeleteMapping(path = "/{id}")
    public String eliminarPorId(@PathVariable("id") Long id) {
        this.rentalService.eliminarAlquiler(id);
        return "Se eliminó el alquiler con id: " + id;
    }

    @GetMapping(path = "/customer/{customerId}")
    public ArrayList<RentalModel> buscarPorCliente(@PathVariable("customerId") Long customerId) {
        return this.rentalService.buscarPorCliente(customerId);
    }

    @GetMapping(path = "/vehicle/{vehicleId}")
    public ArrayList<RentalModel> buscarPorVehiculo(@PathVariable("vehicleId") Long vehicleId) {
        return this.rentalService.buscarPorVehiculo(vehicleId);
    }

    @GetMapping(path = "/status/{status}")
    public ArrayList<RentalModel> buscarPorEstado(@PathVariable("status") RentalStatus status) {
        return this.rentalService.buscarPorEstado(status);
    }
}
