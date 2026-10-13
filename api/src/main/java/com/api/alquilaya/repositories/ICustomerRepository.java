package com.api.alquilaya.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.alquilaya.models.CustomerModel;

@Repository
public interface ICustomerRepository extends JpaRepository<CustomerModel, Long> {

    Optional<CustomerModel> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByDriversLicense(String driversLicense);

    boolean existsByDriversLicenseAndIdNot(String driversLicense, Long id);
}
