package ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out;

public interface ProcessedEventRepositoryPort {
    boolean existsByEventId(String eventId);
    void save(String eventId);
}
