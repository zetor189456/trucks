package com.simpleeffect.trucks.repository;

import com.simpleeffect.trucks.domain.Truck;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Truck entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TruckRepository extends JpaRepository<Truck, Long> {

}
