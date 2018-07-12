package com.simpleeffect.trucks.web.rest;

import com.simpleeffect.trucks.TrucksApp;

import com.simpleeffect.trucks.domain.Cost;
import com.simpleeffect.trucks.repository.CostRepository;
import com.simpleeffect.trucks.service.CostService;
import com.simpleeffect.trucks.web.rest.errors.ExceptionTranslator;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.simpleeffect.trucks.web.rest.TestUtil.sameInstant;
import static com.simpleeffect.trucks.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CostResource REST controller.
 *
 * @see CostResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrucksApp.class)
public class CostResourceIntTest {

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Float DEFAULT_AMOUNT = 1F;
    private static final Float UPDATED_AMOUNT = 2F;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private CostRepository costRepository;

    @Autowired
    private CostService costService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCostMockMvc;

    private Cost cost;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CostResource costResource = new CostResource(costService);
        this.restCostMockMvc = MockMvcBuilders.standaloneSetup(costResource)
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
    public static Cost createEntity(EntityManager em) {
        Cost cost = new Cost()
            .location(DEFAULT_LOCATION)
            .time(DEFAULT_TIME)
            .amount(DEFAULT_AMOUNT)
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return cost;
    }

    @Before
    public void initTest() {
        cost = createEntity(em);
    }

    @Test
    @Transactional
    public void createCost() throws Exception {
        int databaseSizeBeforeCreate = costRepository.findAll().size();

        // Create the Cost
        restCostMockMvc.perform(post("/api/costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cost)))
            .andExpect(status().isCreated());

        // Validate the Cost in the database
        List<Cost> costList = costRepository.findAll();
        assertThat(costList).hasSize(databaseSizeBeforeCreate + 1);
        Cost testCost = costList.get(costList.size() - 1);
        assertThat(testCost.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testCost.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testCost.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testCost.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCost.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createCostWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = costRepository.findAll().size();

        // Create the Cost with an existing ID
        cost.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCostMockMvc.perform(post("/api/costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cost)))
            .andExpect(status().isBadRequest());

        // Validate the Cost in the database
        List<Cost> costList = costRepository.findAll();
        assertThat(costList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCosts() throws Exception {
        // Initialize the database
        costRepository.saveAndFlush(cost);

        // Get all the costList
        restCostMockMvc.perform(get("/api/costs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cost.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getCost() throws Exception {
        // Initialize the database
        costRepository.saveAndFlush(cost);

        // Get the cost
        restCostMockMvc.perform(get("/api/costs/{id}", cost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cost.getId().intValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.time").value(sameInstant(DEFAULT_TIME)))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCost() throws Exception {
        // Get the cost
        restCostMockMvc.perform(get("/api/costs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCost() throws Exception {
        // Initialize the database
        costService.save(cost);

        int databaseSizeBeforeUpdate = costRepository.findAll().size();

        // Update the cost
        Cost updatedCost = costRepository.findOne(cost.getId());
        // Disconnect from session so that the updates on updatedCost are not directly saved in db
        em.detach(updatedCost);
        updatedCost
            .location(UPDATED_LOCATION)
            .time(UPDATED_TIME)
            .amount(UPDATED_AMOUNT)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION);

        restCostMockMvc.perform(put("/api/costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCost)))
            .andExpect(status().isOk());

        // Validate the Cost in the database
        List<Cost> costList = costRepository.findAll();
        assertThat(costList).hasSize(databaseSizeBeforeUpdate);
        Cost testCost = costList.get(costList.size() - 1);
        assertThat(testCost.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testCost.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testCost.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testCost.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCost.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingCost() throws Exception {
        int databaseSizeBeforeUpdate = costRepository.findAll().size();

        // Create the Cost

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCostMockMvc.perform(put("/api/costs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cost)))
            .andExpect(status().isCreated());

        // Validate the Cost in the database
        List<Cost> costList = costRepository.findAll();
        assertThat(costList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCost() throws Exception {
        // Initialize the database
        costService.save(cost);

        int databaseSizeBeforeDelete = costRepository.findAll().size();

        // Get the cost
        restCostMockMvc.perform(delete("/api/costs/{id}", cost.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cost> costList = costRepository.findAll();
        assertThat(costList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cost.class);
        Cost cost1 = new Cost();
        cost1.setId(1L);
        Cost cost2 = new Cost();
        cost2.setId(cost1.getId());
        assertThat(cost1).isEqualTo(cost2);
        cost2.setId(2L);
        assertThat(cost1).isNotEqualTo(cost2);
        cost1.setId(null);
        assertThat(cost1).isNotEqualTo(cost2);
    }
}
