package com.simpleeffect.trucks.web.rest;

import com.simpleeffect.trucks.TrucksApp;

import com.simpleeffect.trucks.domain.Delivery;
import com.simpleeffect.trucks.repository.DeliveryRepository;
import com.simpleeffect.trucks.service.DeliveryService;
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
import java.time.LocalDate;
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
 * Test class for the DeliveryResource REST controller.
 *
 * @see DeliveryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrucksApp.class)
public class DeliveryResourceIntTest {

    private static final LocalDate DEFAULT_DATE_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_SIZE = 1L;
    private static final Long UPDATED_SIZE = 2L;

    private static final Long DEFAULT_WEIGHT = 1L;
    private static final Long UPDATED_WEIGHT = 2L;

    private static final String DEFAULT_PICKUP_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_PICKUP_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_DELIVERY_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_PICKUP_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PICKUP_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DELIVERY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Float DEFAULT_EXPECTED_INCOME = 1F;
    private static final Float UPDATED_EXPECTED_INCOME = 2F;

    private static final ZonedDateTime DEFAULT_PICKUP_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PICKUP_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DELIVERY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DELIVERY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDeliveryMockMvc;

    private Delivery delivery;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DeliveryResource deliveryResource = new DeliveryResource(deliveryService);
        this.restDeliveryMockMvc = MockMvcBuilders.standaloneSetup(deliveryResource)
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
    public static Delivery createEntity(EntityManager em) {
        Delivery delivery = new Delivery()
            .dateAdded(DEFAULT_DATE_ADDED)
            .size(DEFAULT_SIZE)
            .weight(DEFAULT_WEIGHT)
            .pickupLocation(DEFAULT_PICKUP_LOCATION)
            .deliveryLocation(DEFAULT_DELIVERY_LOCATION)
            .pickupDescription(DEFAULT_PICKUP_DESCRIPTION)
            .deliveryDescription(DEFAULT_DELIVERY_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .expectedIncome(DEFAULT_EXPECTED_INCOME)
            .pickupDate(DEFAULT_PICKUP_DATE)
            .deliveryDate(DEFAULT_DELIVERY_DATE);
        return delivery;
    }

    @Before
    public void initTest() {
        delivery = createEntity(em);
    }

    @Test
    @Transactional
    public void createDelivery() throws Exception {
        int databaseSizeBeforeCreate = deliveryRepository.findAll().size();

        // Create the Delivery
        restDeliveryMockMvc.perform(post("/api/deliveries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(delivery)))
            .andExpect(status().isCreated());

        // Validate the Delivery in the database
        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeCreate + 1);
        Delivery testDelivery = deliveryList.get(deliveryList.size() - 1);
        assertThat(testDelivery.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testDelivery.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testDelivery.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testDelivery.getPickupLocation()).isEqualTo(DEFAULT_PICKUP_LOCATION);
        assertThat(testDelivery.getDeliveryLocation()).isEqualTo(DEFAULT_DELIVERY_LOCATION);
        assertThat(testDelivery.getPickupDescription()).isEqualTo(DEFAULT_PICKUP_DESCRIPTION);
        assertThat(testDelivery.getDeliveryDescription()).isEqualTo(DEFAULT_DELIVERY_DESCRIPTION);
        assertThat(testDelivery.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDelivery.getExpectedIncome()).isEqualTo(DEFAULT_EXPECTED_INCOME);
        assertThat(testDelivery.getPickupDate()).isEqualTo(DEFAULT_PICKUP_DATE);
        assertThat(testDelivery.getDeliveryDate()).isEqualTo(DEFAULT_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void createDeliveryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = deliveryRepository.findAll().size();

        // Create the Delivery with an existing ID
        delivery.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliveryMockMvc.perform(post("/api/deliveries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(delivery)))
            .andExpect(status().isBadRequest());

        // Validate the Delivery in the database
        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDeliveries() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList
        restDeliveryMockMvc.perform(get("/api/deliveries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(delivery.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.intValue())))
            .andExpect(jsonPath("$.[*].pickupLocation").value(hasItem(DEFAULT_PICKUP_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].deliveryLocation").value(hasItem(DEFAULT_DELIVERY_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].pickupDescription").value(hasItem(DEFAULT_PICKUP_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].deliveryDescription").value(hasItem(DEFAULT_DELIVERY_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].expectedIncome").value(hasItem(DEFAULT_EXPECTED_INCOME.doubleValue())))
            .andExpect(jsonPath("$.[*].pickupDate").value(hasItem(sameInstant(DEFAULT_PICKUP_DATE))))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(sameInstant(DEFAULT_DELIVERY_DATE))));
    }

    @Test
    @Transactional
    public void getDelivery() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get the delivery
        restDeliveryMockMvc.perform(get("/api/deliveries/{id}", delivery.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(delivery.getId().intValue()))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.intValue()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.intValue()))
            .andExpect(jsonPath("$.pickupLocation").value(DEFAULT_PICKUP_LOCATION.toString()))
            .andExpect(jsonPath("$.deliveryLocation").value(DEFAULT_DELIVERY_LOCATION.toString()))
            .andExpect(jsonPath("$.pickupDescription").value(DEFAULT_PICKUP_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.deliveryDescription").value(DEFAULT_DELIVERY_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.expectedIncome").value(DEFAULT_EXPECTED_INCOME.doubleValue()))
            .andExpect(jsonPath("$.pickupDate").value(sameInstant(DEFAULT_PICKUP_DATE)))
            .andExpect(jsonPath("$.deliveryDate").value(sameInstant(DEFAULT_DELIVERY_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingDelivery() throws Exception {
        // Get the delivery
        restDeliveryMockMvc.perform(get("/api/deliveries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDelivery() throws Exception {
        // Initialize the database
        deliveryService.save(delivery);

        int databaseSizeBeforeUpdate = deliveryRepository.findAll().size();

        // Update the delivery
        Delivery updatedDelivery = deliveryRepository.findOne(delivery.getId());
        // Disconnect from session so that the updates on updatedDelivery are not directly saved in db
        em.detach(updatedDelivery);
        updatedDelivery
            .dateAdded(UPDATED_DATE_ADDED)
            .size(UPDATED_SIZE)
            .weight(UPDATED_WEIGHT)
            .pickupLocation(UPDATED_PICKUP_LOCATION)
            .deliveryLocation(UPDATED_DELIVERY_LOCATION)
            .pickupDescription(UPDATED_PICKUP_DESCRIPTION)
            .deliveryDescription(UPDATED_DELIVERY_DESCRIPTION)
            .status(UPDATED_STATUS)
            .expectedIncome(UPDATED_EXPECTED_INCOME)
            .pickupDate(UPDATED_PICKUP_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restDeliveryMockMvc.perform(put("/api/deliveries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDelivery)))
            .andExpect(status().isOk());

        // Validate the Delivery in the database
        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeUpdate);
        Delivery testDelivery = deliveryList.get(deliveryList.size() - 1);
        assertThat(testDelivery.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testDelivery.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testDelivery.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testDelivery.getPickupLocation()).isEqualTo(UPDATED_PICKUP_LOCATION);
        assertThat(testDelivery.getDeliveryLocation()).isEqualTo(UPDATED_DELIVERY_LOCATION);
        assertThat(testDelivery.getPickupDescription()).isEqualTo(UPDATED_PICKUP_DESCRIPTION);
        assertThat(testDelivery.getDeliveryDescription()).isEqualTo(UPDATED_DELIVERY_DESCRIPTION);
        assertThat(testDelivery.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDelivery.getExpectedIncome()).isEqualTo(UPDATED_EXPECTED_INCOME);
        assertThat(testDelivery.getPickupDate()).isEqualTo(UPDATED_PICKUP_DATE);
        assertThat(testDelivery.getDeliveryDate()).isEqualTo(UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingDelivery() throws Exception {
        int databaseSizeBeforeUpdate = deliveryRepository.findAll().size();

        // Create the Delivery

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDeliveryMockMvc.perform(put("/api/deliveries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(delivery)))
            .andExpect(status().isCreated());

        // Validate the Delivery in the database
        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDelivery() throws Exception {
        // Initialize the database
        deliveryService.save(delivery);

        int databaseSizeBeforeDelete = deliveryRepository.findAll().size();

        // Get the delivery
        restDeliveryMockMvc.perform(delete("/api/deliveries/{id}", delivery.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Delivery.class);
        Delivery delivery1 = new Delivery();
        delivery1.setId(1L);
        Delivery delivery2 = new Delivery();
        delivery2.setId(delivery1.getId());
        assertThat(delivery1).isEqualTo(delivery2);
        delivery2.setId(2L);
        assertThat(delivery1).isNotEqualTo(delivery2);
        delivery1.setId(null);
        assertThat(delivery1).isNotEqualTo(delivery2);
    }
}
