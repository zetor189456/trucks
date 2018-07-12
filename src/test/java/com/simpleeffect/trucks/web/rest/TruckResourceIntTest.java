package com.simpleeffect.trucks.web.rest;

import com.simpleeffect.trucks.TrucksApp;

import com.simpleeffect.trucks.domain.Truck;
import com.simpleeffect.trucks.repository.TruckRepository;
import com.simpleeffect.trucks.service.TruckService;
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
import java.util.List;

import static com.simpleeffect.trucks.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TruckResource REST controller.
 *
 * @see TruckResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrucksApp.class)
public class TruckResourceIntTest {

    private static final String DEFAULT_DRIVER = "AAAAAAAAAA";
    private static final String UPDATED_DRIVER = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRATION = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private TruckRepository truckRepository;

    @Autowired
    private TruckService truckService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTruckMockMvc;

    private Truck truck;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TruckResource truckResource = new TruckResource(truckService);
        this.restTruckMockMvc = MockMvcBuilders.standaloneSetup(truckResource)
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
    public static Truck createEntity(EntityManager em) {
        Truck truck = new Truck()
            .driver(DEFAULT_DRIVER)
            .registration(DEFAULT_REGISTRATION)
            .type(DEFAULT_TYPE);
        return truck;
    }

    @Before
    public void initTest() {
        truck = createEntity(em);
    }

    @Test
    @Transactional
    public void createTruck() throws Exception {
        int databaseSizeBeforeCreate = truckRepository.findAll().size();

        // Create the Truck
        restTruckMockMvc.perform(post("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truck)))
            .andExpect(status().isCreated());

        // Validate the Truck in the database
        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeCreate + 1);
        Truck testTruck = truckList.get(truckList.size() - 1);
        assertThat(testTruck.getDriver()).isEqualTo(DEFAULT_DRIVER);
        assertThat(testTruck.getRegistration()).isEqualTo(DEFAULT_REGISTRATION);
        assertThat(testTruck.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createTruckWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = truckRepository.findAll().size();

        // Create the Truck with an existing ID
        truck.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTruckMockMvc.perform(post("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truck)))
            .andExpect(status().isBadRequest());

        // Validate the Truck in the database
        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTrucks() throws Exception {
        // Initialize the database
        truckRepository.saveAndFlush(truck);

        // Get all the truckList
        restTruckMockMvc.perform(get("/api/trucks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(truck.getId().intValue())))
            .andExpect(jsonPath("$.[*].driver").value(hasItem(DEFAULT_DRIVER.toString())))
            .andExpect(jsonPath("$.[*].registration").value(hasItem(DEFAULT_REGISTRATION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getTruck() throws Exception {
        // Initialize the database
        truckRepository.saveAndFlush(truck);

        // Get the truck
        restTruckMockMvc.perform(get("/api/trucks/{id}", truck.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(truck.getId().intValue()))
            .andExpect(jsonPath("$.driver").value(DEFAULT_DRIVER.toString()))
            .andExpect(jsonPath("$.registration").value(DEFAULT_REGISTRATION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTruck() throws Exception {
        // Get the truck
        restTruckMockMvc.perform(get("/api/trucks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTruck() throws Exception {
        // Initialize the database
        truckService.save(truck);

        int databaseSizeBeforeUpdate = truckRepository.findAll().size();

        // Update the truck
        Truck updatedTruck = truckRepository.findOne(truck.getId());
        // Disconnect from session so that the updates on updatedTruck are not directly saved in db
        em.detach(updatedTruck);
        updatedTruck
            .driver(UPDATED_DRIVER)
            .registration(UPDATED_REGISTRATION)
            .type(UPDATED_TYPE);

        restTruckMockMvc.perform(put("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTruck)))
            .andExpect(status().isOk());

        // Validate the Truck in the database
        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeUpdate);
        Truck testTruck = truckList.get(truckList.size() - 1);
        assertThat(testTruck.getDriver()).isEqualTo(UPDATED_DRIVER);
        assertThat(testTruck.getRegistration()).isEqualTo(UPDATED_REGISTRATION);
        assertThat(testTruck.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingTruck() throws Exception {
        int databaseSizeBeforeUpdate = truckRepository.findAll().size();

        // Create the Truck

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTruckMockMvc.perform(put("/api/trucks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(truck)))
            .andExpect(status().isCreated());

        // Validate the Truck in the database
        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTruck() throws Exception {
        // Initialize the database
        truckService.save(truck);

        int databaseSizeBeforeDelete = truckRepository.findAll().size();

        // Get the truck
        restTruckMockMvc.perform(delete("/api/trucks/{id}", truck.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Truck> truckList = truckRepository.findAll();
        assertThat(truckList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Truck.class);
        Truck truck1 = new Truck();
        truck1.setId(1L);
        Truck truck2 = new Truck();
        truck2.setId(truck1.getId());
        assertThat(truck1).isEqualTo(truck2);
        truck2.setId(2L);
        assertThat(truck1).isNotEqualTo(truck2);
        truck1.setId(null);
        assertThat(truck1).isNotEqualTo(truck2);
    }
}
