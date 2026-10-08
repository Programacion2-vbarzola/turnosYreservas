package ar.edu.um.prog2.turnosyreservas.web.rest;

import static ar.edu.um.prog2.turnosyreservas.domain.ReservationProcessAsserts.*;
import static ar.edu.um.prog2.turnosyreservas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.prog2.turnosyreservas.IntegrationTest;
import ar.edu.um.prog2.turnosyreservas.domain.Hold;
import ar.edu.um.prog2.turnosyreservas.domain.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.domain.User;
import ar.edu.um.prog2.turnosyreservas.domain.enumeration.ProcessStatus;
import ar.edu.um.prog2.turnosyreservas.repository.ReservationProcessRepository;
import ar.edu.um.prog2.turnosyreservas.repository.UserRepository;
import ar.edu.um.prog2.turnosyreservas.service.ReservationProcessService;
import ar.edu.um.prog2.turnosyreservas.service.dto.ReservationProcessDTO;
import ar.edu.um.prog2.turnosyreservas.service.mapper.ReservationProcessMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReservationProcessResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReservationProcessResourceIT {

    private static final String DEFAULT_EXTERNAL_PROCESS_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_PROCESS_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_PROFESSIONAL_ID = 1L;
    private static final Long UPDATED_PROFESSIONAL_ID = 2L;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_START_TIME = "AAAAAAAA";
    private static final String UPDATED_START_TIME = "BBBBBBBB";

    private static final String DEFAULT_PATIENT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PATIENT_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATIENT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PATIENT_LAST_NAME = "BBBBBBBBBB";

    private static final ProcessStatus DEFAULT_STATUS = ProcessStatus.HELD;
    private static final ProcessStatus UPDATED_STATUS = ProcessStatus.WAITING_FOR_PHONE;

    private static final Instant DEFAULT_EXPIRES_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRES_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REQUEST_EVENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_EVENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/reservation-processes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReservationProcessRepository reservationProcessRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ReservationProcessRepository reservationProcessRepositoryMock;

    @Autowired
    private ReservationProcessMapper reservationProcessMapper;

    @Mock
    private ReservationProcessService reservationProcessServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReservationProcessMockMvc;

    private ReservationProcess reservationProcess;

    private ReservationProcess insertedReservationProcess;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReservationProcess createEntity(EntityManager em) {
        ReservationProcess reservationProcess = new ReservationProcess()
            .externalProcessId(DEFAULT_EXTERNAL_PROCESS_ID)
            .professionalId(DEFAULT_PROFESSIONAL_ID)
            .date(DEFAULT_DATE)
            .startTime(DEFAULT_START_TIME)
            .patientFirstName(DEFAULT_PATIENT_FIRST_NAME)
            .patientLastName(DEFAULT_PATIENT_LAST_NAME)
            .status(DEFAULT_STATUS)
            .expiresAt(DEFAULT_EXPIRES_AT)
            .requestEventId(DEFAULT_REQUEST_EVENT_ID)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Hold hold;
        if (TestUtil.findAll(em, Hold.class).isEmpty()) {
            hold = HoldResourceIT.createEntity(em);
            em.persist(hold);
            em.flush();
        } else {
            hold = TestUtil.findAll(em, Hold.class).get(0);
        }
        reservationProcess.setHold(hold);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        reservationProcess.setUser(user);
        return reservationProcess;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReservationProcess createUpdatedEntity(EntityManager em) {
        ReservationProcess updatedReservationProcess = new ReservationProcess()
            .externalProcessId(UPDATED_EXTERNAL_PROCESS_ID)
            .professionalId(UPDATED_PROFESSIONAL_ID)
            .date(UPDATED_DATE)
            .startTime(UPDATED_START_TIME)
            .patientFirstName(UPDATED_PATIENT_FIRST_NAME)
            .patientLastName(UPDATED_PATIENT_LAST_NAME)
            .status(UPDATED_STATUS)
            .expiresAt(UPDATED_EXPIRES_AT)
            .requestEventId(UPDATED_REQUEST_EVENT_ID)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Hold hold;
        if (TestUtil.findAll(em, Hold.class).isEmpty()) {
            hold = HoldResourceIT.createUpdatedEntity(em);
            em.persist(hold);
            em.flush();
        } else {
            hold = TestUtil.findAll(em, Hold.class).get(0);
        }
        updatedReservationProcess.setHold(hold);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedReservationProcess.setUser(user);
        return updatedReservationProcess;
    }

    @BeforeEach
    void initTest() {
        reservationProcess = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedReservationProcess != null) {
            reservationProcessRepository.delete(insertedReservationProcess);
            insertedReservationProcess = null;
        }
    }

    @Test
    @Transactional
    void createReservationProcess() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReservationProcess
        ReservationProcessDTO reservationProcessDTO = reservationProcessMapper.toDto(reservationProcess);
        var returnedReservationProcessDTO = om.readValue(
            restReservationProcessMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationProcessDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReservationProcessDTO.class
        );

        // Validate the ReservationProcess in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReservationProcess = reservationProcessMapper.toEntity(returnedReservationProcessDTO);
        assertReservationProcessUpdatableFieldsEquals(
            returnedReservationProcess,
            getPersistedReservationProcess(returnedReservationProcess)
        );

        insertedReservationProcess = returnedReservationProcess;
    }

    @Test
    @Transactional
    void createReservationProcessWithExistingId() throws Exception {
        // Create the ReservationProcess with an existing ID
        reservationProcess.setId(1L);
        ReservationProcessDTO reservationProcessDTO = reservationProcessMapper.toDto(reservationProcess);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservationProcessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationProcessDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReservationProcess in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExternalProcessIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reservationProcess.setExternalProcessId(null);

        // Create the ReservationProcess, which fails.
        ReservationProcessDTO reservationProcessDTO = reservationProcessMapper.toDto(reservationProcess);

        restReservationProcessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationProcessDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProfessionalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reservationProcess.setProfessionalId(null);

        // Create the ReservationProcess, which fails.
        ReservationProcessDTO reservationProcessDTO = reservationProcessMapper.toDto(reservationProcess);

        restReservationProcessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationProcessDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reservationProcess.setDate(null);

        // Create the ReservationProcess, which fails.
        ReservationProcessDTO reservationProcessDTO = reservationProcessMapper.toDto(reservationProcess);

        restReservationProcessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationProcessDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reservationProcess.setStartTime(null);

        // Create the ReservationProcess, which fails.
        ReservationProcessDTO reservationProcessDTO = reservationProcessMapper.toDto(reservationProcess);

        restReservationProcessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationProcessDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPatientFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reservationProcess.setPatientFirstName(null);

        // Create the ReservationProcess, which fails.
        ReservationProcessDTO reservationProcessDTO = reservationProcessMapper.toDto(reservationProcess);

        restReservationProcessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationProcessDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPatientLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reservationProcess.setPatientLastName(null);

        // Create the ReservationProcess, which fails.
        ReservationProcessDTO reservationProcessDTO = reservationProcessMapper.toDto(reservationProcess);

        restReservationProcessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationProcessDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reservationProcess.setStatus(null);

        // Create the ReservationProcess, which fails.
        ReservationProcessDTO reservationProcessDTO = reservationProcessMapper.toDto(reservationProcess);

        restReservationProcessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationProcessDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reservationProcess.setCreatedAt(null);

        // Create the ReservationProcess, which fails.
        ReservationProcessDTO reservationProcessDTO = reservationProcessMapper.toDto(reservationProcess);

        restReservationProcessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationProcessDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reservationProcess.setUpdatedAt(null);

        // Create the ReservationProcess, which fails.
        ReservationProcessDTO reservationProcessDTO = reservationProcessMapper.toDto(reservationProcess);

        restReservationProcessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationProcessDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReservationProcesses() throws Exception {
        // Initialize the database
        insertedReservationProcess = reservationProcessRepository.saveAndFlush(reservationProcess);

        // Get all the reservationProcessList
        restReservationProcessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservationProcess.getId().intValue())))
            .andExpect(jsonPath("$.[*].externalProcessId").value(hasItem(DEFAULT_EXTERNAL_PROCESS_ID)))
            .andExpect(jsonPath("$.[*].professionalId").value(hasItem(DEFAULT_PROFESSIONAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.[*].patientFirstName").value(hasItem(DEFAULT_PATIENT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].patientLastName").value(hasItem(DEFAULT_PATIENT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].expiresAt").value(hasItem(DEFAULT_EXPIRES_AT.toString())))
            .andExpect(jsonPath("$.[*].requestEventId").value(hasItem(DEFAULT_REQUEST_EVENT_ID)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReservationProcessesWithEagerRelationshipsIsEnabled() throws Exception {
        when(reservationProcessServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReservationProcessMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reservationProcessServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReservationProcessesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reservationProcessServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReservationProcessMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reservationProcessRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReservationProcess() throws Exception {
        // Initialize the database
        insertedReservationProcess = reservationProcessRepository.saveAndFlush(reservationProcess);

        // Get the reservationProcess
        restReservationProcessMockMvc
            .perform(get(ENTITY_API_URL_ID, reservationProcess.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reservationProcess.getId().intValue()))
            .andExpect(jsonPath("$.externalProcessId").value(DEFAULT_EXTERNAL_PROCESS_ID))
            .andExpect(jsonPath("$.professionalId").value(DEFAULT_PROFESSIONAL_ID.intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME))
            .andExpect(jsonPath("$.patientFirstName").value(DEFAULT_PATIENT_FIRST_NAME))
            .andExpect(jsonPath("$.patientLastName").value(DEFAULT_PATIENT_LAST_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.expiresAt").value(DEFAULT_EXPIRES_AT.toString()))
            .andExpect(jsonPath("$.requestEventId").value(DEFAULT_REQUEST_EVENT_ID))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReservationProcess() throws Exception {
        // Get the reservationProcess
        restReservationProcessMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReservationProcess() throws Exception {
        // Initialize the database
        insertedReservationProcess = reservationProcessRepository.saveAndFlush(reservationProcess);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reservationProcess
        ReservationProcess updatedReservationProcess = reservationProcessRepository.findById(reservationProcess.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReservationProcess are not directly saved in db
        em.detach(updatedReservationProcess);
        updatedReservationProcess
            .externalProcessId(UPDATED_EXTERNAL_PROCESS_ID)
            .professionalId(UPDATED_PROFESSIONAL_ID)
            .date(UPDATED_DATE)
            .startTime(UPDATED_START_TIME)
            .patientFirstName(UPDATED_PATIENT_FIRST_NAME)
            .patientLastName(UPDATED_PATIENT_LAST_NAME)
            .status(UPDATED_STATUS)
            .expiresAt(UPDATED_EXPIRES_AT)
            .requestEventId(UPDATED_REQUEST_EVENT_ID)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ReservationProcessDTO reservationProcessDTO = reservationProcessMapper.toDto(updatedReservationProcess);

        restReservationProcessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reservationProcessDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reservationProcessDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReservationProcess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReservationProcessToMatchAllProperties(updatedReservationProcess);
    }

    @Test
    @Transactional
    void putNonExistingReservationProcess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reservationProcess.setId(longCount.incrementAndGet());

        // Create the ReservationProcess
        ReservationProcessDTO reservationProcessDTO = reservationProcessMapper.toDto(reservationProcess);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservationProcessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reservationProcessDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reservationProcessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReservationProcess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReservationProcess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reservationProcess.setId(longCount.incrementAndGet());

        // Create the ReservationProcess
        ReservationProcessDTO reservationProcessDTO = reservationProcessMapper.toDto(reservationProcess);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationProcessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reservationProcessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReservationProcess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReservationProcess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reservationProcess.setId(longCount.incrementAndGet());

        // Create the ReservationProcess
        ReservationProcessDTO reservationProcessDTO = reservationProcessMapper.toDto(reservationProcess);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationProcessMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservationProcessDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReservationProcess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReservationProcessWithPatch() throws Exception {
        // Initialize the database
        insertedReservationProcess = reservationProcessRepository.saveAndFlush(reservationProcess);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reservationProcess using partial update
        ReservationProcess partialUpdatedReservationProcess = new ReservationProcess();
        partialUpdatedReservationProcess.setId(reservationProcess.getId());

        partialUpdatedReservationProcess
            .professionalId(UPDATED_PROFESSIONAL_ID)
            .date(UPDATED_DATE)
            .patientFirstName(UPDATED_PATIENT_FIRST_NAME)
            .patientLastName(UPDATED_PATIENT_LAST_NAME)
            .status(UPDATED_STATUS)
            .expiresAt(UPDATED_EXPIRES_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restReservationProcessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReservationProcess.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReservationProcess))
            )
            .andExpect(status().isOk());

        // Validate the ReservationProcess in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReservationProcessUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReservationProcess, reservationProcess),
            getPersistedReservationProcess(reservationProcess)
        );
    }

    @Test
    @Transactional
    void fullUpdateReservationProcessWithPatch() throws Exception {
        // Initialize the database
        insertedReservationProcess = reservationProcessRepository.saveAndFlush(reservationProcess);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reservationProcess using partial update
        ReservationProcess partialUpdatedReservationProcess = new ReservationProcess();
        partialUpdatedReservationProcess.setId(reservationProcess.getId());

        partialUpdatedReservationProcess
            .externalProcessId(UPDATED_EXTERNAL_PROCESS_ID)
            .professionalId(UPDATED_PROFESSIONAL_ID)
            .date(UPDATED_DATE)
            .startTime(UPDATED_START_TIME)
            .patientFirstName(UPDATED_PATIENT_FIRST_NAME)
            .patientLastName(UPDATED_PATIENT_LAST_NAME)
            .status(UPDATED_STATUS)
            .expiresAt(UPDATED_EXPIRES_AT)
            .requestEventId(UPDATED_REQUEST_EVENT_ID)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restReservationProcessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReservationProcess.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReservationProcess))
            )
            .andExpect(status().isOk());

        // Validate the ReservationProcess in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReservationProcessUpdatableFieldsEquals(
            partialUpdatedReservationProcess,
            getPersistedReservationProcess(partialUpdatedReservationProcess)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReservationProcess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reservationProcess.setId(longCount.incrementAndGet());

        // Create the ReservationProcess
        ReservationProcessDTO reservationProcessDTO = reservationProcessMapper.toDto(reservationProcess);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservationProcessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reservationProcessDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reservationProcessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReservationProcess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReservationProcess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reservationProcess.setId(longCount.incrementAndGet());

        // Create the ReservationProcess
        ReservationProcessDTO reservationProcessDTO = reservationProcessMapper.toDto(reservationProcess);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationProcessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reservationProcessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReservationProcess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReservationProcess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reservationProcess.setId(longCount.incrementAndGet());

        // Create the ReservationProcess
        ReservationProcessDTO reservationProcessDTO = reservationProcessMapper.toDto(reservationProcess);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservationProcessMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reservationProcessDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReservationProcess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReservationProcess() throws Exception {
        // Initialize the database
        insertedReservationProcess = reservationProcessRepository.saveAndFlush(reservationProcess);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reservationProcess
        restReservationProcessMockMvc
            .perform(delete(ENTITY_API_URL_ID, reservationProcess.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reservationProcessRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ReservationProcess getPersistedReservationProcess(ReservationProcess reservationProcess) {
        return reservationProcessRepository.findById(reservationProcess.getId()).orElseThrow();
    }

    protected void assertPersistedReservationProcessToMatchAllProperties(ReservationProcess expectedReservationProcess) {
        assertReservationProcessAllPropertiesEquals(expectedReservationProcess, getPersistedReservationProcess(expectedReservationProcess));
    }

    protected void assertPersistedReservationProcessToMatchUpdatableProperties(ReservationProcess expectedReservationProcess) {
        assertReservationProcessAllUpdatablePropertiesEquals(
            expectedReservationProcess,
            getPersistedReservationProcess(expectedReservationProcess)
        );
    }
}
