package com.sonnetstone.inventory.web.rest;

import com.sonnetstone.inventory.InventoryApp;

import com.sonnetstone.inventory.domain.WasteDetails;
import com.sonnetstone.inventory.repository.WasteDetailsRepository;
import com.sonnetstone.inventory.repository.search.WasteDetailsSearchRepository;
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
 * Test class for the WasteDetailsResource REST controller.
 *
 * @see WasteDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InventoryApp.class)
public class WasteDetailsResourceIntTest {

    private static final Double DEFAULT_LENGTH = 1D;
    private static final Double UPDATED_LENGTH = 2D;

    private static final Double DEFAULT_BREADTH = 1D;
    private static final Double UPDATED_BREADTH = 2D;

    private static final Double DEFAULT_SQUARE_FEET = 1D;
    private static final Double UPDATED_SQUARE_FEET = 2D;

    @Autowired
    private WasteDetailsRepository wasteDetailsRepository;

    @Autowired
    private WasteDetailsSearchRepository wasteDetailsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWasteDetailsMockMvc;

    private WasteDetails wasteDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WasteDetailsResource wasteDetailsResource = new WasteDetailsResource(wasteDetailsRepository, wasteDetailsSearchRepository);
        this.restWasteDetailsMockMvc = MockMvcBuilders.standaloneSetup(wasteDetailsResource)
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
    public static WasteDetails createEntity(EntityManager em) {
        WasteDetails wasteDetails = new WasteDetails()
            .length(DEFAULT_LENGTH)
            .breadth(DEFAULT_BREADTH)
            .squareFeet(DEFAULT_SQUARE_FEET);
        return wasteDetails;
    }

