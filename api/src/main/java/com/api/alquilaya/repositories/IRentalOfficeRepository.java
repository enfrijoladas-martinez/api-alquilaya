package com.api.alquilaya.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.alquilaya.models.RentalOfficeModel;

@Repository
public interface IRentalOfficeRepository extends JpaRepository<RentalOfficeModel, Long> {

    ArrayList<RentalOfficeModel> findByCity(String city);

    ArrayList<RentalOfficeModel> findByRegion(String region);
}
