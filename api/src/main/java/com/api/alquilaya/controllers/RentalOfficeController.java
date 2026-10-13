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

import com.api.alquilaya.models.RentalOfficeModel;
import com.api.alquilaya.services.RentalOfficeService;

@RestController
@RequestMapping("/offices")
public class RentalOfficeController {
    @Autowired
    private RentalOfficeService officeService;

    @GetMapping
    public ArrayList<RentalOfficeModel> obtenerOficinas() {
        return this.officeService.obtenerOficinas();
    }

    @GetMapping(path = "/{id}")
    public RentalOfficeModel obtenerPorId(@PathVariable("id") Long id) {
        return this.officeService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RentalOfficeModel guardarOficina(@RequestBody RentalOfficeModel oficina) {
        return this.officeService.guardarOficina(oficina);
    }

    @PutMapping(path = "/{id}")
    public RentalOfficeModel actualizarOficina(@RequestBody RentalOfficeModel request, @PathVariable("id") Long id) {
        return this.officeService.actualizarOficina(request, id);
    }

    @DeleteMapping(path = "/{id}")
    public String eliminarPorId(@PathVariable("id") Long id) {
        this.officeService.eliminarOficina(id);
        return "Se eliminó la oficina con id: " + id;
    }

    @GetMapping(path = "/city/{city}")
    public ArrayList<RentalOfficeModel> buscarPorCiudad(@PathVariable("city") String city) {
        return this.officeService.buscarPorCiudad(city);
    }

    @GetMapping(path = "/region/{region}")
    public ArrayList<RentalOfficeModel> buscarPorRegion(@PathVariable("region") String region) {
        return this.officeService.buscarPorRegion(region);
    }
}
