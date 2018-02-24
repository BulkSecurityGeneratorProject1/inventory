package com.sonnetstone.inventory.web.rest;

import com.sonnetstone.inventory.InventoryApp;

import com.sonnetstone.inventory.domain.ProductDetails;
import com.sonnetstone.inventory.repository.ProductDetailsRepository;
import com.sonnetstone.inventory.repository.search.ProductDetailsSearchRepository;
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
 * Test class for the ProductDetailsResource REST controller.
 *
 * @see ProductDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InventoryApp.class)
public class ProductDetailsResourceIntTest {

    private static final Double DEFAULT_LENGTH = 1D;
    private static final Double UPDATED_LENGTH = 2D;

    private static final Double DEFAULT_BREADTH = 1D;
    private static final Double UPDATED_BREADTH = 2D;

    private static final Double DEFAULT_SQUARE_FEET = 1D;
    private static final Double UPDATED_SQUARE_FEET = 2D;

    private static final String DEFAULT_SOLD = "AAAAAAAAAA";
    private static final String UPDATED_SOLD = "BBBBBBBBBB";

    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    @Autowired
    private ProductDetailsSearchRepository productDetailsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProductDetailsMockMvc;

    private ProductDetails productDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductDetailsResource productDetailsResource = new ProductDetailsResource(productDetailsRepository, productDetailsSearchRepository);
        this.restProductDetailsMockMvc = MockMvcBuilders.standaloneSetup(productDetailsResource)
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
    public static ProductDetails createEntity(EntityManager em) {
        ProductDetails productDetails = new ProductDetails()
            .length(DEFAULT_LENGTH)
            .breadth(DEFAULT_BREADTH)
            .squareFeet(DEFAULT_SQUARE_FEET)
            .sold(DEFAULT_SOLD);
        return productDetails;
    }

