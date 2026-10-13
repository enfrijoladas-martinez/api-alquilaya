package com.api.alquilaya.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.alquilaya.models.MaintenanceModel;

@Repository
public interface IMaintenanceRepository extends JpaRepository<MaintenanceModel, Long> {

    ArrayList<MaintenanceModel> findByVehicleIdOrderByMaintenanceDateDesc(Long vehicleId);

    boolean existsByVehicleId(Long vehicleId);
}
