package com.api.alquilaya.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.alquilaya.exceptions.BusinessException;
import com.api.alquilaya.exceptions.NotFoundException;
import com.api.alquilaya.models.CustomerModel;
import com.api.alquilaya.repositories.ICustomerRepository;
import com.api.alquilaya.repositories.IRentalRepository;

@Service
public class CustomerService {
    @Autowired
    ICustomerRepository customerRepository;

    @Autowired
    IRentalRepository rentalRepository;

    public ArrayList<CustomerModel> obtenerClientes() {
        return (ArrayList<CustomerModel>) customerRepository.findAll();
    }

    public CustomerModel obtenerPorId(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe el cliente con id: " + id));
    }

    public CustomerModel buscarPorEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("No existe un cliente con email: " + email));
    }

    public CustomerModel guardarCliente(CustomerModel cliente) {
        validar(cliente);
        if (customerRepository.existsByEmail(cliente.getEmail())) {
            throw new BusinessException("Ya existe un cliente con el email " + cliente.getEmail());
        }
        if (customerRepository.existsByDriversLicense(cliente.getDriversLicense())) {
            throw new BusinessException("Ya existe un cliente con la licencia " + cliente.getDriversLicense());
        }
        cliente.setId(null);
        return customerRepository.save(cliente);
    }

    public CustomerModel actualizarCliente(CustomerModel request, Long id) {
        validar(request);
        CustomerModel cliente = obtenerPorId(id);
        if (customerRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new BusinessException("Ya existe otro cliente con el email " + request.getEmail());
        }
        if (customerRepository.existsByDriversLicenseAndIdNot(request.getDriversLicense(), id)) {
            throw new BusinessException("Ya existe otro cliente con la licencia " + request.getDriversLicense());
        }
        cliente.setFirstName(request.getFirstName());
        cliente.setLastName(request.getLastName());
        cliente.setEmail(request.getEmail());
        cliente.setDriversLicense(request.getDriversLicense());
        cliente.setPhone(request.getPhone());
        return customerRepository.save(cliente);
    }

    public void eliminarCliente(Long id) {
        obtenerPorId(id);
        if (rentalRepository.existsByCustomerId(id)) {
            throw new BusinessException("No se puede eliminar el cliente " + id + " porque tiene alquileres registrados");
        }
        customerRepository.deleteById(id);
    }

    private void validar(CustomerModel c) {
        if (c.getFirstName() == null || c.getFirstName().isBlank()
                || c.getLastName() == null || c.getLastName().isBlank()
                || c.getDriversLicense() == null || c.getDriversLicense().isBlank()) {
            throw new BusinessException("firstName, lastName y driversLicense son obligatorios");
        }
        if (c.getEmail() == null || !c.getEmail().contains("@")) {
            throw new BusinessException("Email no válido");
        }
    }
}
