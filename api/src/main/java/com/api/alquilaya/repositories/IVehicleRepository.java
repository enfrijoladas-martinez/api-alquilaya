package com.api.alquilaya.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.alquilaya.models.VehicleModel;
import com.api.alquilaya.models.VehicleStatus;

@Repository
public interface IVehicleRepository extends JpaRepository<VehicleModel, Long> {

    ArrayList<VehicleModel> findByStatus(VehicleStatus status);

    ArrayList<VehicleModel> findByOfficeId(Long officeId);

    boolean existsByLicensePlate(String licensePlate);

    boolean existsByLicensePlateAndIdNot(String licensePlate, Long id);

    boolean existsByOfficeId(Long officeId);
}
