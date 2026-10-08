package ar.edu.um.prog2.turnosyreservas.web.rest;

import ar.edu.um.prog2.turnosyreservas.repository.ReservationProcessRepository;
import ar.edu.um.prog2.turnosyreservas.service.ReservationProcessService;
import ar.edu.um.prog2.turnosyreservas.service.dto.ReservationProcessDTO;
import ar.edu.um.prog2.turnosyreservas.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.prog2.turnosyreservas.domain.ReservationProcess}.
 */
@RestController
@RequestMapping("/api/reservation-processes")
public class ReservationProcessResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationProcessResource.class);

    private static final String ENTITY_NAME = "reservationProcess";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReservationProcessService reservationProcessService;

    private final ReservationProcessRepository reservationProcessRepository;

    public ReservationProcessResource(
        ReservationProcessService reservationProcessService,
        ReservationProcessRepository reservationProcessRepository
    ) {
        this.reservationProcessService = reservationProcessService;
        this.reservationProcessRepository = reservationProcessRepository;
    }

    /**
     * {@code POST  /reservation-processes} : Create a new reservationProcess.
     *
     * @param reservationProcessDTO the reservationProcessDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reservationProcessDTO, or with status {@code 400 (Bad Request)} if the reservationProcess has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReservationProcessDTO> createReservationProcess(@Valid @RequestBody ReservationProcessDTO reservationProcessDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ReservationProcess : {}", reservationProcessDTO);
        if (reservationProcessDTO.getId() != null) {
            throw new BadRequestAlertException("A new reservationProcess cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reservationProcessDTO = reservationProcessService.save(reservationProcessDTO);
        return ResponseEntity.created(new URI("/api/reservation-processes/" + reservationProcessDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, reservationProcessDTO.getId().toString()))
            .body(reservationProcessDTO);
    }

    /**
     * {@code PUT  /reservation-processes/:id} : Updates an existing reservationProcess.
     *
     * @param id the id of the reservationProcessDTO to save.
     * @param reservationProcessDTO the reservationProcessDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservationProcessDTO,
     * or with status {@code 400 (Bad Request)} if the reservationProcessDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reservationProcessDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReservationProcessDTO> updateReservationProcess(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReservationProcessDTO reservationProcessDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReservationProcess : {}, {}", id, reservationProcessDTO);
        if (reservationProcessDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reservationProcessDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reservationProcessRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reservationProcessDTO = reservationProcessService.update(reservationProcessDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reservationProcessDTO.getId().toString()))
            .body(reservationProcessDTO);
    }

    /**
     * {@code PATCH  /reservation-processes/:id} : Partial updates given fields of an existing reservationProcess, field will ignore if it is null
     *
     * @param id the id of the reservationProcessDTO to save.
     * @param reservationProcessDTO the reservationProcessDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservationProcessDTO,
     * or with status {@code 400 (Bad Request)} if the reservationProcessDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reservationProcessDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reservationProcessDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReservationProcessDTO> partialUpdateReservationProcess(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReservationProcessDTO reservationProcessDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReservationProcess partially : {}, {}", id, reservationProcessDTO);
        if (reservationProcessDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reservationProcessDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reservationProcessRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReservationProcessDTO> result = reservationProcessService.partialUpdate(reservationProcessDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reservationProcessDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reservation-processes} : get all the reservationProcesses.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reservationProcesses in body.
     */
    @GetMapping("")
    public List<ReservationProcessDTO> getAllReservationProcesses(
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("reservation-is-null".equals(filter)) {
            LOG.debug("REST request to get all ReservationProcesss where reservation is null");
            return reservationProcessService.findAllWhereReservationIsNull();
        }
        LOG.debug("REST request to get all ReservationProcesses");
        return reservationProcessService.findAll();
    }

    /**
     * {@code GET  /reservation-processes/:id} : get the "id" reservationProcess.
     *
     * @param id the id of the reservationProcessDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reservationProcessDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationProcessDTO> getReservationProcess(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReservationProcess : {}", id);
        Optional<ReservationProcessDTO> reservationProcessDTO = reservationProcessService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reservationProcessDTO);
    }

    /**
     * {@code DELETE  /reservation-processes/:id} : delete the "id" reservationProcess.
     *
     * @param id the id of the reservationProcessDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationProcess(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReservationProcess : {}", id);
        reservationProcessService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