    @Before
    public void initTest() {
        productDetailsSearchRepository.deleteAll();
        productDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductDetails() throws Exception {
        int databaseSizeBeforeCreate = productDetailsRepository.findAll().size();

        // Create the ProductDetails
        restProductDetailsMockMvc.perform(post("/api/product-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDetails)))
            .andExpect(status().isCreated());

        // Validate the ProductDetails in the database
        List<ProductDetails> productDetailsList = productDetailsRepository.findAll();
        assertThat(productDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        ProductDetails testProductDetails = productDetailsList.get(productDetailsList.size() - 1);
        assertThat(testProductDetails.getLength()).isEqualTo(DEFAULT_LENGTH);
        assertThat(testProductDetails.getBreadth()).isEqualTo(DEFAULT_BREADTH);
        assertThat(testProductDetails.getSquareFeet()).isEqualTo(DEFAULT_SQUARE_FEET);
        assertThat(testProductDetails.getSold()).isEqualTo(DEFAULT_SOLD);

        // Validate the ProductDetails in Elasticsearch
        ProductDetails productDetailsEs = productDetailsSearchRepository.findOne(testProductDetails.getId());
        assertThat(productDetailsEs).isEqualToIgnoringGivenFields(testProductDetails);
    }

    @Test
    @Transactional
    public void createProductDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productDetailsRepository.findAll().size();

        // Create the ProductDetails with an existing ID
        productDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductDetailsMockMvc.perform(post("/api/product-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDetails)))
            .andExpect(status().isBadRequest());

        // Validate the ProductDetails in the database
        List<ProductDetails> productDetailsList = productDetailsRepository.findAll();
        assertThat(productDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLengthIsRequired() throws Exception {
        int databaseSizeBeforeTest = productDetailsRepository.findAll().size();
        // set the field null
        productDetails.setLength(null);

        // Create the ProductDetails, which fails.

        restProductDetailsMockMvc.perform(post("/api/product-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDetails)))
            .andExpect(status().isBadRequest());

        List<ProductDetails> productDetailsList = productDetailsRepository.findAll();
        assertThat(productDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBreadthIsRequired() throws Exception {
        int databaseSizeBeforeTest = productDetailsRepository.findAll().size();
        // set the field null
        productDetails.setBreadth(null);

        // Create the ProductDetails, which fails.

        restProductDetailsMockMvc.perform(post("/api/product-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDetails)))
            .andExpect(status().isBadRequest());

        List<ProductDetails> productDetailsList = productDetailsRepository.findAll();
        assertThat(productDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSquareFeetIsRequired() throws Exception {
        int databaseSizeBeforeTest = productDetailsRepository.findAll().size();
        // set the field null
        productDetails.setSquareFeet(null);

        // Create the ProductDetails, which fails.

        restProductDetailsMockMvc.perform(post("/api/product-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDetails)))
            .andExpect(status().isBadRequest());

        List<ProductDetails> productDetailsList = productDetailsRepository.findAll();
        assertThat(productDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSoldIsRequired() throws Exception {
        int databaseSizeBeforeTest = productDetailsRepository.findAll().size();
        // set the field null
        productDetails.setSold(null);

        // Create the ProductDetails, which fails.

        restProductDetailsMockMvc.perform(post("/api/product-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDetails)))
            .andExpect(status().isBadRequest());

        List<ProductDetails> productDetailsList = productDetailsRepository.findAll();
        assertThat(productDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductDetails() throws Exception {
        // Initialize the database
        productDetailsRepository.saveAndFlush(productDetails);

        // Get all the productDetailsList
        restProductDetailsMockMvc.perform(get("/api/product-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.doubleValue())))
            .andExpect(jsonPath("$.[*].breadth").value(hasItem(DEFAULT_BREADTH.doubleValue())))
            .andExpect(jsonPath("$.[*].squareFeet").value(hasItem(DEFAULT_SQUARE_FEET.doubleValue())))
            .andExpect(jsonPath("$.[*].sold").value(hasItem(DEFAULT_SOLD.toString())));
    }

    @Test
    @Transactional
    public void getProductDetails() throws Exception {
        // Initialize the database
        productDetailsRepository.saveAndFlush(productDetails);

        // Get the productDetails
        restProductDetailsMockMvc.perform(get("/api/product-details/{id}", productDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productDetails.getId().intValue()))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH.doubleValue()))
            .andExpect(jsonPath("$.breadth").value(DEFAULT_BREADTH.doubleValue()))
            .andExpect(jsonPath("$.squareFeet").value(DEFAULT_SQUARE_FEET.doubleValue()))
            .andExpect(jsonPath("$.sold").value(DEFAULT_SOLD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProductDetails() throws Exception {
        // Get the productDetails
        restProductDetailsMockMvc.perform(get("/api/product-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductDetails() throws Exception {
        // Initialize the database
        productDetailsRepository.saveAndFlush(productDetails);
        productDetailsSearchRepository.save(productDetails);
        int databaseSizeBeforeUpdate = productDetailsRepository.findAll().size();

        // Update the productDetails
        ProductDetails updatedProductDetails = productDetailsRepository.findOne(productDetails.getId());
        // Disconnect from session so that the updates on updatedProductDetails are not directly saved in db
        em.detach(updatedProductDetails);
        updatedProductDetails
            .length(UPDATED_LENGTH)
            .breadth(UPDATED_BREADTH)
            .squareFeet(UPDATED_SQUARE_FEET)
            .sold(UPDATED_SOLD);

        restProductDetailsMockMvc.perform(put("/api/product-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProductDetails)))
            .andExpect(status().isOk());

        // Validate the ProductDetails in the database
        List<ProductDetails> productDetailsList = productDetailsRepository.findAll();
        assertThat(productDetailsList).hasSize(databaseSizeBeforeUpdate);
        ProductDetails testProductDetails = productDetailsList.get(productDetailsList.size() - 1);
        assertThat(testProductDetails.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testProductDetails.getBreadth()).isEqualTo(UPDATED_BREADTH);
        assertThat(testProductDetails.getSquareFeet()).isEqualTo(UPDATED_SQUARE_FEET);
        assertThat(testProductDetails.getSold()).isEqualTo(UPDATED_SOLD);

        // Validate the ProductDetails in Elasticsearch
        ProductDetails productDetailsEs = productDetailsSearchRepository.findOne(testProductDetails.getId());
        assertThat(productDetailsEs).isEqualToIgnoringGivenFields(testProductDetails);
    }

    @Test
    @Transactional
    public void updateNonExistingProductDetails() throws Exception {
        int databaseSizeBeforeUpdate = productDetailsRepository.findAll().size();

        // Create the ProductDetails

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProductDetailsMockMvc.perform(put("/api/product-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDetails)))
            .andExpect(status().isCreated());

        // Validate the ProductDetails in the database
        List<ProductDetails> productDetailsList = productDetailsRepository.findAll();
        assertThat(productDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProductDetails() throws Exception {
        // Initialize the database
        productDetailsRepository.saveAndFlush(productDetails);
        productDetailsSearchRepository.save(productDetails);
        int databaseSizeBeforeDelete = productDetailsRepository.findAll().size();

        // Get the productDetails
        restProductDetailsMockMvc.perform(delete("/api/product-details/{id}", productDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean productDetailsExistsInEs = productDetailsSearchRepository.exists(productDetails.getId());
        assertThat(productDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<ProductDetails> productDetailsList = productDetailsRepository.findAll();
        assertThat(productDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProductDetails() throws Exception {
        // Initialize the database
        productDetailsRepository.saveAndFlush(productDetails);
        productDetailsSearchRepository.save(productDetails);

        // Search the productDetails
        restProductDetailsMockMvc.perform(get("/api/_search/product-details?query=id:" + productDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.doubleValue())))
            .andExpect(jsonPath("$.[*].breadth").value(hasItem(DEFAULT_BREADTH.doubleValue())))
            .andExpect(jsonPath("$.[*].squareFeet").value(hasItem(DEFAULT_SQUARE_FEET.doubleValue())))
            .andExpect(jsonPath("$.[*].sold").value(hasItem(DEFAULT_SOLD.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductDetails.class);
        ProductDetails productDetails1 = new ProductDetails();
        productDetails1.setId(1L);
        ProductDetails productDetails2 = new ProductDetails();
        productDetails2.setId(productDetails1.getId());
        assertThat(productDetails1).isEqualTo(productDetails2);
        productDetails2.setId(2L);
        assertThat(productDetails1).isNotEqualTo(productDetails2);
        productDetails1.setId(null);
        assertThat(productDetails1).isNotEqualTo(productDetails2);
    }
}
