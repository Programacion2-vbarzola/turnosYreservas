package ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.adapter;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out.ProcessedEventRepositoryPort;
import ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.entity.ProcessedEventEntity;
import ar.edu.um.prog2.turnosyreservas.reservation.infrastructure.persistence.repository.ProcessedEventJpaRepository;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class ProcessedEventPersistenceAdapter implements ProcessedEventRepositoryPort {

    private final ProcessedEventJpaRepository processedEventJpaRepository;

    ProcessedEventPersistenceAdapter(ProcessedEventJpaRepository processedEventJpaRepository) {
        this.processedEventJpaRepository = processedEventJpaRepository;
    }

    @Override
    public boolean existsByEventId(String eventId) {
        return processedEventJpaRepository.existsByEventId(eventId);
    }

    @Override
    public void save(String eventId) {
        ProcessedEventEntity entity = new ProcessedEventEntity();
        entity.setEventId(eventId);
        entity.setProcessedAt(Instant.now());
        processedEventJpaRepository.save(entity);
    }
}
