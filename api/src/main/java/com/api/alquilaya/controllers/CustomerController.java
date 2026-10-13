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

import com.api.alquilaya.models.CustomerModel;
import com.api.alquilaya.services.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ArrayList<CustomerModel> obtenerClientes() {
        return this.customerService.obtenerClientes();
    }

    @GetMapping(path = "/{id}")
    public CustomerModel obtenerPorId(@PathVariable("id") Long id) {
        return this.customerService.obtenerPorId(id);
    }

    // Métodos obtenerDetalle() / obtenerNombreCompleto() del diagrama UML
    @GetMapping(path = "/{id}/detalle")
    public String obtenerDetalle(@PathVariable("id") Long id) {
        return this.customerService.obtenerPorId(id).obtenerDetalle();
    }

    @GetMapping(path = "/email/{email}")
    public CustomerModel buscarPorEmail(@PathVariable("email") String email) {
        return this.customerService.buscarPorEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerModel guardarCliente(@RequestBody CustomerModel cliente) {
        return this.customerService.guardarCliente(cliente);
    }

    @PutMapping(path = "/{id}")
    public CustomerModel actualizarCliente(@RequestBody CustomerModel request, @PathVariable("id") Long id) {
        return this.customerService.actualizarCliente(request, id);
    }

    @DeleteMapping(path = "/{id}")
    public String eliminarPorId(@PathVariable("id") Long id) {
        this.customerService.eliminarCliente(id);
        return "Se eliminó el cliente con id: " + id;
    }
}
