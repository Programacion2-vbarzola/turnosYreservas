package ar.edu.um.prog2.turnosyreservas.service;

import ar.edu.um.prog2.turnosyreservas.service.dto.HoldDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ar.edu.um.prog2.turnosyreservas.domain.Hold}.
 */
public interface HoldService {
    /**
     * Save a hold.
     *
     * @param holdDTO the entity to save.
     * @return the persisted entity.
     */
    HoldDTO save(HoldDTO holdDTO);

    /**
     * Updates a hold.
     *
     * @param holdDTO the entity to update.
     * @return the persisted entity.
     */
    HoldDTO update(HoldDTO holdDTO);

    /**
     * Partially updates a hold.
     *
     * @param holdDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HoldDTO> partialUpdate(HoldDTO holdDTO);

    /**
     * Get all the holds.
     *
     * @return the list of entities.
     */
    List<HoldDTO> findAll();

    /**
     * Get all the HoldDTO where ReservationProcess is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<HoldDTO> findAllWhereReservationProcessIsNull();

    /**
     * Get all the holds with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HoldDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" hold.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HoldDTO> findOne(Long id);

    /**
     * Delete the "id" hold.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
