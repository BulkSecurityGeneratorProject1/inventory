package com.sonnetstone.inventory.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sonnetstone.inventory.domain.SalesDetails;

import com.sonnetstone.inventory.repository.SalesDetailsRepository;
import com.sonnetstone.inventory.repository.search.SalesDetailsSearchRepository;
import com.sonnetstone.inventory.web.rest.errors.BadRequestAlertException;
import com.sonnetstone.inventory.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SalesDetails.
 */
@RestController
@RequestMapping("/api")
public class SalesDetailsResource {

    private final Logger log = LoggerFactory.getLogger(SalesDetailsResource.class);

    private static final String ENTITY_NAME = "salesDetails";

    private final SalesDetailsRepository salesDetailsRepository;

    private final SalesDetailsSearchRepository salesDetailsSearchRepository;

    public SalesDetailsResource(SalesDetailsRepository salesDetailsRepository, SalesDetailsSearchRepository salesDetailsSearchRepository) {
        this.salesDetailsRepository = salesDetailsRepository;
        this.salesDetailsSearchRepository = salesDetailsSearchRepository;
    }

    /**
     * POST  /sales-details : Create a new salesDetails.
     *
     * @param salesDetails the salesDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new salesDetails, or with status 400 (Bad Request) if the salesDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sales-details")
    @Timed
    public ResponseEntity<SalesDetails> createSalesDetails(@Valid @RequestBody SalesDetails salesDetails) throws URISyntaxException {
        log.debug("REST request to save SalesDetails : {}", salesDetails);
        if (salesDetails.getId() != null) {
            throw new BadRequestAlertException("A new salesDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SalesDetails result = salesDetailsRepository.save(salesDetails);
        salesDetailsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/sales-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sales-details : Updates an existing salesDetails.
     *
     * @param salesDetails the salesDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated salesDetails,
     * or with status 400 (Bad Request) if the salesDetails is not valid,
     * or with status 500 (Internal Server Error) if the salesDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sales-details")
    @Timed
    public ResponseEntity<SalesDetails> updateSalesDetails(@Valid @RequestBody SalesDetails salesDetails) throws URISyntaxException {
        log.debug("REST request to update SalesDetails : {}", salesDetails);
        if (salesDetails.getId() == null) {
            return createSalesDetails(salesDetails);
        }
        SalesDetails result = salesDetailsRepository.save(salesDetails);
        salesDetailsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, salesDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sales-details : get all the salesDetails.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of salesDetails in body
     */
    @GetMapping("/sales-details")
    @Timed
    public List<SalesDetails> getAllSalesDetails() {
        log.debug("REST request to get all SalesDetails");
        return salesDetailsRepository.findAll();
        }

    /**
     * GET  /sales-details/:id : get the "id" salesDetails.
     *
     * @param id the id of the salesDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the salesDetails, or with status 404 (Not Found)
     */
    @GetMapping("/sales-details/{id}")
    @Timed
    public ResponseEntity<SalesDetails> getSalesDetails(@PathVariable Long id) {
        log.debug("REST request to get SalesDetails : {}", id);
        SalesDetails salesDetails = salesDetailsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(salesDetails));
    }

    /**
     * DELETE  /sales-details/:id : delete the "id" salesDetails.
     *
     * @param id the id of the salesDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sales-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteSalesDetails(@PathVariable Long id) {
        log.debug("REST request to delete SalesDetails : {}", id);
        salesDetailsRepository.delete(id);
        salesDetailsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sales-details?query=:query : search for the salesDetails corresponding
     * to the query.
     *
     * @param query the query of the salesDetails search
     * @return the result of the search
     */
    @GetMapping("/_search/sales-details")
    @Timed
    public List<SalesDetails> searchSalesDetails(@RequestParam String query) {
        log.debug("REST request to search SalesDetails for query {}", query);
        return StreamSupport
            .stream(salesDetailsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
