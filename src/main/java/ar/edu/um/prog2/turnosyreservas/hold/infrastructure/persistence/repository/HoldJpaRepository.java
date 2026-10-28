package ar.edu.um.prog2.turnosyreservas.hold.infrastructure.persistence.repository;

import ar.edu.um.prog2.turnosyreservas.hold.domain.model.enumeration.HoldStatus;
import ar.edu.um.prog2.turnosyreservas.hold.infrastructure.persistence.entity.HoldEntity;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HoldEntity.
 */
@Repository
public interface HoldJpaRepository extends JpaRepository<HoldEntity, Long> {
    boolean existsByProfessionalIdAndDateAndStartTimeAndStatus(Long professionalId, LocalDate date, String startTime, HoldStatus status);
}
