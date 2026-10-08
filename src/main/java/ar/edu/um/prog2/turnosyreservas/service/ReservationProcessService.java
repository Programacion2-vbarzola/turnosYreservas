package ar.edu.um.prog2.turnosyreservas.service;

import ar.edu.um.prog2.turnosyreservas.service.dto.ReservationProcessDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ar.edu.um.prog2.turnosyreservas.domain.ReservationProcess}.
 */
public interface ReservationProcessService {
    /**
     * Save a reservationProcess.
     *
     * @param reservationProcessDTO the entity to save.
     * @return the persisted entity.
     */
    ReservationProcessDTO save(ReservationProcessDTO reservationProcessDTO);

    /**
     * Updates a reservationProcess.
     *
     * @param reservationProcessDTO the entity to update.
     * @return the persisted entity.
     */
    ReservationProcessDTO update(ReservationProcessDTO reservationProcessDTO);

    /**
     * Partially updates a reservationProcess.
     *
     * @param reservationProcessDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReservationProcessDTO> partialUpdate(ReservationProcessDTO reservationProcessDTO);

    /**
     * Get all the reservationProcesses.
     *
     * @return the list of entities.
     */
    List<ReservationProcessDTO> findAll();

    /**
     * Get all the ReservationProcessDTO where Reservation is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ReservationProcessDTO> findAllWhereReservationIsNull();

    /**
     * Get all the reservationProcesses with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReservationProcessDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" reservationProcess.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReservationProcessDTO> findOne(Long id);

    /**
     * Delete the "id" reservationProcess.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
