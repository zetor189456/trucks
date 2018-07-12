package com.simpleeffect.trucks.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.simpleeffect.trucks.domain.Cost;
import com.simpleeffect.trucks.service.CostService;
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
 * REST controller for managing Cost.
 */
@RestController
@RequestMapping("/api")
public class CostResource {

    private final Logger log = LoggerFactory.getLogger(CostResource.class);

    private static final String ENTITY_NAME = "cost";

    private final CostService costService;

    public CostResource(CostService costService) {
        this.costService = costService;
    }

    /**
     * POST  /costs : Create a new cost.
     *
     * @param cost the cost to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cost, or with status 400 (Bad Request) if the cost has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/costs")
    @Timed
    public ResponseEntity<Cost> createCost(@RequestBody Cost cost) throws URISyntaxException {
        log.debug("REST request to save Cost : {}", cost);
        if (cost.getId() != null) {
            throw new BadRequestAlertException("A new cost cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cost result = costService.save(cost);
        return ResponseEntity.created(new URI("/api/costs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /costs : Updates an existing cost.
     *
     * @param cost the cost to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cost,
     * or with status 400 (Bad Request) if the cost is not valid,
     * or with status 500 (Internal Server Error) if the cost couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/costs")
    @Timed
    public ResponseEntity<Cost> updateCost(@RequestBody Cost cost) throws URISyntaxException {
        log.debug("REST request to update Cost : {}", cost);
        if (cost.getId() == null) {
            return createCost(cost);
        }
        Cost result = costService.save(cost);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cost.getId().toString()))
            .body(result);
    }

    /**
     * GET  /costs : get all the costs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of costs in body
     */
    @GetMapping("/costs")
    @Timed
    public ResponseEntity<List<Cost>> getAllCosts(Pageable pageable) {
        log.debug("REST request to get a page of Costs");
        Page<Cost> page = costService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/costs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /costs/:id : get the "id" cost.
     *
     * @param id the id of the cost to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cost, or with status 404 (Not Found)
     */
    @GetMapping("/costs/{id}")
    @Timed
    public ResponseEntity<Cost> getCost(@PathVariable Long id) {
        log.debug("REST request to get Cost : {}", id);
        Cost cost = costService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cost));
    }

    /**
     * DELETE  /costs/:id : delete the "id" cost.
     *
     * @param id the id of the cost to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/costs/{id}")
    @Timed
    public ResponseEntity<Void> deleteCost(@PathVariable Long id) {
        log.debug("REST request to delete Cost : {}", id);
        costService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
