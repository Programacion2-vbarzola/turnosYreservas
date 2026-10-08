package ar.edu.um.prog2.turnosyreservas.web.rest;

import static ar.edu.um.prog2.turnosyreservas.domain.HoldAsserts.*;
import static ar.edu.um.prog2.turnosyreservas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.prog2.turnosyreservas.IntegrationTest;
import ar.edu.um.prog2.turnosyreservas.domain.Hold;
import ar.edu.um.prog2.turnosyreservas.domain.User;
import ar.edu.um.prog2.turnosyreservas.domain.enumeration.HoldStatus;
import ar.edu.um.prog2.turnosyreservas.repository.HoldRepository;
import ar.edu.um.prog2.turnosyreservas.repository.UserRepository;
import ar.edu.um.prog2.turnosyreservas.service.HoldService;
import ar.edu.um.prog2.turnosyreservas.service.dto.HoldDTO;
import ar.edu.um.prog2.turnosyreservas.service.mapper.HoldMapper;
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
 * Integration tests for the {@link HoldResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HoldResourceIT {

    private static final String DEFAULT_EXTERNAL_HOLD_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_HOLD_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_PROCESS_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_PROCESS_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_PROFESSIONAL_ID = 1L;
    private static final Long UPDATED_PROFESSIONAL_ID = 2L;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_START_TIME = "AAAAAAAA";
    private static final String UPDATED_START_TIME = "BBBBBBBB";

    private static final String DEFAULT_END_TIME = "AAAAAAAA";
    private static final String UPDATED_END_TIME = "BBBBBBBB";

    private static final HoldStatus DEFAULT_STATUS = HoldStatus.HELD;
    private static final HoldStatus UPDATED_STATUS = HoldStatus.EXPIRED;

    private static final Instant DEFAULT_EXPIRES_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRES_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/holds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HoldRepository holdRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private HoldRepository holdRepositoryMock;

    @Autowired
    private HoldMapper holdMapper;

    @Mock
    private HoldService holdServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHoldMockMvc;

    private Hold hold;

    private Hold insertedHold;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hold createEntity(EntityManager em) {
        Hold hold = new Hold()
            .externalHoldId(DEFAULT_EXTERNAL_HOLD_ID)
            .externalProcessId(DEFAULT_EXTERNAL_PROCESS_ID)
            .professionalId(DEFAULT_PROFESSIONAL_ID)
            .date(DEFAULT_DATE)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .status(DEFAULT_STATUS)
            .expiresAt(DEFAULT_EXPIRES_AT)
            .createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        hold.setUser(user);
        return hold;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hold createUpdatedEntity(EntityManager em) {
        Hold updatedHold = new Hold()
            .externalHoldId(UPDATED_EXTERNAL_HOLD_ID)
            .externalProcessId(UPDATED_EXTERNAL_PROCESS_ID)
            .professionalId(UPDATED_PROFESSIONAL_ID)
            .date(UPDATED_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .status(UPDATED_STATUS)
            .expiresAt(UPDATED_EXPIRES_AT)
            .createdAt(UPDATED_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedHold.setUser(user);
        return updatedHold;
    }

    @BeforeEach
    void initTest() {
        hold = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedHold != null) {
            holdRepository.delete(insertedHold);
            insertedHold = null;
        }
    }

    @Test
    @Transactional
    void createHold() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Hold
        HoldDTO holdDTO = holdMapper.toDto(hold);
        var returnedHoldDTO = om.readValue(
            restHoldMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(holdDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HoldDTO.class
        );

        // Validate the Hold in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHold = holdMapper.toEntity(returnedHoldDTO);
        assertHoldUpdatableFieldsEquals(returnedHold, getPersistedHold(returnedHold));

        insertedHold = returnedHold;
    }

    @Test
    @Transactional
    void createHoldWithExistingId() throws Exception {
        // Create the Hold with an existing ID
        hold.setId(1L);
        HoldDTO holdDTO = holdMapper.toDto(hold);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHoldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(holdDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Hold in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExternalHoldIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hold.setExternalHoldId(null);

        // Create the Hold, which fails.
        HoldDTO holdDTO = holdMapper.toDto(hold);

        restHoldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(holdDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExternalProcessIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hold.setExternalProcessId(null);

        // Create the Hold, which fails.
        HoldDTO holdDTO = holdMapper.toDto(hold);

        restHoldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(holdDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProfessionalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hold.setProfessionalId(null);

        // Create the Hold, which fails.
        HoldDTO holdDTO = holdMapper.toDto(hold);

        restHoldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(holdDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hold.setDate(null);

        // Create the Hold, which fails.
        HoldDTO holdDTO = holdMapper.toDto(hold);

        restHoldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(holdDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hold.setStartTime(null);

        // Create the Hold, which fails.
        HoldDTO holdDTO = holdMapper.toDto(hold);

        restHoldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(holdDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hold.setStatus(null);

        // Create the Hold, which fails.
        HoldDTO holdDTO = holdMapper.toDto(hold);

        restHoldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(holdDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpiresAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hold.setExpiresAt(null);

        // Create the Hold, which fails.
        HoldDTO holdDTO = holdMapper.toDto(hold);

        restHoldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(holdDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hold.setCreatedAt(null);

        // Create the Hold, which fails.
        HoldDTO holdDTO = holdMapper.toDto(hold);

        restHoldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(holdDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHolds() throws Exception {
        // Initialize the database
        insertedHold = holdRepository.saveAndFlush(hold);

        // Get all the holdList
        restHoldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hold.getId().intValue())))
            .andExpect(jsonPath("$.[*].externalHoldId").value(hasItem(DEFAULT_EXTERNAL_HOLD_ID)))
            .andExpect(jsonPath("$.[*].externalProcessId").value(hasItem(DEFAULT_EXTERNAL_PROCESS_ID)))
            .andExpect(jsonPath("$.[*].professionalId").value(hasItem(DEFAULT_PROFESSIONAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].expiresAt").value(hasItem(DEFAULT_EXPIRES_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHoldsWithEagerRelationshipsIsEnabled() throws Exception {
        when(holdServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHoldMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(holdServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHoldsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(holdServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHoldMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(holdRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHold() throws Exception {
        // Initialize the database
        insertedHold = holdRepository.saveAndFlush(hold);

        // Get the hold
        restHoldMockMvc
            .perform(get(ENTITY_API_URL_ID, hold.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hold.getId().intValue()))
            .andExpect(jsonPath("$.externalHoldId").value(DEFAULT_EXTERNAL_HOLD_ID))
            .andExpect(jsonPath("$.externalProcessId").value(DEFAULT_EXTERNAL_PROCESS_ID))
            .andExpect(jsonPath("$.professionalId").value(DEFAULT_PROFESSIONAL_ID.intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.expiresAt").value(DEFAULT_EXPIRES_AT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHold() throws Exception {
        // Get the hold
        restHoldMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHold() throws Exception {
        // Initialize the database
        insertedHold = holdRepository.saveAndFlush(hold);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hold
        Hold updatedHold = holdRepository.findById(hold.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHold are not directly saved in db
        em.detach(updatedHold);
        updatedHold
            .externalHoldId(UPDATED_EXTERNAL_HOLD_ID)
            .externalProcessId(UPDATED_EXTERNAL_PROCESS_ID)
            .professionalId(UPDATED_PROFESSIONAL_ID)
            .date(UPDATED_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .status(UPDATED_STATUS)
            .expiresAt(UPDATED_EXPIRES_AT)
            .createdAt(UPDATED_CREATED_AT);
        HoldDTO holdDTO = holdMapper.toDto(updatedHold);

        restHoldMockMvc
            .perform(put(ENTITY_API_URL_ID, holdDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(holdDTO)))
            .andExpect(status().isOk());

        // Validate the Hold in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHoldToMatchAllProperties(updatedHold);
    }

    @Test
    @Transactional
    void putNonExistingHold() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hold.setId(longCount.incrementAndGet());

        // Create the Hold
        HoldDTO holdDTO = holdMapper.toDto(hold);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHoldMockMvc
            .perform(put(ENTITY_API_URL_ID, holdDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(holdDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Hold in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHold() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hold.setId(longCount.incrementAndGet());

        // Create the Hold
        HoldDTO holdDTO = holdMapper.toDto(hold);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(holdDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hold in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHold() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hold.setId(longCount.incrementAndGet());

        // Create the Hold
        HoldDTO holdDTO = holdMapper.toDto(hold);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoldMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(holdDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hold in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHoldWithPatch() throws Exception {
        // Initialize the database
        insertedHold = holdRepository.saveAndFlush(hold);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hold using partial update
        Hold partialUpdatedHold = new Hold();
        partialUpdatedHold.setId(hold.getId());

        partialUpdatedHold
            .externalProcessId(UPDATED_EXTERNAL_PROCESS_ID)
            .date(UPDATED_DATE)
            .endTime(UPDATED_END_TIME)
            .status(UPDATED_STATUS)
            .expiresAt(UPDATED_EXPIRES_AT)
            .createdAt(UPDATED_CREATED_AT);

        restHoldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHold.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHold))
            )
            .andExpect(status().isOk());

        // Validate the Hold in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHoldUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedHold, hold), getPersistedHold(hold));
    }

    @Test
    @Transactional
    void fullUpdateHoldWithPatch() throws Exception {
        // Initialize the database
        insertedHold = holdRepository.saveAndFlush(hold);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hold using partial update
        Hold partialUpdatedHold = new Hold();
        partialUpdatedHold.setId(hold.getId());

        partialUpdatedHold
            .externalHoldId(UPDATED_EXTERNAL_HOLD_ID)
            .externalProcessId(UPDATED_EXTERNAL_PROCESS_ID)
            .professionalId(UPDATED_PROFESSIONAL_ID)
            .date(UPDATED_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .status(UPDATED_STATUS)
            .expiresAt(UPDATED_EXPIRES_AT)
            .createdAt(UPDATED_CREATED_AT);

        restHoldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHold.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHold))
            )
            .andExpect(status().isOk());

        // Validate the Hold in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHoldUpdatableFieldsEquals(partialUpdatedHold, getPersistedHold(partialUpdatedHold));
    }

    @Test
    @Transactional
    void patchNonExistingHold() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hold.setId(longCount.incrementAndGet());

        // Create the Hold
        HoldDTO holdDTO = holdMapper.toDto(hold);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHoldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, holdDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(holdDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hold in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHold() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hold.setId(longCount.incrementAndGet());

        // Create the Hold
        HoldDTO holdDTO = holdMapper.toDto(hold);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(holdDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hold in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHold() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hold.setId(longCount.incrementAndGet());

        // Create the Hold
        HoldDTO holdDTO = holdMapper.toDto(hold);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoldMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(holdDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hold in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHold() throws Exception {
        // Initialize the database
        insertedHold = holdRepository.saveAndFlush(hold);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hold
        restHoldMockMvc
            .perform(delete(ENTITY_API_URL_ID, hold.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return holdRepository.count();
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

    protected Hold getPersistedHold(Hold hold) {
        return holdRepository.findById(hold.getId()).orElseThrow();
    }

    protected void assertPersistedHoldToMatchAllProperties(Hold expectedHold) {
        assertHoldAllPropertiesEquals(expectedHold, getPersistedHold(expectedHold));
    }

    protected void assertPersistedHoldToMatchUpdatableProperties(Hold expectedHold) {
        assertHoldAllUpdatablePropertiesEquals(expectedHold, getPersistedHold(expectedHold));
    }
}
