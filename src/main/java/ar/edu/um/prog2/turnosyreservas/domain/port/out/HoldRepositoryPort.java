package ar.edu.um.prog2.turnosyreservas.domain.port.out;

import ar.edu.um.prog2.turnosyreservas.domain.Hold;
import java.time.LocalDate;
import java.time.LocalTime;

public interface HoldRepositoryPort {
    Hold save(Hold hold);
    boolean existsActiveHoldForSlot(Long professionalId, LocalDate date, LocalTime startTime);
}
//contrato para las operaciones de persistencia
