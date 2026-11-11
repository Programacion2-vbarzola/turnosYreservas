package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.repository;

import ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.entity.ReservationEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReservationEntity.
 */
@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByUserId(Long userid);
    Optional<ReservationEntity> findByProcessId(Long processId);
}
