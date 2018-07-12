package com.simpleeffect.trucks.repository;

import com.simpleeffect.trucks.domain.Cost;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Cost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CostRepository extends JpaRepository<Cost, Long> {

}
