package com.api.alquilaya.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.alquilaya.exceptions.BusinessException;
import com.api.alquilaya.exceptions.NotFoundException;
import com.api.alquilaya.models.RentalOfficeModel;
import com.api.alquilaya.repositories.IRentalOfficeRepository;
import com.api.alquilaya.repositories.IVehicleRepository;

@Service
public class RentalOfficeService {
    @Autowired
    IRentalOfficeRepository officeRepository;

    @Autowired
    IVehicleRepository vehicleRepository;

    public ArrayList<RentalOfficeModel> obtenerOficinas() {
        return (ArrayList<RentalOfficeModel>) officeRepository.findAll();
    }

    public RentalOfficeModel obtenerPorId(Long id) {
        return officeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe la oficina con id: " + id));
    }

    public RentalOfficeModel guardarOficina(RentalOfficeModel oficina) {
        validar(oficina);
        oficina.setId(null);
        return officeRepository.save(oficina);
    }

    public RentalOfficeModel actualizarOficina(RentalOfficeModel request, Long id) {
        validar(request);
        RentalOfficeModel oficina = obtenerPorId(id);
        oficina.setOfficeName(request.getOfficeName());
        oficina.setAddress(request.getAddress());
        oficina.setCity(request.getCity());
        oficina.setRegion(request.getRegion());
        return officeRepository.save(oficina);
    }

    public void eliminarOficina(Long id) {
        obtenerPorId(id);
        if (vehicleRepository.existsByOfficeId(id)) {
            throw new BusinessException("No se puede eliminar la oficina " + id + " porque tiene vehículos asignados");
        }
        officeRepository.deleteById(id);
    }

    public ArrayList<RentalOfficeModel> buscarPorCiudad(String city) {
        return officeRepository.findByCity(city);
    }

    public ArrayList<RentalOfficeModel> buscarPorRegion(String region) {
        return officeRepository.findByRegion(region);
    }

    private void validar(RentalOfficeModel o) {
        if (vacio(o.getOfficeName()) || vacio(o.getAddress()) || vacio(o.getCity()) || vacio(o.getRegion())) {
            throw new BusinessException("officeName, address, city y region son obligatorios");
        }
    }

    private boolean vacio(String s) {
        return s == null || s.isBlank();
    }
}
