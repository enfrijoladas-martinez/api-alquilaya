package com.api.alquilaya.repositories;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.alquilaya.models.RentalModel;
import com.api.alquilaya.models.RentalStatus;

@Repository
public interface IRentalRepository extends JpaRepository<RentalModel, Long> {

    ArrayList<RentalModel> findByCustomerId(Long customerId);

    ArrayList<RentalModel> findByVehicleId(Long vehicleId);

    ArrayList<RentalModel> findByStatus(RentalStatus status);

    boolean existsByVehicleId(Long vehicleId);

    boolean existsByCustomerId(Long customerId);

    // ¿Hay otro alquiler (pendiente o activo) del mismo vehículo que se cruce con esas fechas?
    @Query("""
            SELECT COUNT(r) > 0 FROM RentalModel r
            WHERE r.vehicle.id = :vehicleId
              AND r.status IN :statuses
              AND r.rentalDate <= :end
              AND r.returnDate >= :start
              AND (:excludeId IS NULL OR r.id <> :excludeId)
            """)
    boolean existsOverlap(@Param("vehicleId") Long vehicleId,
                          @Param("statuses") List<RentalStatus> statuses,
                          @Param("start") LocalDate start,
                          @Param("end") LocalDate end,
                          @Param("excludeId") Long excludeId);
}
