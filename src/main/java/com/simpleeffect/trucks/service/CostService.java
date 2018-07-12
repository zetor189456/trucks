package com.simpleeffect.trucks.service;

import com.simpleeffect.trucks.domain.Cost;
import com.simpleeffect.trucks.repository.CostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Cost.
 */
@Service
@Transactional
public class CostService {

    private final Logger log = LoggerFactory.getLogger(CostService.class);

    private final CostRepository costRepository;

    public CostService(CostRepository costRepository) {
        this.costRepository = costRepository;
    }

    /**
     * Save a cost.
     *
     * @param cost the entity to save
     * @return the persisted entity
     */
    public Cost save(Cost cost) {
        log.debug("Request to save Cost : {}", cost);
        return costRepository.save(cost);
    }

    /**
     * Get all the costs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Cost> findAll(Pageable pageable) {
        log.debug("Request to get all Costs");
        return costRepository.findAll(pageable);
    }

    /**
     * Get one cost by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Cost findOne(Long id) {
        log.debug("Request to get Cost : {}", id);
        return costRepository.findOne(id);
    }

    /**
     * Delete the cost by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Cost : {}", id);
        costRepository.delete(id);
    }
}
