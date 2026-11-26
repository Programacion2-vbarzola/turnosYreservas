package ar.edu.um.prog2.turnosyreservas.reservation.domain.port.out;

import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.ReservationProcess;
import ar.edu.um.prog2.turnosyreservas.reservation.domain.model.enumeration.ProcessStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ReservationProcessRepositoryPort {
    ReservationProcess save(ReservationProcess reservationProcess);
    Optional<ReservationProcess> findByHoldId(Long holdId);
    Optional<ReservationProcess> findById(Long processId);
    Optional<ReservationProcess> findByExternalProcessId(String externalProcessId);
    List<ReservationProcess> findByStatusInAndExpiresAtBefore(List<ProcessStatus> statuses, Instant now);
}
