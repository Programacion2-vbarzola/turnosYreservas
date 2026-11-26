package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.repository;

import ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.entity.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedEventJpaRepository extends JpaRepository<ProcessedEventEntity, Long> {
    boolean existsByEventId(String eventId);
}
