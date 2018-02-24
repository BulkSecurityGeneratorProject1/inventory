package com.sonnetstone.inventory.web.rest;

import com.sonnetstone.inventory.InventoryApp;

import com.sonnetstone.inventory.domain.LeftoverDetails;
import com.sonnetstone.inventory.repository.LeftoverDetailsRepository;
import com.sonnetstone.inventory.repository.search.LeftoverDetailsSearchRepository;
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
 * Test class for the LeftoverDetailsResource REST controller.
 *
 * @see LeftoverDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InventoryApp.class)
public class LeftoverDetailsResourceIntTest {

    private static final Integer DEFAULT_PARENT_ID = 1;
    private static final Integer UPDATED_PARENT_ID = 2;

    private static final Double DEFAULT_LENGTH = 1D;
    private static final Double UPDATED_LENGTH = 2D;

    private static final Double DEFAULT_BREADTH = 1D;
    private static final Double UPDATED_BREADTH = 2D;

    private static final Double DEFAULT_SQUARE_FEET = 1D;
    private static final Double UPDATED_SQUARE_FEET = 2D;

    @Autowired
    private LeftoverDetailsRepository leftoverDetailsRepository;

    @Autowired
    private LeftoverDetailsSearchRepository leftoverDetailsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLeftoverDetailsMockMvc;

    private LeftoverDetails leftoverDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LeftoverDetailsResource leftoverDetailsResource = new LeftoverDetailsResource(leftoverDetailsRepository, leftoverDetailsSearchRepository);
        this.restLeftoverDetailsMockMvc = MockMvcBuilders.standaloneSetup(leftoverDetailsResource)
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
    public static LeftoverDetails createEntity(EntityManager em) {
        LeftoverDetails leftoverDetails = new LeftoverDetails()
            .parentId(DEFAULT_PARENT_ID)
            .length(DEFAULT_LENGTH)
            .breadth(DEFAULT_BREADTH)
            .squareFeet(DEFAULT_SQUARE_FEET);
        return leftoverDetails;
    }

