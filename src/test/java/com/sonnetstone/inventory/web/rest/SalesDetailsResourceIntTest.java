package com.sonnetstone.inventory.web.rest;

import com.sonnetstone.inventory.InventoryApp;

import com.sonnetstone.inventory.domain.SalesDetails;
import com.sonnetstone.inventory.repository.SalesDetailsRepository;
import com.sonnetstone.inventory.repository.search.SalesDetailsSearchRepository;
import com.sonnetstone.inventory.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sonnetstone.inventory.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SalesDetailsResource REST controller.
 *
 * @see SalesDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InventoryApp.class)
public class SalesDetailsResourceIntTest {

    private static final Double DEFAULT_LENGTH = 1D;
    private static final Double UPDATED_LENGTH = 2D;

    private static final Double DEFAULT_BREADTH = 1D;
    private static final Double UPDATED_BREADTH = 2D;

    private static final Double DEFAULT_SQUARE_FEET = 1D;
    private static final Double UPDATED_SQUARE_FEET = 2D;

    @Autowired
    private SalesDetailsRepository salesDetailsRepository;

    @Autowired
    private SalesDetailsSearchRepository salesDetailsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSalesDetailsMockMvc;

    private SalesDetails salesDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SalesDetailsResource salesDetailsResource = new SalesDetailsResource(salesDetailsRepository, salesDetailsSearchRepository);
        this.restSalesDetailsMockMvc = MockMvcBuilders.standaloneSetup(salesDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalesDetails createEntity(EntityManager em) {
        SalesDetails salesDetails = new SalesDetails()
            .length(DEFAULT_LENGTH)
            .breadth(DEFAULT_BREADTH)
            .squareFeet(DEFAULT_SQUARE_FEET);
        return salesDetails;
    }

    @Before
    public void initTest() {
        salesDetailsSearchRepository.deleteAll();
        salesDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createSalesDetails() throws Exception {
        int databaseSizeBeforeCreate = salesDetailsRepository.findAll().size();

        // Create the SalesDetails
        restSalesDetailsMockMvc.perform(post("/api/sales-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesDetails)))
            .andExpect(status().isCreated());

        // Validate the SalesDetails in the database
        List<SalesDetails> salesDetailsList = salesDetailsRepository.findAll();
        assertThat(salesDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        SalesDetails testSalesDetails = salesDetailsList.get(salesDetailsList.size() - 1);
        assertThat(testSalesDetails.getLength()).isEqualTo(DEFAULT_LENGTH);
        assertThat(testSalesDetails.getBreadth()).isEqualTo(DEFAULT_BREADTH);
        assertThat(testSalesDetails.getSquareFeet()).isEqualTo(DEFAULT_SQUARE_FEET);

        // Validate the SalesDetails in Elasticsearch
        SalesDetails salesDetailsEs = salesDetailsSearchRepository.findOne(testSalesDetails.getId());
        assertThat(salesDetailsEs).isEqualToIgnoringGivenFields(testSalesDetails);
    }

    @Test
    @Transactional
    public void createSalesDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salesDetailsRepository.findAll().size();

        // Create the SalesDetails with an existing ID
        salesDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalesDetailsMockMvc.perform(post("/api/sales-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesDetails)))
            .andExpect(status().isBadRequest());

        // Validate the SalesDetails in the database
        List<SalesDetails> salesDetailsList = salesDetailsRepository.findAll();
        assertThat(salesDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLengthIsRequired() throws Exception {
        int databaseSizeBeforeTest = salesDetailsRepository.findAll().size();
        // set the field null
        salesDetails.setLength(null);

        // Create the SalesDetails, which fails.

        restSalesDetailsMockMvc.perform(post("/api/sales-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesDetails)))
            .andExpect(status().isBadRequest());

        List<SalesDetails> salesDetailsList = salesDetailsRepository.findAll();
        assertThat(salesDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBreadthIsRequired() throws Exception {
        int databaseSizeBeforeTest = salesDetailsRepository.findAll().size();
        // set the field null
        salesDetails.setBreadth(null);

        // Create the SalesDetails, which fails.

        restSalesDetailsMockMvc.perform(post("/api/sales-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesDetails)))
            .andExpect(status().isBadRequest());

        List<SalesDetails> salesDetailsList = salesDetailsRepository.findAll();
        assertThat(salesDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSquareFeetIsRequired() throws Exception {
        int databaseSizeBeforeTest = salesDetailsRepository.findAll().size();
        // set the field null
        salesDetails.setSquareFeet(null);

        // Create the SalesDetails, which fails.

        restSalesDetailsMockMvc.perform(post("/api/sales-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesDetails)))
            .andExpect(status().isBadRequest());

        List<SalesDetails> salesDetailsList = salesDetailsRepository.findAll();
        assertThat(salesDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSalesDetails() throws Exception {
        // Initialize the database
        salesDetailsRepository.saveAndFlush(salesDetails);

        // Get all the salesDetailsList
        restSalesDetailsMockMvc.perform(get("/api/sales-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.doubleValue())))
            .andExpect(jsonPath("$.[*].breadth").value(hasItem(DEFAULT_BREADTH.doubleValue())))
            .andExpect(jsonPath("$.[*].squareFeet").value(hasItem(DEFAULT_SQUARE_FEET.doubleValue())));
    }

    @Test
    @Transactional
    public void getSalesDetails() throws Exception {
        // Initialize the database
        salesDetailsRepository.saveAndFlush(salesDetails);

        // Get the salesDetails
        restSalesDetailsMockMvc.perform(get("/api/sales-details/{id}", salesDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(salesDetails.getId().intValue()))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH.doubleValue()))
            .andExpect(jsonPath("$.breadth").value(DEFAULT_BREADTH.doubleValue()))
            .andExpect(jsonPath("$.squareFeet").value(DEFAULT_SQUARE_FEET.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSalesDetails() throws Exception {
        // Get the salesDetails
        restSalesDetailsMockMvc.perform(get("/api/sales-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalesDetails() throws Exception {
        // Initialize the database
        salesDetailsRepository.saveAndFlush(salesDetails);
        salesDetailsSearchRepository.save(salesDetails);
        int databaseSizeBeforeUpdate = salesDetailsRepository.findAll().size();

        // Update the salesDetails
        SalesDetails updatedSalesDetails = salesDetailsRepository.findOne(salesDetails.getId());
        // Disconnect from session so that the updates on updatedSalesDetails are not directly saved in db
        em.detach(updatedSalesDetails);
        updatedSalesDetails
            .length(UPDATED_LENGTH)
            .breadth(UPDATED_BREADTH)
            .squareFeet(UPDATED_SQUARE_FEET);

        restSalesDetailsMockMvc.perform(put("/api/sales-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSalesDetails)))
            .andExpect(status().isOk());

        // Validate the SalesDetails in the database
        List<SalesDetails> salesDetailsList = salesDetailsRepository.findAll();
        assertThat(salesDetailsList).hasSize(databaseSizeBeforeUpdate);
        SalesDetails testSalesDetails = salesDetailsList.get(salesDetailsList.size() - 1);
        assertThat(testSalesDetails.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testSalesDetails.getBreadth()).isEqualTo(UPDATED_BREADTH);
        assertThat(testSalesDetails.getSquareFeet()).isEqualTo(UPDATED_SQUARE_FEET);

        // Validate the SalesDetails in Elasticsearch
        SalesDetails salesDetailsEs = salesDetailsSearchRepository.findOne(testSalesDetails.getId());
        assertThat(salesDetailsEs).isEqualToIgnoringGivenFields(testSalesDetails);
    }

    @Test
    @Transactional
    public void updateNonExistingSalesDetails() throws Exception {
        int databaseSizeBeforeUpdate = salesDetailsRepository.findAll().size();

        // Create the SalesDetails

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSalesDetailsMockMvc.perform(put("/api/sales-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesDetails)))
            .andExpect(status().isCreated());

        // Validate the SalesDetails in the database
        List<SalesDetails> salesDetailsList = salesDetailsRepository.findAll();
        assertThat(salesDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSalesDetails() throws Exception {
        // Initialize the database
        salesDetailsRepository.saveAndFlush(salesDetails);
        salesDetailsSearchRepository.save(salesDetails);
        int databaseSizeBeforeDelete = salesDetailsRepository.findAll().size();

        // Get the salesDetails
        restSalesDetailsMockMvc.perform(delete("/api/sales-details/{id}", salesDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean salesDetailsExistsInEs = salesDetailsSearchRepository.exists(salesDetails.getId());
        assertThat(salesDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<SalesDetails> salesDetailsList = salesDetailsRepository.findAll();
        assertThat(salesDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSalesDetails() throws Exception {
        // Initialize the database
        salesDetailsRepository.saveAndFlush(salesDetails);
        salesDetailsSearchRepository.save(salesDetails);

        // Search the salesDetails
        restSalesDetailsMockMvc.perform(get("/api/_search/sales-details?query=id:" + salesDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.doubleValue())))
            .andExpect(jsonPath("$.[*].breadth").value(hasItem(DEFAULT_BREADTH.doubleValue())))
            .andExpect(jsonPath("$.[*].squareFeet").value(hasItem(DEFAULT_SQUARE_FEET.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalesDetails.class);
        SalesDetails salesDetails1 = new SalesDetails();
        salesDetails1.setId(1L);
        SalesDetails salesDetails2 = new SalesDetails();
        salesDetails2.setId(salesDetails1.getId());
        assertThat(salesDetails1).isEqualTo(salesDetails2);
        salesDetails2.setId(2L);
        assertThat(salesDetails1).isNotEqualTo(salesDetails2);
        salesDetails1.setId(null);
        assertThat(salesDetails1).isNotEqualTo(salesDetails2);
    }
}
