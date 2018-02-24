package com.sonnetstone.inventory.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sonnetstone.inventory.domain.WasteDetails;

import com.sonnetstone.inventory.repository.WasteDetailsRepository;
import com.sonnetstone.inventory.repository.search.WasteDetailsSearchRepository;
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
 * REST controller for managing WasteDetails.
 */
@RestController
@RequestMapping("/api")
public class WasteDetailsResource {

    private final Logger log = LoggerFactory.getLogger(WasteDetailsResource.class);

    private static final String ENTITY_NAME = "wasteDetails";

    private final WasteDetailsRepository wasteDetailsRepository;

    private final WasteDetailsSearchRepository wasteDetailsSearchRepository;

    public WasteDetailsResource(WasteDetailsRepository wasteDetailsRepository, WasteDetailsSearchRepository wasteDetailsSearchRepository) {
        this.wasteDetailsRepository = wasteDetailsRepository;
        this.wasteDetailsSearchRepository = wasteDetailsSearchRepository;
    }

    /**
     * POST  /waste-details : Create a new wasteDetails.
     *
     * @param wasteDetails the wasteDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wasteDetails, or with status 400 (Bad Request) if the wasteDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/waste-details")
    @Timed
    public ResponseEntity<WasteDetails> createWasteDetails(@Valid @RequestBody WasteDetails wasteDetails) throws URISyntaxException {
        log.debug("REST request to save WasteDetails : {}", wasteDetails);
        if (wasteDetails.getId() != null) {
            throw new BadRequestAlertException("A new wasteDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WasteDetails result = wasteDetailsRepository.save(wasteDetails);
        wasteDetailsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/waste-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /waste-details : Updates an existing wasteDetails.
     *
     * @param wasteDetails the wasteDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wasteDetails,
     * or with status 400 (Bad Request) if the wasteDetails is not valid,
     * or with status 500 (Internal Server Error) if the wasteDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/waste-details")
    @Timed
    public ResponseEntity<WasteDetails> updateWasteDetails(@Valid @RequestBody WasteDetails wasteDetails) throws URISyntaxException {
        log.debug("REST request to update WasteDetails : {}", wasteDetails);
        if (wasteDetails.getId() == null) {
            return createWasteDetails(wasteDetails);
        }
        WasteDetails result = wasteDetailsRepository.save(wasteDetails);
        wasteDetailsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wasteDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /waste-details : get all the wasteDetails.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of wasteDetails in body
     */
    @GetMapping("/waste-details")
    @Timed
    public List<WasteDetails> getAllWasteDetails() {
        log.debug("REST request to get all WasteDetails");
        return wasteDetailsRepository.findAll();
        }

    /**
     * GET  /waste-details/:id : get the "id" wasteDetails.
     *
     * @param id the id of the wasteDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wasteDetails, or with status 404 (Not Found)
     */
    @GetMapping("/waste-details/{id}")
    @Timed
    public ResponseEntity<WasteDetails> getWasteDetails(@PathVariable Long id) {
        log.debug("REST request to get WasteDetails : {}", id);
        WasteDetails wasteDetails = wasteDetailsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wasteDetails));
    }

    /**
     * DELETE  /waste-details/:id : delete the "id" wasteDetails.
     *
     * @param id the id of the wasteDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/waste-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteWasteDetails(@PathVariable Long id) {
        log.debug("REST request to delete WasteDetails : {}", id);
        wasteDetailsRepository.delete(id);
        wasteDetailsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/waste-details?query=:query : search for the wasteDetails corresponding
     * to the query.
     *
     * @param query the query of the wasteDetails search
     * @return the result of the search
     */
    @GetMapping("/_search/waste-details")
    @Timed
    public List<WasteDetails> searchWasteDetails(@RequestParam String query) {
        log.debug("REST request to search WasteDetails for query {}", query);
        return StreamSupport
            .stream(wasteDetailsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
