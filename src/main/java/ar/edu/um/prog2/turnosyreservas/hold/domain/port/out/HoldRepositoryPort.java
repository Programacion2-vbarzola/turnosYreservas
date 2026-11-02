package ar.edu.um.prog2.turnosyreservas.hold.domain.port.out;

import ar.edu.um.prog2.turnosyreservas.hold.domain.model.Hold;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface HoldRepositoryPort {
    Hold save(Hold hold);
    boolean existsActiveHoldForSlot(Long professionalId, LocalDate date, LocalTime startTime);
    Optional<Hold> findById(Long holdId);
}
//contrato para las operaciones de persistencia
