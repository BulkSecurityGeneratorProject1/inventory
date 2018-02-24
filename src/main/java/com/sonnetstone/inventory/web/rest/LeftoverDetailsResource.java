package com.sonnetstone.inventory.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sonnetstone.inventory.domain.LeftoverDetails;

import com.sonnetstone.inventory.repository.LeftoverDetailsRepository;
import com.sonnetstone.inventory.repository.search.LeftoverDetailsSearchRepository;
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
 * REST controller for managing LeftoverDetails.
 */
@RestController
@RequestMapping("/api")
public class LeftoverDetailsResource {

    private final Logger log = LoggerFactory.getLogger(LeftoverDetailsResource.class);

    private static final String ENTITY_NAME = "leftoverDetails";

    private final LeftoverDetailsRepository leftoverDetailsRepository;

    private final LeftoverDetailsSearchRepository leftoverDetailsSearchRepository;

    public LeftoverDetailsResource(LeftoverDetailsRepository leftoverDetailsRepository, LeftoverDetailsSearchRepository leftoverDetailsSearchRepository) {
        this.leftoverDetailsRepository = leftoverDetailsRepository;
        this.leftoverDetailsSearchRepository = leftoverDetailsSearchRepository;
    }

    /**
     * POST  /leftover-details : Create a new leftoverDetails.
     *
     * @param leftoverDetails the leftoverDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new leftoverDetails, or with status 400 (Bad Request) if the leftoverDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/leftover-details")
    @Timed
    public ResponseEntity<LeftoverDetails> createLeftoverDetails(@Valid @RequestBody LeftoverDetails leftoverDetails) throws URISyntaxException {
        log.debug("REST request to save LeftoverDetails : {}", leftoverDetails);
        if (leftoverDetails.getId() != null) {
            throw new BadRequestAlertException("A new leftoverDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeftoverDetails result = leftoverDetailsRepository.save(leftoverDetails);
        leftoverDetailsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/leftover-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /leftover-details : Updates an existing leftoverDetails.
     *
     * @param leftoverDetails the leftoverDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated leftoverDetails,
     * or with status 400 (Bad Request) if the leftoverDetails is not valid,
     * or with status 500 (Internal Server Error) if the leftoverDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/leftover-details")
    @Timed
    public ResponseEntity<LeftoverDetails> updateLeftoverDetails(@Valid @RequestBody LeftoverDetails leftoverDetails) throws URISyntaxException {
        log.debug("REST request to update LeftoverDetails : {}", leftoverDetails);
        if (leftoverDetails.getId() == null) {
            return createLeftoverDetails(leftoverDetails);
        }
        LeftoverDetails result = leftoverDetailsRepository.save(leftoverDetails);
        leftoverDetailsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, leftoverDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /leftover-details : get all the leftoverDetails.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of leftoverDetails in body
     */
    @GetMapping("/leftover-details")
    @Timed
    public List<LeftoverDetails> getAllLeftoverDetails() {
        log.debug("REST request to get all LeftoverDetails");
        return leftoverDetailsRepository.findAll();
        }

    /**
     * GET  /leftover-details/:id : get the "id" leftoverDetails.
     *
     * @param id the id of the leftoverDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the leftoverDetails, or with status 404 (Not Found)
     */
    @GetMapping("/leftover-details/{id}")
    @Timed
    public ResponseEntity<LeftoverDetails> getLeftoverDetails(@PathVariable Long id) {
        log.debug("REST request to get LeftoverDetails : {}", id);
        LeftoverDetails leftoverDetails = leftoverDetailsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(leftoverDetails));
    }

    /**
     * DELETE  /leftover-details/:id : delete the "id" leftoverDetails.
     *
     * @param id the id of the leftoverDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/leftover-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteLeftoverDetails(@PathVariable Long id) {
        log.debug("REST request to delete LeftoverDetails : {}", id);
        leftoverDetailsRepository.delete(id);
        leftoverDetailsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/leftover-details?query=:query : search for the leftoverDetails corresponding
     * to the query.
     *
     * @param query the query of the leftoverDetails search
     * @return the result of the search
     */
    @GetMapping("/_search/leftover-details")
    @Timed
    public List<LeftoverDetails> searchLeftoverDetails(@RequestParam String query) {
        log.debug("REST request to search LeftoverDetails for query {}", query);
        return StreamSupport
            .stream(leftoverDetailsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
