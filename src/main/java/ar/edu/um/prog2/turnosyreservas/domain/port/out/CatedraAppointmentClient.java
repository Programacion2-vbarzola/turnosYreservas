package ar.edu.um.prog2.turnosyreservas.domain.port.out;

import ar.edu.um.prog2.turnosyreservas.domain.model.Occupancy;
import java.time.LocalDate;
import java.util.List;

public interface CatedraAppointmentClient {
    List<Occupancy> getOccupancies(Long professionalId, LocalDate from, LocalDate to);
}
