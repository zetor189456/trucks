package com.simpleeffect.trucks.service;

import com.simpleeffect.trucks.domain.Truck;
import com.simpleeffect.trucks.repository.TruckRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Truck.
 */
@Service
@Transactional
public class TruckService {

    private final Logger log = LoggerFactory.getLogger(TruckService.class);

    private final TruckRepository truckRepository;

    public TruckService(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
    }

    /**
     * Save a truck.
     *
     * @param truck the entity to save
     * @return the persisted entity
     */
    public Truck save(Truck truck) {
        log.debug("Request to save Truck : {}", truck);
        return truckRepository.save(truck);
    }

    /**
     * Get all the trucks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Truck> findAll(Pageable pageable) {
        log.debug("Request to get all Trucks");
        return truckRepository.findAll(pageable);
    }

    /**
     * Get one truck by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Truck findOne(Long id) {
        log.debug("Request to get Truck : {}", id);
        return truckRepository.findOne(id);
    }

    /**
     * Delete the truck by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Truck : {}", id);
        truckRepository.delete(id);
    }
}
