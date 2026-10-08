package ar.edu.um.prog2.turnosyreservas.web.rest;

import ar.edu.um.prog2.turnosyreservas.repository.HoldRepository;
import ar.edu.um.prog2.turnosyreservas.service.HoldService;
import ar.edu.um.prog2.turnosyreservas.service.dto.HoldDTO;
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
 * REST controller for managing {@link ar.edu.um.prog2.turnosyreservas.domain.Hold}.
 */
@RestController
@RequestMapping("/api/holds")
public class HoldResource {

    private static final Logger LOG = LoggerFactory.getLogger(HoldResource.class);

    private static final String ENTITY_NAME = "hold";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HoldService holdService;

    private final HoldRepository holdRepository;

    public HoldResource(HoldService holdService, HoldRepository holdRepository) {
        this.holdService = holdService;
        this.holdRepository = holdRepository;
    }

    /**
     * {@code POST  /holds} : Create a new hold.
     *
     * @param holdDTO the holdDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new holdDTO, or with status {@code 400 (Bad Request)} if the hold has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HoldDTO> createHold(@Valid @RequestBody HoldDTO holdDTO) throws URISyntaxException {
        LOG.debug("REST request to save Hold : {}", holdDTO);
        if (holdDTO.getId() != null) {
            throw new BadRequestAlertException("A new hold cannot already have an ID", ENTITY_NAME, "idexists");
        }
        holdDTO = holdService.save(holdDTO);
        return ResponseEntity.created(new URI("/api/holds/" + holdDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, holdDTO.getId().toString()))
            .body(holdDTO);
    }

    /**
     * {@code PUT  /holds/:id} : Updates an existing hold.
     *
     * @param id the id of the holdDTO to save.
     * @param holdDTO the holdDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated holdDTO,
     * or with status {@code 400 (Bad Request)} if the holdDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the holdDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HoldDTO> updateHold(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HoldDTO holdDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Hold : {}, {}", id, holdDTO);
        if (holdDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, holdDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!holdRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        holdDTO = holdService.update(holdDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, holdDTO.getId().toString()))
            .body(holdDTO);
    }

    /**
     * {@code PATCH  /holds/:id} : Partial updates given fields of an existing hold, field will ignore if it is null
     *
     * @param id the id of the holdDTO to save.
     * @param holdDTO the holdDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated holdDTO,
     * or with status {@code 400 (Bad Request)} if the holdDTO is not valid,
     * or with status {@code 404 (Not Found)} if the holdDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the holdDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HoldDTO> partialUpdateHold(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HoldDTO holdDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Hold partially : {}, {}", id, holdDTO);
        if (holdDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, holdDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!holdRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HoldDTO> result = holdService.partialUpdate(holdDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, holdDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /holds} : get all the holds.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of holds in body.
     */
    @GetMapping("")
    public List<HoldDTO> getAllHolds(
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("reservationprocess-is-null".equals(filter)) {
            LOG.debug("REST request to get all Holds where reservationProcess is null");
            return holdService.findAllWhereReservationProcessIsNull();
        }
        LOG.debug("REST request to get all Holds");
        return holdService.findAll();
    }

    /**
     * {@code GET  /holds/:id} : get the "id" hold.
     *
     * @param id the id of the holdDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the holdDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HoldDTO> getHold(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Hold : {}", id);
        Optional<HoldDTO> holdDTO = holdService.findOne(id);
        return ResponseUtil.wrapOrNotFound(holdDTO);
    }

    /**
     * {@code DELETE  /holds/:id} : delete the "id" hold.
     *
     * @param id the id of the holdDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHold(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Hold : {}", id);
        holdService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
