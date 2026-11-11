package ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.Reservation;
import java.util.List;
import java.util.Optional;

public interface ReservationRepositoryPort {
    Reservation save(Reservation reservation);
    List<Reservation> findByUserId(Long userId);
    Optional<Reservation> findByProcessId(Long processId);
}
