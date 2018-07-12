package com.simpleeffect.trucks.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.simpleeffect.trucks.domain.Truck;
import com.simpleeffect.trucks.service.TruckService;
import com.simpleeffect.trucks.web.rest.errors.BadRequestAlertException;
import com.simpleeffect.trucks.web.rest.util.HeaderUtil;
import com.simpleeffect.trucks.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Truck.
 */
@RestController
@RequestMapping("/api")
public class TruckResource {

    private final Logger log = LoggerFactory.getLogger(TruckResource.class);

    private static final String ENTITY_NAME = "truck";

    private final TruckService truckService;

    public TruckResource(TruckService truckService) {
        this.truckService = truckService;
    }

    /**
     * POST  /trucks : Create a new truck.
     *
     * @param truck the truck to create
     * @return the ResponseEntity with status 201 (Created) and with body the new truck, or with status 400 (Bad Request) if the truck has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/trucks")
    @Timed
    public ResponseEntity<Truck> createTruck(@RequestBody Truck truck) throws URISyntaxException {
        log.debug("REST request to save Truck : {}", truck);
        if (truck.getId() != null) {
            throw new BadRequestAlertException("A new truck cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Truck result = truckService.save(truck);
        return ResponseEntity.created(new URI("/api/trucks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trucks : Updates an existing truck.
     *
     * @param truck the truck to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated truck,
     * or with status 400 (Bad Request) if the truck is not valid,
     * or with status 500 (Internal Server Error) if the truck couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/trucks")
    @Timed
    public ResponseEntity<Truck> updateTruck(@RequestBody Truck truck) throws URISyntaxException {
        log.debug("REST request to update Truck : {}", truck);
        if (truck.getId() == null) {
            return createTruck(truck);
        }
        Truck result = truckService.save(truck);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, truck.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trucks : get all the trucks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of trucks in body
     */
    @GetMapping("/trucks")
    @Timed
    public ResponseEntity<List<Truck>> getAllTrucks(Pageable pageable) {
        log.debug("REST request to get a page of Trucks");
        Page<Truck> page = truckService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trucks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /trucks/:id : get the "id" truck.
     *
     * @param id the id of the truck to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the truck, or with status 404 (Not Found)
     */
    @GetMapping("/trucks/{id}")
    @Timed
    public ResponseEntity<Truck> getTruck(@PathVariable Long id) {
        log.debug("REST request to get Truck : {}", id);
        Truck truck = truckService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(truck));
    }

    /**
     * DELETE  /trucks/:id : delete the "id" truck.
     *
     * @param id the id of the truck to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/trucks/{id}")
    @Timed
    public ResponseEntity<Void> deleteTruck(@PathVariable Long id) {
        log.debug("REST request to delete Truck : {}", id);
        truckService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
