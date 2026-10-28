package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.repository;

import ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.entity.ReservationProcessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReservationProcessEntity.
 */
@Repository
public interface ReservationProcessJpaRepository extends JpaRepository<ReservationProcessEntity, Long> {}