    @Before
    public void initTest() {
        wasteDetailsSearchRepository.deleteAll();
        wasteDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createWasteDetails() throws Exception {
        int databaseSizeBeforeCreate = wasteDetailsRepository.findAll().size();

        // Create the WasteDetails
        restWasteDetailsMockMvc.perform(post("/api/waste-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wasteDetails)))
            .andExpect(status().isCreated());

        // Validate the WasteDetails in the database
        List<WasteDetails> wasteDetailsList = wasteDetailsRepository.findAll();
        assertThat(wasteDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        WasteDetails testWasteDetails = wasteDetailsList.get(wasteDetailsList.size() - 1);
        assertThat(testWasteDetails.getLength()).isEqualTo(DEFAULT_LENGTH);
        assertThat(testWasteDetails.getBreadth()).isEqualTo(DEFAULT_BREADTH);
        assertThat(testWasteDetails.getSquareFeet()).isEqualTo(DEFAULT_SQUARE_FEET);

        // Validate the WasteDetails in Elasticsearch
        WasteDetails wasteDetailsEs = wasteDetailsSearchRepository.findOne(testWasteDetails.getId());
        assertThat(wasteDetailsEs).isEqualToIgnoringGivenFields(testWasteDetails);
    }

    @Test
    @Transactional
    public void createWasteDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wasteDetailsRepository.findAll().size();

        // Create the WasteDetails with an existing ID
        wasteDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWasteDetailsMockMvc.perform(post("/api/waste-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wasteDetails)))
            .andExpect(status().isBadRequest());

        // Validate the WasteDetails in the database
        List<WasteDetails> wasteDetailsList = wasteDetailsRepository.findAll();
        assertThat(wasteDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLengthIsRequired() throws Exception {
        int databaseSizeBeforeTest = wasteDetailsRepository.findAll().size();
        // set the field null
        wasteDetails.setLength(null);

        // Create the WasteDetails, which fails.

        restWasteDetailsMockMvc.perform(post("/api/waste-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wasteDetails)))
            .andExpect(status().isBadRequest());

        List<WasteDetails> wasteDetailsList = wasteDetailsRepository.findAll();
        assertThat(wasteDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBreadthIsRequired() throws Exception {
        int databaseSizeBeforeTest = wasteDetailsRepository.findAll().size();
        // set the field null
        wasteDetails.setBreadth(null);

        // Create the WasteDetails, which fails.

        restWasteDetailsMockMvc.perform(post("/api/waste-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wasteDetails)))
            .andExpect(status().isBadRequest());

        List<WasteDetails> wasteDetailsList = wasteDetailsRepository.findAll();
        assertThat(wasteDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSquareFeetIsRequired() throws Exception {
        int databaseSizeBeforeTest = wasteDetailsRepository.findAll().size();
        // set the field null
        wasteDetails.setSquareFeet(null);

        // Create the WasteDetails, which fails.

        restWasteDetailsMockMvc.perform(post("/api/waste-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wasteDetails)))
            .andExpect(status().isBadRequest());

        List<WasteDetails> wasteDetailsList = wasteDetailsRepository.findAll();
        assertThat(wasteDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWasteDetails() throws Exception {
        // Initialize the database
        wasteDetailsRepository.saveAndFlush(wasteDetails);

        // Get all the wasteDetailsList
        restWasteDetailsMockMvc.perform(get("/api/waste-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wasteDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.doubleValue())))
            .andExpect(jsonPath("$.[*].breadth").value(hasItem(DEFAULT_BREADTH.doubleValue())))
            .andExpect(jsonPath("$.[*].squareFeet").value(hasItem(DEFAULT_SQUARE_FEET.doubleValue())));
    }

    @Test
    @Transactional
    public void getWasteDetails() throws Exception {
        // Initialize the database
        wasteDetailsRepository.saveAndFlush(wasteDetails);

        // Get the wasteDetails
        restWasteDetailsMockMvc.perform(get("/api/waste-details/{id}", wasteDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wasteDetails.getId().intValue()))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH.doubleValue()))
            .andExpect(jsonPath("$.breadth").value(DEFAULT_BREADTH.doubleValue()))
            .andExpect(jsonPath("$.squareFeet").value(DEFAULT_SQUARE_FEET.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWasteDetails() throws Exception {
        // Get the wasteDetails
        restWasteDetailsMockMvc.perform(get("/api/waste-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWasteDetails() throws Exception {
        // Initialize the database
        wasteDetailsRepository.saveAndFlush(wasteDetails);
        wasteDetailsSearchRepository.save(wasteDetails);
        int databaseSizeBeforeUpdate = wasteDetailsRepository.findAll().size();

        // Update the wasteDetails
        WasteDetails updatedWasteDetails = wasteDetailsRepository.findOne(wasteDetails.getId());
        // Disconnect from session so that the updates on updatedWasteDetails are not directly saved in db
        em.detach(updatedWasteDetails);
        updatedWasteDetails
            .length(UPDATED_LENGTH)
            .breadth(UPDATED_BREADTH)
            .squareFeet(UPDATED_SQUARE_FEET);

        restWasteDetailsMockMvc.perform(put("/api/waste-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWasteDetails)))
            .andExpect(status().isOk());

        // Validate the WasteDetails in the database
        List<WasteDetails> wasteDetailsList = wasteDetailsRepository.findAll();
        assertThat(wasteDetailsList).hasSize(databaseSizeBeforeUpdate);
        WasteDetails testWasteDetails = wasteDetailsList.get(wasteDetailsList.size() - 1);
        assertThat(testWasteDetails.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testWasteDetails.getBreadth()).isEqualTo(UPDATED_BREADTH);
        assertThat(testWasteDetails.getSquareFeet()).isEqualTo(UPDATED_SQUARE_FEET);

        // Validate the WasteDetails in Elasticsearch
        WasteDetails wasteDetailsEs = wasteDetailsSearchRepository.findOne(testWasteDetails.getId());
        assertThat(wasteDetailsEs).isEqualToIgnoringGivenFields(testWasteDetails);
    }

    @Test
    @Transactional
    public void updateNonExistingWasteDetails() throws Exception {
        int databaseSizeBeforeUpdate = wasteDetailsRepository.findAll().size();

        // Create the WasteDetails

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWasteDetailsMockMvc.perform(put("/api/waste-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wasteDetails)))
            .andExpect(status().isCreated());

        // Validate the WasteDetails in the database
        List<WasteDetails> wasteDetailsList = wasteDetailsRepository.findAll();
        assertThat(wasteDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWasteDetails() throws Exception {
        // Initialize the database
        wasteDetailsRepository.saveAndFlush(wasteDetails);
        wasteDetailsSearchRepository.save(wasteDetails);
        int databaseSizeBeforeDelete = wasteDetailsRepository.findAll().size();

        // Get the wasteDetails
        restWasteDetailsMockMvc.perform(delete("/api/waste-details/{id}", wasteDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean wasteDetailsExistsInEs = wasteDetailsSearchRepository.exists(wasteDetails.getId());
        assertThat(wasteDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<WasteDetails> wasteDetailsList = wasteDetailsRepository.findAll();
        assertThat(wasteDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWasteDetails() throws Exception {
        // Initialize the database
        wasteDetailsRepository.saveAndFlush(wasteDetails);
        wasteDetailsSearchRepository.save(wasteDetails);

        // Search the wasteDetails
        restWasteDetailsMockMvc.perform(get("/api/_search/waste-details?query=id:" + wasteDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wasteDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.doubleValue())))
            .andExpect(jsonPath("$.[*].breadth").value(hasItem(DEFAULT_BREADTH.doubleValue())))
            .andExpect(jsonPath("$.[*].squareFeet").value(hasItem(DEFAULT_SQUARE_FEET.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WasteDetails.class);
        WasteDetails wasteDetails1 = new WasteDetails();
        wasteDetails1.setId(1L);
        WasteDetails wasteDetails2 = new WasteDetails();
        wasteDetails2.setId(wasteDetails1.getId());
        assertThat(wasteDetails1).isEqualTo(wasteDetails2);
        wasteDetails2.setId(2L);
        assertThat(wasteDetails1).isNotEqualTo(wasteDetails2);
        wasteDetails1.setId(null);
        assertThat(wasteDetails1).isNotEqualTo(wasteDetails2);
    }
}