    @Before
    public void initTest() {
        leftoverDetailsSearchRepository.deleteAll();
        leftoverDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeftoverDetails() throws Exception {
        int databaseSizeBeforeCreate = leftoverDetailsRepository.findAll().size();

        // Create the LeftoverDetails
        restLeftoverDetailsMockMvc.perform(post("/api/leftover-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leftoverDetails)))
            .andExpect(status().isCreated());

        // Validate the LeftoverDetails in the database
        List<LeftoverDetails> leftoverDetailsList = leftoverDetailsRepository.findAll();
        assertThat(leftoverDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        LeftoverDetails testLeftoverDetails = leftoverDetailsList.get(leftoverDetailsList.size() - 1);
        assertThat(testLeftoverDetails.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testLeftoverDetails.getLength()).isEqualTo(DEFAULT_LENGTH);
        assertThat(testLeftoverDetails.getBreadth()).isEqualTo(DEFAULT_BREADTH);
        assertThat(testLeftoverDetails.getSquareFeet()).isEqualTo(DEFAULT_SQUARE_FEET);

        // Validate the LeftoverDetails in Elasticsearch
        LeftoverDetails leftoverDetailsEs = leftoverDetailsSearchRepository.findOne(testLeftoverDetails.getId());
        assertThat(leftoverDetailsEs).isEqualToIgnoringGivenFields(testLeftoverDetails);
    }

    @Test
    @Transactional
    public void createLeftoverDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leftoverDetailsRepository.findAll().size();

        // Create the LeftoverDetails with an existing ID
        leftoverDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeftoverDetailsMockMvc.perform(post("/api/leftover-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leftoverDetails)))
            .andExpect(status().isBadRequest());

        // Validate the LeftoverDetails in the database
        List<LeftoverDetails> leftoverDetailsList = leftoverDetailsRepository.findAll();
        assertThat(leftoverDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkParentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = leftoverDetailsRepository.findAll().size();
        // set the field null
        leftoverDetails.setParentId(null);

        // Create the LeftoverDetails, which fails.

        restLeftoverDetailsMockMvc.perform(post("/api/leftover-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leftoverDetails)))
            .andExpect(status().isBadRequest());

        List<LeftoverDetails> leftoverDetailsList = leftoverDetailsRepository.findAll();
        assertThat(leftoverDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLengthIsRequired() throws Exception {
        int databaseSizeBeforeTest = leftoverDetailsRepository.findAll().size();
        // set the field null
        leftoverDetails.setLength(null);

        // Create the LeftoverDetails, which fails.

        restLeftoverDetailsMockMvc.perform(post("/api/leftover-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leftoverDetails)))
            .andExpect(status().isBadRequest());

        List<LeftoverDetails> leftoverDetailsList = leftoverDetailsRepository.findAll();
        assertThat(leftoverDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBreadthIsRequired() throws Exception {
        int databaseSizeBeforeTest = leftoverDetailsRepository.findAll().size();
        // set the field null
        leftoverDetails.setBreadth(null);

        // Create the LeftoverDetails, which fails.

        restLeftoverDetailsMockMvc.perform(post("/api/leftover-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leftoverDetails)))
            .andExpect(status().isBadRequest());

        List<LeftoverDetails> leftoverDetailsList = leftoverDetailsRepository.findAll();
        assertThat(leftoverDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSquareFeetIsRequired() throws Exception {
        int databaseSizeBeforeTest = leftoverDetailsRepository.findAll().size();
        // set the field null
        leftoverDetails.setSquareFeet(null);

        // Create the LeftoverDetails, which fails.

        restLeftoverDetailsMockMvc.perform(post("/api/leftover-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leftoverDetails)))
            .andExpect(status().isBadRequest());

        List<LeftoverDetails> leftoverDetailsList = leftoverDetailsRepository.findAll();
        assertThat(leftoverDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLeftoverDetails() throws Exception {
        // Initialize the database
        leftoverDetailsRepository.saveAndFlush(leftoverDetails);

        // Get all the leftoverDetailsList
        restLeftoverDetailsMockMvc.perform(get("/api/leftover-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leftoverDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID)))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.doubleValue())))
            .andExpect(jsonPath("$.[*].breadth").value(hasItem(DEFAULT_BREADTH.doubleValue())))
            .andExpect(jsonPath("$.[*].squareFeet").value(hasItem(DEFAULT_SQUARE_FEET.doubleValue())));
    }

    @Test
    @Transactional
    public void getLeftoverDetails() throws Exception {
        // Initialize the database
        leftoverDetailsRepository.saveAndFlush(leftoverDetails);

        // Get the leftoverDetails
        restLeftoverDetailsMockMvc.perform(get("/api/leftover-details/{id}", leftoverDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(leftoverDetails.getId().intValue()))
            .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH.doubleValue()))
            .andExpect(jsonPath("$.breadth").value(DEFAULT_BREADTH.doubleValue()))
            .andExpect(jsonPath("$.squareFeet").value(DEFAULT_SQUARE_FEET.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLeftoverDetails() throws Exception {
        // Get the leftoverDetails
        restLeftoverDetailsMockMvc.perform(get("/api/leftover-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeftoverDetails() throws Exception {
        // Initialize the database
        leftoverDetailsRepository.saveAndFlush(leftoverDetails);
        leftoverDetailsSearchRepository.save(leftoverDetails);
        int databaseSizeBeforeUpdate = leftoverDetailsRepository.findAll().size();

        // Update the leftoverDetails
        LeftoverDetails updatedLeftoverDetails = leftoverDetailsRepository.findOne(leftoverDetails.getId());
        // Disconnect from session so that the updates on updatedLeftoverDetails are not directly saved in db
        em.detach(updatedLeftoverDetails);
        updatedLeftoverDetails
            .parentId(UPDATED_PARENT_ID)
            .length(UPDATED_LENGTH)
            .breadth(UPDATED_BREADTH)
            .squareFeet(UPDATED_SQUARE_FEET);

        restLeftoverDetailsMockMvc.perform(put("/api/leftover-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLeftoverDetails)))
            .andExpect(status().isOk());

        // Validate the LeftoverDetails in the database
        List<LeftoverDetails> leftoverDetailsList = leftoverDetailsRepository.findAll();
        assertThat(leftoverDetailsList).hasSize(databaseSizeBeforeUpdate);
        LeftoverDetails testLeftoverDetails = leftoverDetailsList.get(leftoverDetailsList.size() - 1);
        assertThat(testLeftoverDetails.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testLeftoverDetails.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testLeftoverDetails.getBreadth()).isEqualTo(UPDATED_BREADTH);
        assertThat(testLeftoverDetails.getSquareFeet()).isEqualTo(UPDATED_SQUARE_FEET);

        // Validate the LeftoverDetails in Elasticsearch
        LeftoverDetails leftoverDetailsEs = leftoverDetailsSearchRepository.findOne(testLeftoverDetails.getId());
        assertThat(leftoverDetailsEs).isEqualToIgnoringGivenFields(testLeftoverDetails);
    }

    @Test
    @Transactional
    public void updateNonExistingLeftoverDetails() throws Exception {
        int databaseSizeBeforeUpdate = leftoverDetailsRepository.findAll().size();

        // Create the LeftoverDetails

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLeftoverDetailsMockMvc.perform(put("/api/leftover-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leftoverDetails)))
            .andExpect(status().isCreated());

        // Validate the LeftoverDetails in the database
        List<LeftoverDetails> leftoverDetailsList = leftoverDetailsRepository.findAll();
        assertThat(leftoverDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLeftoverDetails() throws Exception {
        // Initialize the database
        leftoverDetailsRepository.saveAndFlush(leftoverDetails);
        leftoverDetailsSearchRepository.save(leftoverDetails);
        int databaseSizeBeforeDelete = leftoverDetailsRepository.findAll().size();

        // Get the leftoverDetails
        restLeftoverDetailsMockMvc.perform(delete("/api/leftover-details/{id}", leftoverDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean leftoverDetailsExistsInEs = leftoverDetailsSearchRepository.exists(leftoverDetails.getId());
        assertThat(leftoverDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<LeftoverDetails> leftoverDetailsList = leftoverDetailsRepository.findAll();
        assertThat(leftoverDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLeftoverDetails() throws Exception {
        // Initialize the database
        leftoverDetailsRepository.saveAndFlush(leftoverDetails);
        leftoverDetailsSearchRepository.save(leftoverDetails);

        // Search the leftoverDetails
        restLeftoverDetailsMockMvc.perform(get("/api/_search/leftover-details?query=id:" + leftoverDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leftoverDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID)))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.doubleValue())))
            .andExpect(jsonPath("$.[*].breadth").value(hasItem(DEFAULT_BREADTH.doubleValue())))
            .andExpect(jsonPath("$.[*].squareFeet").value(hasItem(DEFAULT_SQUARE_FEET.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeftoverDetails.class);
        LeftoverDetails leftoverDetails1 = new LeftoverDetails();
        leftoverDetails1.setId(1L);
        LeftoverDetails leftoverDetails2 = new LeftoverDetails();
        leftoverDetails2.setId(leftoverDetails1.getId());
        assertThat(leftoverDetails1).isEqualTo(leftoverDetails2);
        leftoverDetails2.setId(2L);
        assertThat(leftoverDetails1).isNotEqualTo(leftoverDetails2);
        leftoverDetails1.setId(null);
        assertThat(leftoverDetails1).isNotEqualTo(leftoverDetails2);
    }
}
